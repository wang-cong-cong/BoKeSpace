package com.itheima.controller;

import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cong
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/getUserName.do")
    public Result getUserName(){

            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal != null){
                return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,principal.getUsername());
            }else{
                return new Result(false,MessageConstant.GET_USERNAME_FAIL);
            }

    }
}
