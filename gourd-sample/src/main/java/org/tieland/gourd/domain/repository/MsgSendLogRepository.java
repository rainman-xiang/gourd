package org.tieland.gourd.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tieland.gourd.domain.MsgSendLogDO;

import java.util.Date;

/**
 * @author zhouxiang
 * @date 2018/10/18 15:17
 */
public interface MsgSendLogRepository {

    @Query(" update MsgSendLogDO set status = 1 " +
            " where id = :id and sendCount = 0 ")
    @Modifying
    int updateStart(@Param("id") String id);

    @Query(" update MsgSendLogDO set sendCount = sendCount+1, lastSendTime = :lastSendTime " +
            " where id = :id and sendCount = :sendCount and status = 1 ")
    @Modifying
    int updateSend(@Param("id") String id, @Param("lastSendTime") Date sendTime, @Param("sendCount") Integer sendCount);

    @Query(" update MsgSendLogDO msl set status = :status where id = :id and status = 1 ")
    @Modifying
    int updateEnd(@Param("id") String id, @Param("status") Integer status);

    @Query(" select msl from MsgSendLogDO msl where status in (0, 1) and sendCount <= :sendCount ")
    Page<MsgSendLogDO> find(Integer sendCount, Pageable pageable);

}
