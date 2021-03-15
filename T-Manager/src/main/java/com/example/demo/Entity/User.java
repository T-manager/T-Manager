package com.example.demo.Entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.io.Serializable;
import java.io.Serializable;

/**
 * 用户实体
 *
 * [x] length, [x] index
 */
@Entity
@Data
@Builder
@AllArgsConstructor

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键 */
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 用户名 */
    @Column(length = 20, unique = true)
    private String username;

    /** 密码 */
    @JsonIgnore
    @Column(length = 255)
    private String password;

    // 用户头像
    @Column(length = 20)
    private String avatar;

}