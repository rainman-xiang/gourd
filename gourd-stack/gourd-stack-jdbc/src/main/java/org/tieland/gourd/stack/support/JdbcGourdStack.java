package org.tieland.gourd.stack.support;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tieland.gourd.common.support.DestinationConverterFactory;
import org.tieland.gourd.common.Gourd;
import org.tieland.gourd.stack.api.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhouxiang
 * @date 2018/10/29 14:08
 */
@Slf4j
public class JdbcGourdStack implements GourdStack {

    private final String QUERY_SQL = " SELECT * FROM gourd_producer_log WHERE id=?";

    private JdbcTemplate jdbcTemplate;

    private String serviceId;

    public JdbcGourdStack(final String serviceId){
        this.serviceId = serviceId;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void prepare(Gourd gourd) {
        String sql = " INSERT INTO gourd_producer_log(id, service_id, payload, destination, " +
                " create_time, status, send_count, last_handle_time, version) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, new Object[]{
                gourd.getId(), serviceId, gourd.getPayload(), DestinationConverterFactory.getInstance().get().from(gourd.getDestination()),
                new Date(), HandleStatus.NEW.getCode(), 0, null, 0
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelPrepare(Gourd gourd) {
        String sql = " DELETE FROM gourd_producer_log WHERE id=? ";
        jdbcTemplate.update(sql, new Object[]{gourd.getId()});
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Gourd get(String key) {
        try{
            GourdNode node = jdbcTemplate.queryForObject(QUERY_SQL, new GourdNodeRowMapper(), key);
            return node.getGourd();
        }catch (EmptyResultDataAccessException ex){
            return null;
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation= Propagation.REQUIRES_NEW)
    public void createNew(Gourd gourd) {
        Gourd existGourd = get(gourd.getId());
        if(existGourd == null){
            prepare(gourd);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void trigger(Gourd gourd) {
        GourdNode node = jdbcTemplate.queryForObject(QUERY_SQL, new GourdNodeRowMapper(), gourd.getId());
        HandleNode handleNode = node.getHandleNode();
        String sql = "UPDATE gourd_producer_log SET status=?, send_count=?, last_handle_time=?, version=version+1 " +
                " WHERE id=? AND version=? ";
        Integer result = jdbcTemplate.update(sql, new Object[]{HandleStatus.PROCESSING.getCode(), handleNode.getTimes()+1, new Date(),
                gourd.getId(), handleNode.getVersion()});
        if(result == 0){
            log.error(" not update trigger success ");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void succeed(String key) {
        GourdNode node = jdbcTemplate.queryForObject(QUERY_SQL, new GourdNodeRowMapper(), key);
        HandleNode handleNode = node.getHandleNode();
        if(handleNode.getStatus() != HandleStatus.PROCESSING
                && handleNode.getStatus() != HandleStatus.RETRYING){
            log.error(" GourdNode is ready failed or success, can not update. handleNode:{}", handleNode);
            return;
        }

        String sql = "UPDATE gourd_producer_log SET status=?, version=version+1 WHERE id=? AND version=? ";
        Integer result = jdbcTemplate.update(sql, new Object[]{HandleStatus.SUCCEED.getCode(), key, handleNode.getVersion()});
        if(result == 0){
            log.error(" not update succeed success ");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void failed(String key) {
        GourdNode node = jdbcTemplate.queryForObject(QUERY_SQL, new GourdNodeRowMapper(), key);
        HandleNode handleNode = node.getHandleNode();
        if(handleNode.getStatus() != HandleStatus.PROCESSING
                && handleNode.getStatus() != HandleStatus.RETRYING){
            log.error(" GourdNode is ready failed or success, can not update. handleNode:{}", handleNode);
            return;
        }

        String sql = "UPDATE gourd_producer_log SET status=?, version=version+1 WHERE id=? AND version=? ";
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
        GourdNode node = jdbcTemplate.queryForObject(QUERY_SQL, new GourdNodeRowMapper(), key);
        HandleNode handleNode = node.getHandleNode();
        if(handleNode == null){
            log.error(" getHandleNode error, gourd key:{} ", key);
            throw new StackException(" GourdNode is not exist ");
        }

        return handleNode;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Integer getRetryCount(Integer maxRetry) {
        String sql = "SELECT count(1) FROM gourd_producer_log WHERE " +
                "  service_id=? AND send_count<? AND status<>? ";
        return jdbcTemplate.queryForObject(sql, new Object[]{serviceId, maxRetry, HandleStatus.SUCCEED.getCode()}, Integer.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Gourd> getRetryList(Integer maxRetry, Integer maxSize) {
        String sql = "SELECT * FROM gourd_producer_log WHERE " +
                " service_id=? AND send_count<? AND status<>? order by last_handle_time, id " +
                " LIMIT ?,?";
        List<GourdNode> nodeList = jdbcTemplate.query(sql, new Object[]{serviceId, maxRetry, HandleStatus.SUCCEED.getCode(),
                0, maxSize}, new GourdNodeRowMapper());
        if(CollectionUtils.isNotEmpty(nodeList)){
            List<Gourd> gourdList = new ArrayList<>();
            String lockSql = " UPDATE gourd_producer_log SET version=version+1, last_handle_time=? WHERE id=? AND version=? ";
            for(GourdNode node : nodeList){
                int result = jdbcTemplate.update(lockSql, new Object[]{new Date(), node.getGourd().getId(), node.getHandleNode().getVersion()});
                if(result == 1){
                    gourdList.add(node.getGourd());
                }
            }

            return gourdList;
        }

        return null;
    }

    private static class GourdNodeRowMapper implements RowMapper<GourdNode> {

        @Nullable
        @Override
        public GourdNode mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            HandleNode handleNode = HandleNode.builder().status(HandleStatus.get(resultSet.getInt("status")))
                    .id(resultSet.getString("id"))
                    .times(resultSet.getInt("send_count"))
                    .version(resultSet.getInt("version")).build();
            Gourd gourd = Gourd.recover().id(resultSet.getString("id"))
                    .destination(DestinationConverterFactory.getInstance().get().to(resultSet.getString("destination")))
                    .payload(resultSet.getString("payload")).build();
            return GourdNode.builder().gourd(gourd).handleNode(handleNode).createTime(resultSet.getDate("create_time")).build();
        }
    }
}
