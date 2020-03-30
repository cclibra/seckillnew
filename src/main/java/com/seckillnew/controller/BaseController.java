package com.seckillnew.controller;

import com.seckillnew.error.BusinessException;
import com.seckillnew.error.EmBusinessError;
import com.seckillnew.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class BaseController {
    //定义exceptionhandler解决未被controller层吸收的exception
    @ExceptionHandler(Exception.class)//当收到exception根类时进入handler处理,
    @ResponseStatus(HttpStatus.OK)//业务逻辑可处理的错误，所以返回前端可处理，替换掉500错误
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception ex) {
        Map<String, Object> repondeData = new HashMap<>();
        if (ex instanceof BusinessException) {
            BusinessException businessException = (BusinessException) ex;
            repondeData.put("errCode", businessException.getErrorCode());
            repondeData.put("errMsg", businessException.getErrMsg());

        } else {
            repondeData.put("errCode", EmBusinessError.UNKNOE_ERROR.getErrorCode());
            repondeData.put("errMsg", EmBusinessError.UNKNOE_ERROR.getErrMsg());

        }
        return CommonReturnType.create(repondeData, "fail");
    }

}
