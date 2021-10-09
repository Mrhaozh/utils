package com.example.qwer.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OperationLog implements Serializable {
    private Long id;
    private Long userId;
    private String userName;
    private String description;
    private String module;
    private String content;
    private String ip;
    private Date createTime;
}
