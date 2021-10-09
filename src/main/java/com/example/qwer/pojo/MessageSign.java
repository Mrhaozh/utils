package com.example.qwer.pojo;


public enum MessageSign {

    REGIST_SIGN("SMS_160590377","注册"),
    LOGIN_SIGN("SMS_160590379","登录"),
    RESET_SIGN("SMS_160590376","重置密码");


    private String value;//值
    private String desc;//描述
    /**
     * @param value
     */
    private MessageSign(String value, String desc){
        this.value=value;
        this.desc=desc;
    }
    /**
     * @return
     */
    public String getValue(){
        return value;
    }
    public String getDesc(){
        return desc;
    }
}
