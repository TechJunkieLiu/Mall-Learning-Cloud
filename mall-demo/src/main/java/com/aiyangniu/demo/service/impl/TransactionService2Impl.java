package com.aiyangniu.demo.service.impl;

import com.aiyangniu.demo.service.TransactionService1;
import com.aiyangniu.demo.service.TransactionService2;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 事务测试
 *
 * @author lzq
 * @date 2024/08/29
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TransactionService2Impl implements TransactionService2 {

    private final JdbcTemplate jdbcTemplate;
    private final TransactionService1 transactionService1;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int doSave() {
        int result = 0;
        jdbcTemplate.update("insert into ums_role(name, description, admin_count, create_time, status, sort) values ('事务管理员', '管理事务', 0, now(), 0, 0)");
        try {
            result = transactionService1.insertUmsRole();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
