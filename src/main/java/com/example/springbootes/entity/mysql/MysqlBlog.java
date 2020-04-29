package com.example.springbootes.entity.mysql;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * CREATE TABLE `t_blog` (
 *   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
 *   `title` varchar(60) DEFAULT NULL COMMENT '博客标题',
 *   `author` varchar(60) DEFAULT NULL COMMENT '博客作者',
 *   `content` mediumtext COMMENT '博客内容',
 *   `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
 *   `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
 *   PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB AUTO_INCREMENT=112 DEFAULT CHARSET=utf8mb4
 */

//@Data
//@Table(name = "t_blog")
//@Entity
//public class MysqlBlog {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//    private String title;
//    private String author;
//    @Column(columnDefinition = "mediumtext")
//    private String content;
//    private Date createTime;
//    private Date updateTime;
//}

@Data
@Table(name = "movies")
@Entity

public class MysqlBlog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String movieId;
    private String title;               //中文名
    private String directors;           //导演
    private String casts;               //主演
    private String pubdates;            //上映时间
    private String year;                //年份
    private String durations;;          //时长
    private String genres;;             //类别
    private String rating;              //评分
    private String images;              //图片
    private String collectCount;       //观看人数
    private String countries;           //国家地区
    private String languages;           //语言
    private String videos;              //播放链接
    private String ratingsCount;       //评分人数
    private String commentsCount;      //短评人数
    @Column(columnDefinition = "longtext")
    private String summary;             //简介
    @Column(columnDefinition = "longtext")
    private String reviews_1;           //短评
    @Column(columnDefinition = "longtext")
    private String reviews_2;
    @Column(columnDefinition = "longtext")
    private String reviews_3;
    @Column(columnDefinition = "longtext")
    private String reviews_4;
    @Column(columnDefinition = "longtext")
    private String reviews_5;
    @Column(columnDefinition = "longtext")
    private String reviews_6;
    @Column(columnDefinition = "longtext")
    private String reviews_7;
    @Column(columnDefinition = "longtext")
    private String reviews_8;
    @Column(columnDefinition = "longtext")
    private String reviews_9;
    @Column(columnDefinition = "longtext")
    private String reviews_10;
}
