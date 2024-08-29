package com.aiyangniu.demo.controller;

import com.aiyangniu.demo.service.TransactionService2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 事务测试
 *
 * @author lzq
 * @date 2024/08/29
 */
@Slf4j
@Api(value = "TransactionController", tags = "事务测试")
@RestController
@RequestMapping("/trans")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TransactionController {

    private final TransactionService2 transactionService2;

    @ApiOperation("事务测试")
    @GetMapping(value = "/test")
    public int test() {
        return transactionService2.doSave();
    }
}
