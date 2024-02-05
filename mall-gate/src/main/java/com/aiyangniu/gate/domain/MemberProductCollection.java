package com.aiyangniu.gate.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 会员商品收藏
 *
 * @Document
 *      类似于hibernate的entity注解，标明由mongo来维护该表
 * @Id
 *      主键，不可重复，自带索引，可以在定义的列名上标注，需要自己生成并维护不重复的约束
 * @Indexed
 *      声明该字段需要加索引，加索引后以该字段为条件检索将大大提高速度
 *      唯一索引 @Indexed(unique = true)
 *      如果被索引的列是数组，MongoDB会索引这个数组中的每一个元素
 *      也可以对整个Document进行索引，排序是预定义的按插入BSON数据的先后升序排列
 *      也可以对关联的对象的字段进行索引（注解怎么写还不清楚，待查）
 *
 * @author lzq
 * @date 2023/09/20
 */
@Getter
@Setter
@Document
public class MemberProductCollection {

    @Id
    private String id;
    @Indexed
    private Long memberId;
    private String memberNickname;
    private String memberIcon;
    @Indexed
    private Long productId;
    private String productName;
    private String productPic;
    private String productSubTitle;
    private String productPrice;
    private Date createTime;
}
