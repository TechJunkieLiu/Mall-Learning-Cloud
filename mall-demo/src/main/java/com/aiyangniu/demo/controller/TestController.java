package com.aiyangniu.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.aiyangniu.common.utils.DateUtil;
import com.aiyangniu.demo.dto.TestUser;
import com.aiyangniu.demo.mapper.TestMapper;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @description：
 * @author：Mr.Liu
 * @create: 2024-01-03
 */
@RestController
@RequestMapping("/mysql")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TestController {

    @Getter
    @AllArgsConstructor
    enum ResponseEnum{
        SUCCESS(200, "操作成功"),
        FAIL(201,"获取数据失败"),
        ERROR(400,"错误请求"),
        NOT_FOUND(404,"页面未找到"),
        SYS_ERROR(-1,"系统异常"),
        MSG_ERROR(409,"信息已存在");

        private final Integer code;
        private final String resultMessage;

        public static ResponseEnum getResultCode(Integer code){
            for (ResponseEnum value : ResponseEnum.values()) {
                if (code.equals(value.getCode())){
                    return value;
                }
            }
            return ResponseEnum.ERROR;
        }
    }

    @Data
    public static class Result<T> implements Serializable{
        private static final long serialVersionUID = 56665257248936049L;
        private Integer code;
        private String message;
        private T data;
        private Result(){}

        /**
         * 操作成功方法
         */
         public static <T> Result<T> ok(T data) {
            Result<T> response = new Result<>();
            response.setCode(ResponseEnum.SUCCESS.getCode());
            response.setMessage(ResponseEnum.SUCCESS.getResultMessage());
            response.setData(data);
            return response;
        }

        /**
         * 编译失败方法
         */
        public static <T> Result<T> buildFailure(Integer errCode, String errMessage){
            Result<T> response = new Result<>();
            response.setCode(errCode);
            response.setMessage(errMessage);
            return response;
        }
    }

    private final TestMapper testMapper;

    @PostMapping("/addUserTo")
    public Result<Object> addUser(HttpServletRequest request) {
        JSONObject object = new JSONObject();
        TestUser user = new TestUser();
        user.setName(request.getParameter("name").trim());
        user.setSex(Boolean.valueOf(request.getParameter("sex").trim()));
        user.setAddress(request.getParameter("address").trim());
        user.setCreateTime(new Date());
        testMapper.insert(user);
        object.put("code", 1);
        object.put("success", true);
        object.put("msg", "添加成功");
        object.put("type", "success");
        return Result.ok(object);
    }

    @DeleteMapping("/deleteUserTo")
    public Result<Boolean> deleteUserTo(HttpServletRequest request) {
        return Result.ok(testMapper.deleteById(request.getParameter("id")) == 1);
    }

    @PostMapping("/updateUserTo")
    public Result<Object> updateUserTo(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String id = request.getParameter("id").trim();
        String name = request.getParameter("name").trim();
        String sex = request.getParameter("sex").trim();
        String address = request.getParameter("address").trim();
        String createTime = request.getParameter("createTime").trim();
        TestUser testUser = new TestUser();
        testUser.setId(Integer.parseInt(id));
        testUser.setName(name);
        testUser.setSex(Boolean.valueOf(sex));
        testUser.setAddress(address);
        testUser.setCreateTime(StrUtil.isEmpty(createTime) ? new Date() : DateUtil.str2Date(createTime));
        testMapper.updateById(testUser);
        jsonObject.put("code", 1);
        jsonObject.put("msg", "修改成功！");
        return Result.ok(jsonObject);
    }

    @GetMapping("/getAllTo")
    public Result<List<TestUser>> allUser() {
        return Result.ok(testMapper.selectList(new QueryWrapper<>()));
    }

    @GetMapping("/getById")
    public Result<TestUser> getById(HttpServletRequest request) {
        return Result.ok(testMapper.selectById(request.getParameter("id")));
    }
}
