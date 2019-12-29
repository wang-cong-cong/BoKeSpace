package com.itheima.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * @author cong
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 发送短信验证码，并将验证码存入redis
     * */
    @RequestMapping("/send4Order.do")
    public Result send4Order(String telephone){
        //生成四位随机验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);

        try {
            //发送短信验证码
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode.toString());

        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //将已发送的验证码存入redis中
        jedisPool.getResource().setex(telephone+RedisMessageConstant.SENDTYPE_ORDER,5*60,validateCode.toString());

        System.out.println("你发送的验证码是："+validateCode);
        //验证码发送成功
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
