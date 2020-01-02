package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.itheima.constant.MessageConstant;
import com.itheima.domain.Member;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @author cong
 */
@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    /**
     * 登陆
     * */
    @RequestMapping("/login.do")
    public Result login(HttpServletResponse response, @RequestBody Map map){
        //1、校验用户输入的短信验证码是否正确，如果验证码错误则登录失败
        String telephone = (String) map.get("telephone");//获取参数中的手机号
        String validateCode = (String) map.get("validateCode");//获取参数中的验证码
        String codeInRedis = jedisPool.getResource().get(telephone + MessageConstant.SEND_VALIDATECODE_SUCCESS);//获取redis中的验证码
        if (codeInRedis == null || !validateCode.equals(codeInRedis)){
            //验证码不正确，发送错误信息
            return new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }else{
            //2、如果验证码正确，则判断当前用户是否为会员，如果不是会员则自动完成会员注册
            Member member = memberService.findByTelephone(telephone);//根据手机号查询会员信息
            if (member == null){
                //不是会员，自动注册会员
                member = new Member();
                member.setPhoneNumber(telephone);
                member.setRegTime(new Date());
                memberService.add(member);
            }
            //3、登陆成功向客户端写入Cookie，内容为用户手机号
            Cookie cookie = new Cookie("login_member_telephone",telephone);
            cookie.setMaxAge(60*60*24);//设置存活时间
            cookie.setPath("/");//设置路径,cookie的作用范围
            response.addCookie(cookie);

            //4、将会员信息保存到Redis，使用手机号作为key，保存时长为30分钟
            String json = JSON.toJSON(member).toString()+telephone;
            jedisPool.getResource().setex(telephone,30*60,json);

            return new Result(true,MessageConstant.LOGIN_SUCCESS);
        }
    }
}
