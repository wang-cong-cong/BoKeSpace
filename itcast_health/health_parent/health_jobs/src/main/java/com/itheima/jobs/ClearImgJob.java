package com.itheima.jobs;

import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * 自定义job类定时进行垃圾图片的清理
 * @author cong
 */
public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    public void clearImg(){
        //根据Redis中保存的两个set集合进行差值计算，获得垃圾图片名称集合
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if (set != null) {
            for (String name : set) {
                //获得每一个垃圾图片，从七牛云上删除
                QiniuUtils.deleteFileFromQiniu(name);
                //获得每一个垃圾图片，从redis中删除
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES, name);
                System.out.println("自定义任务执行，清理垃圾图片:" + name);
            }
        }
    }
}
