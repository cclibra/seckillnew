package com.seckillnew.controller;

import com.seckillnew.controller.viewobject.UserVO;
import com.seckillnew.error.BusinessException;
import com.seckillnew.error.EmBusinessError;
import com.seckillnew.response.CommonReturnType;
import com.seckillnew.service.UserService;
import com.seckillnew.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;


@Controller("user")
@RequestMapping("/user")

public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @RequestMapping("/getopt")
    @ResponseBody
    //用户获取opt短信接口
    public CommonReturnType getOpt(@RequestParam(name = "telephone") String telephone) {
        //需要按照一定规则生成OTP验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += 10000;
        String otpCode = String.valueOf(randomInt);


        //将OPT验证码同对应用户的手机号关联，使用httpsession的方式绑定手机号与OtpCode
        httpServletRequest.getSession().setAttribute(telephone, otpCode);
        //将OPT验证码通过短信通道发送给用户，省略
        System.out.println("telephone " + telephone + "& otpCode =" + otpCode);

        return CommonReturnType.create(null);
    }

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws BusinessException {
        //调用service服务获取对应id的用户对象并返回给前端
        UserModel userModel = userService.getUserById(id);

        //若获取的对应用户信息不存在
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
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

}
