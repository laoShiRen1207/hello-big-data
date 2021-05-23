package com.laoshiren.hello.big.data.common.constant;


/**
 * ProjectName:     hello-big-data
 * Package:         com.laoshiren.hello.big.data.common.constant
 * ClassName:       ResultCode
 * Author:          laoshiren
 * Git:             xiangdehua@pharmakeyring.com
 * Description:     TBD
 * Date:            2021/5/23 21:06
 * Version:         1.0.0
 */
public enum ResultCode {

    BASE_SUCCESS(20000, "服务调用成功"),
    BASE_RESOURCE_NOT_FOUND(40400, "资源未找到"),
    BASE_FAILED(50000, "服务调用失败");

    private Integer code;

    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
