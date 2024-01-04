package com.aiyangniu.demo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description：
 * @author：Mr.Liu
 * @create: 2024-01-03
 */
@Data
@ApiModel(value = "test_user")
public class TestUser implements Serializable {
    private static final long serialVersionUID = -4317225431416721150L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private Boolean sex;
    private String address;
    private Date createTime;
}
