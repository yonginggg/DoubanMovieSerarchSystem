package com.example.springbootes.entity.es;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.*;
import java.util.Date;

/**
 *
 */
@Data
@Document(indexName = "blog",type = "doc",useServerConfiguration = true,createIndex = false)
public class EsBlog {
    @Id
    private Integer id;

    private String movie_id;
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String title;               //中文名
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String directors;           //导演
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String casts;               //主演
    private String pubdates;            //上映时间
    private String year;                //年份
    private String durations;;          //时长
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String genres;;             //类别
    private String rating;              //评分
    private String images;              //图片
    private String collect_count;       //观看人数
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String countries;           //国家地区
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String languages;           //语言
    private String videos;              //播放链接
    private String ratings_count;       //评分人数
    private String comments_count;      //短评人数
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String summary;             //简介
    private String reviews_1;           //短评
    private String reviews_2;
    private String reviews_3;
    private String reviews_4;
    private String reviews_5;
    private String reviews_6;
    private String reviews_7;
    private String reviews_8;
    private String reviews_9;
    private String reviews_10;
}
