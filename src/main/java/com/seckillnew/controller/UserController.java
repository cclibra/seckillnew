package com.seckillnew.controller;

import com.seckillnew.controller.viewobject.UserVO;
import com.seckillnew.error.BusinessException;
import com.seckillnew.error.EmBusinessError;
import com.seckillnew.response.CommonReturnType;
import com.seckillnew.service.UserService;
import com.seckillnew.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller("user")
@RequestMapping("/user")

public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws BusinessException {
        //调用service服务获取对应id的用户对象并返回给前端
        UserModel userModel = userService.getUserById(id);

        //若获取的对应用户信息不存在
        if (userModel == null) {
            userModel.setEncrptPassword("1212");
            //throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

        //将核心领域模型用户对象转化为可供前端使用的viewobject
        UserVO userVO = convertFromModel(userModel);

        //返回通用对象
        return CommonReturnType.create(userVO);
    }

    private UserVO convertFromModel(UserModel userModel) {
        if (userModel == null)
            return null;
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }

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
