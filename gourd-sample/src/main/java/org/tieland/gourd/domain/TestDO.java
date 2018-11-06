package org.tieland.gourd.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author zhouxiang
 * @date 2018/9/28 17:05
 */
@Entity
@Table(name = "demo")
@NoArgsConstructor
@Data
public class TestDO implements Serializable{
    private static final long serialVersionUID = 2891058007996344405L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;
}
