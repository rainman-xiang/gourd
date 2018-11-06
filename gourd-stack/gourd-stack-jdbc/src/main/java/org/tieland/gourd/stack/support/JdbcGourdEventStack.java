package org.tieland.gourd.stack.support;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.tieland.gourd.common.GourdEvent;
import org.tieland.gourd.stack.api.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhouxiang
 * @date 2018/10/29 14:09
 */
@Slf4j
public class JdbcGourdEventStack implements GourdEventStack {

    private final String QUERY_SQL = " SELECT * FROM gourd_consumer_log WHERE id=? ";

    private JdbcTemplate jdbcTemplate;

    private String serviceId;

    public JdbcGourdEventStack(final String serviceId){
        this.serviceId = serviceId;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public GourdEvent get(String key) {
        try{
            GourdEventNode node = jdbcTemplate.queryForObject(QUERY_SQL, new GourdEventNodeRowMapper(), new Object[]{key});
            return node.getEvent();
        }catch (EmptyResultDataAccessException ex){
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createNew(GourdEvent event) {
        String sql = " INSERT INTO gourd_consumer_log(id, service_id, payload, queue, create_time," +
                " status, handle_count, last_handle_time, version) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?) ";
        try{
            jdbcTemplate.update(sql, new Object[]{ event.getId(), serviceId, event.getPayload(), event.getQueue(),
                    new Date(), HandleStatus.NEW.getCode(), 0, null, 0});
        }catch (DuplicateKeyException ex){
            log.error("", ex);
            throw new DuplicateException(String.format(" %s is Duplicate ", event.getId()));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void trigger(GourdEvent event) {
        GourdEventNode node = jdbcTemplate.queryForObject(QUERY_SQL, new GourdEventNodeRowMapper(), event.getId());
        HandleNode handleNode = node.getHandleNode();
        String sql = "UPDATE gourd_consumer_log SET status=?, handle_count=?, last_handle_time=?, version=version+1 " +
                " WHERE id=? and version=? ";
        Integer result = jdbcTemplate.update(sql, new Object[]{HandleStatus.PROCESSING.getCode(), handleNode.getTimes()+1, new Date(),
                event.getId(), handleNode.getVersion()});
        if(result == 0){
            log.error(" not update trigger success ");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void succeed(String key) {
        GourdEventNode node = jdbcTemplate.queryForObject(QUERY_SQL, new GourdEventNodeRowMapper(), key);
        HandleNode handleNode = node.getHandleNode();
        if(handleNode.getStatus() != HandleStatus.PROCESSING
                && handleNode.getStatus() != HandleStatus.RETRYING){
            log.error(" GourdEventNode is ready failed or success, can not update. handleNode:{}", handleNode);
            return;
        }

        String sql = "UPDATE gourd_consumer_log SET status=?, version=version+1 WHERE id=? and version=? ";
        Integer result = jdbcTemplate.update(sql, new Object[]{HandleStatus.SUCCEED.getCode(), key, handleNode.getVersion()});
        if(result == 0){
            log.error(" not update succeed success ");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void failed(String key) {
        GourdEventNode node = jdbcTemplate.queryForObject(QUERY_SQL, new GourdEventNodeRowMapper(), key);
        HandleNode handleNode = node.getHandleNode();
        if(handleNode.getStatus() != HandleStatus.PROCESSING
                && handleNode.getStatus() != HandleStatus.RETRYING){
            log.error(" GourdEventNode is ready failed or success, can not update. handleNode:{}", handleNode);
            return;
        }

        String sql = "UPDATE gourd_consumer_log SET status=?, version=version+1 WHERE id=? and version=? ";
        Integer result = jdbcTemplate.update(sql, new Object[]{HandleStatus.FAILED.getCode(), key, handleNode.getVersion()});
        if(result == 0){
            log.error(" not update failed success ");
        }
    }

    @Override
    public void remove(String key) {

    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public HandleNode getHandleNode(String key) {
        GourdEventNode node = jdbcTemplate.queryForObject(QUERY_SQL, new GourdEventNodeRowMapper(), key);
        HandleNode handleNode = node.getHandleNode();
        if(handleNode == null){
            log.error(" getHandleNode error, gourd key:{} ", key);
            throw new StackException(" GourdNode is not exist ");
        }

        return handleNode;
    }

    @Override
    public Integer getRetryCount(Integer maxRetry) {
        String sql = "SELECT count(1) FROM gourd_consumer_log WHERE " +
                "  service_id=? AND handle_count<? AND status<>? ";
        return jdbcTemplate.queryForObject(sql, new Object[]{serviceId, maxRetry, HandleStatus.SUCCEED.getCode()}, Integer.class);
    }

    @Override
    public List<GourdEvent> getRetryList(Integer maxRetry, Integer size) {
        String sql = "SELECT * FROM gourd_consumer_log WHERE " +
                " service_id=? AND handle_count<? AND status<>? order by last_handle_time, id " +
                " LIMIT ?,?";
        List<GourdEventNode> nodeList = jdbcTemplate.query(sql, new Object[]{serviceId, maxRetry, HandleStatus.SUCCEED.getCode(),
                0, size}, new GourdEventNodeRowMapper());
        if(CollectionUtils.isNotEmpty(nodeList)){
            List<GourdEvent> eventList = new ArrayList<>();
            String lockSql = " UPDATE gourd_consumer_log SET version=version+1, last_handle_time=? WHERE id=? AND version=? ";
            for(GourdEventNode node : nodeList){
                int result = jdbcTemplate.update(lockSql, new Object[]{new Date(), node.getEvent().getId(), node.getHandleNode().getVersion()});
                if(result == 1){
                    eventList.add(node.getEvent());
                }
            }

            return eventList;
        }

        return null;
    }

    private static class GourdEventNodeRowMapper implements RowMapper<GourdEventNode> {

        @Nullable
        @Override
        public GourdEventNode mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            GourdEvent event = GourdEvent.builder().id(resultSet.getString("id"))
                    .queue(resultSet.getString("queue"))
                    .payload(resultSet.getString("payload")).build();
            HandleNode handleNode = HandleNode.builder().id(resultSet.getString("id"))
                    .status(HandleStatus.get(resultSet.getInt("status")))
                    .times(resultSet.getInt("handle_count"))
                    .version(resultSet.getInt("version")).build();
            return GourdEventNode.builder().event(event).handleNode(handleNode)
                    .createTime(resultSet.getDate("create_time")).build();
        }
    }
}
