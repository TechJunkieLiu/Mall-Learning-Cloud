package com.aiyangniu.demo.service.impl;

import com.aiyangniu.demo.service.TransactionService1;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 事务测试
 *
 * @author lzq
 * @date 2024/08/29
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TransactionService1Impl implements TransactionService1 {

    private final JdbcTemplate jdbcTemplate;

    /**
     * spring事务传播机制默认是REQUIRED，也就是说支持当前事务，如果当前没有事务，则新建事务，如果当前存在事务，则加入当前事务，合并成一个事务，
     * 当insertUmsRole()方法有事务且事务传播机制为REQUIRED时，会和doSave()方法的事务合并成一个事务，
     * 此时insertUmsRole()方法发生异常，spring捕获异常后，事务将会被设置全局rollback，而最外层的事务方法执行commit操作，
     * 这时由于事务状态为rollback，spring认为不应该commit提交该事务，就会回滚该事务，这就是为什么doSave()方法的事务也被回滚了
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
//    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public int insertUmsRole() {
        int i = 1 / 0;
        return jdbcTemplate.update("insert into ums_role(name, description, admin_count, create_time, status, sort) values ('事务管理员', '管理事务', 0, now(), 0, 0)");
    }
}
