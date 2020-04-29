package com.example.springbootes.entity.mysql;


import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "user")
@Entity
public class MysqlUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String userid;               //用户ID
    private String userpwd;              //密码
}
