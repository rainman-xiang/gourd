package org.tieland.gourd.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author zhouxiang
 * @date 2018/10/18 15:09
 */
//@Entity
//@Table(name = "msg_send_log")
@NoArgsConstructor
@Data
public class MsgSendLogDO implements Serializable {

    @Id
    @Column(name="id")
    private String id;

    @Column(name="category")
    private String category;

    @Column(name="payload")
    private String payload;

    @Column(name="destination")
    private String destination;

    @Column(name="status")
    private Integer status;

    @Column(name="sendCount")
    private Integer sendCount;

    @Column(name="createTime")
    private Date createTime;

    @Column(name="last_send_time")
    private Date lastSendTime;

}
