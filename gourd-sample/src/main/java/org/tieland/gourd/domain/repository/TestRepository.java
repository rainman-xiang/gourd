package org.tieland.gourd.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tieland.gourd.domain.TestDO;

/**
 * @author zhouxiang
 * @date 2018/9/28 17:10
 */
@Repository
public interface TestRepository extends JpaRepository<TestDO, Long> {
}
