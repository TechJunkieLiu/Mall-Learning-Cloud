package com.aiyangniu.admin.service.impl;

import com.aiyangniu.common.exception.ApiException;
import com.aiyangniu.entity.model.pojo.pms.PmsBrand;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 商品品牌名称缓存
 *
 * @author lzq
 * @date 2024/02/18
 */
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PmsBrandNameCache implements ApplicationRunner {

    private static Set<String> nameSet;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) {
        try {
            List<PmsBrand> list = jdbcTemplate.query("SELECT * FROM pms_brand", new Object[]{}, new BeanPropertyRowMapper<>(PmsBrand.class));
            nameSet = list.stream().map(PmsBrand::getName).collect(Collectors.toSet());
        }catch (Exception e){
            throw new ApiException("商品品牌名称初始化失败！", e);
        }
    }

    public static boolean contain(String name) {
        return nameSet.contains(name);
    }
}
