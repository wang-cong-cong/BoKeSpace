package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.domain.Setmeal;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.service.SetMealService;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @author cong
 */
@RestController
@RequestMapping("/setMeal")
public class SetMealController {

    @Reference
    private SetMealService setMealService;

    @Autowired
    private JedisPool jedisPool;

    /*
     * 图片上传
     * */
    @RequestMapping("/upload.do")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) {
        try {
            //获取源文件名
            String originalFilename = imgFile.getOriginalFilename();
            //获取文件名后缀，获取要截取位置的索引
            int i = originalFilename.lastIndexOf(".");
            String suffix = originalFilename.substring(i);
            //使用UUID随机产生文件名，防止相同文件名覆盖
            String filename = UUID.randomUUID().toString() + suffix;
            //上传图片成功
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), filename);
            Result result = new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, filename);
            //将图片名称存入redis集合中，基于set集合存储
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, filename);
            return result;
        } catch (IOException e) {
            //上传文件失败
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    /*
     * 套餐添加
     * */
    @RequestMapping("/add.do")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        try {
            setMealService.add(setmeal, checkgroupIds);
        } catch (Exception e) {
            //新增套餐失败
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
        //新增套餐失败
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    /**
     * 分页查询
     */
    @RequestMapping("/findPage.do")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = setMealService.findPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());
        return pageResult;
    }

    /**
     * 根据id查询套参
     */
    @RequestMapping("/findById.do")
    public Result findById(Integer id) {
        Setmeal setmeal = setMealService.findById(id);
        if (setmeal != null) {
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
        }
        return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
    }

    /**
     * 根据套餐id查询相关的检查组
     */
    @RequestMapping("/findSetMealAndCheckGroup.do")
    public Result findSetMealAndCheckGroup(Integer id) {
        try {
            List<Integer> checkGroupIds = setMealService.findSetMealAndCheckGroup(id);
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroupIds);
        } catch (Exception e) {
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    /**
     * 编辑页面
     * */
    @RequestMapping("/edit.do")
    public Result edit(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        try {
            setMealService.edit(setmeal,checkgroupIds);
            return new Result(true,MessageConstant.EDIT_SETMEAL_SUCCESS);
        } catch (Exception e) {
            return new Result(false,MessageConstant.EDIT_SETMEAL_FAIL);
        }
    }

    /**
     * 删除套餐
     * */
    @RequestMapping("/delete.do")
    public Result delete(Integer id){
        try {
            //删除之前先获取该套餐对象，用于获取图片名称
            Setmeal setmeal = setMealService.findById(id);
            setMealService.delete(id);
            //从redis中删除已删除的套餐的图片名称
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
            return new Result(true,MessageConstant.DELETE_SETMEAL_SUCCESS);
        } catch (Exception e) {
            return new Result(false,MessageConstant.DELETE_SETMEAL_FAIL);
        }
    }
}
