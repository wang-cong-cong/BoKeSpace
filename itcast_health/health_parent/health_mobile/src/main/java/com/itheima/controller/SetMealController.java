package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.domain.Setmeal;
import com.itheima.entity.Result;
import com.itheima.service.SetMealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author cong
 */
@RestController
@RequestMapping("/setMeal")
public class SetMealController {

    @Reference
    private SetMealService setMealService;

    /**
     * 查询套餐列表
     * */
    @RequestMapping("/getSetMeal.do")
    public Result getSetMeal(){
        try {
            List<Setmeal> setMealList =  setMealService.findAll();
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,setMealList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEALLIST_FAIL);
        }
    }

    /**
     * 根据套餐id获取检查组和检查项
     * */
    @RequestMapping("/findById.do")
    public Result findById(int id){
        try {
            Setmeal setmeal = setMealService.findById(id);
            System.out.println(setmeal.getCheckGroups());
            return new Result(true,MessageConstant.QUERY_SETMEALLIST_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEALLIST_FAIL);
        }
    }
}
