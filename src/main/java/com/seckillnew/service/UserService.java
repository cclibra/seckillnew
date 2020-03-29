package com.seckillnew.service;

import com.seckillnew.service.model.UserModel;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

@Service
//@ResponseBody
public interface UserService {
    //通过用户ID获取用户对象的方法
    UserModel getUserById(Integer id);
}
