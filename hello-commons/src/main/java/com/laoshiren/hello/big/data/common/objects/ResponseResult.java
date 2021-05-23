package com.laoshiren.hello.big.data.common.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.laoshiren.hello.big.data.common.constant.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * ProjectName:     hello-big-data
 * Package:         com.laoshiren.hello.big.data.common.objects
 * ClassName:       ResponseResult
 * Author:          laoshiren
 * Git:             xiangdehua@pharmakeyring.com
 * Description:
 * Date:            2021/5/22 18:38
 * Version:         1.0.0
 */
@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ResponseResult<T> implements Serializable {

    private static final long serialVersionUID = 6160766580959098587L;

    private Integer code;

    private String message;

    private T data;

    public static <T> ResponseResult<T> ok(T data) {
        return new ResponseResult<>(ResultCode.BASE_SUCCESS.getCode(), ResultCode.BASE_SUCCESS.getMessage(), data);
    }

    public static <T> ResponseResult<T> fail(Integer code,String message) {
        return new ResponseResult<>(code, message,null);
    }

    public static <T> ResponseResult<T> fail(ResultCode resultCode, T data) {
        return new ResponseResult<>(resultCode.getCode(), resultCode.getMessage(),data);
    }

    public static <T> ResponseResult<T> create(ResultCode resultCode, T data) {
        return new ResponseResult<>(resultCode.getCode(), resultCode.getMessage(), data);
    }

}
