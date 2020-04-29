package com.example.springbootes.entity.mysql;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

//CREATE TABLE `record`  (
//        `id` int(11) NOT NULL AUTO_INCREMENT,
//        `userid` varchar(255) NOT NULL,
//        `record_order` varchar(255) NULL,
//        `movie_id` varchar(255) DEFAULT NULL,
//        `record_time` datetime NULL,
//        PRIMARY KEY (`id`)
//        );

@Data
@Table(name = "record")
@Entity
public class MysqlRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String userid;
    private Integer recordOrder;            //中文名
    private String movieId;                //导演
    private Date recordTime;               //主演
}
