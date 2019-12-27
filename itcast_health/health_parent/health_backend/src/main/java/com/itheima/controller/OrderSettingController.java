package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.domain.OrderSetting;
import com.itheima.entity.Result;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.POIUtils;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预约设置
 * @author cong
 */
@RestController
@RequestMapping("/orderSetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 通过POI读取Excel表格中信息存入数据库
     * */
    @RequestMapping("/upload.do")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile){
        try {
            //读取Excel数据
            List<String[]> list = POIUtils.readExcel(excelFile);
           if (list != null && list.size() > 0){
               //建立集合将每行数据封装成orderSetting对象
               ArrayList<OrderSetting> orderSettingList = new ArrayList<>();
               for (String[] strings : list) {
                   OrderSetting orderSetting = new OrderSetting(new Date(strings[0]), Integer.parseInt(strings[1]));
                   orderSettingList.add(orderSetting);
               }
               //新增
            orderSettingService.add(orderSettingList);
           }
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }


    /**
     * 根据月份查询预约信息
     * */
    @RequestMapping("/getOrderSettingByMonth.do")
    public Result getOrderSettingByMonth(String date){
        try {
            //获取数据成功
            List<Map> mapList = orderSettingService.getOrderSettingByMonth(date);
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,mapList);
        } catch (Exception e) {
            e.printStackTrace();
            //获取数据失败
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    /**
     *根据指定日期修改可预约人数
     * */
    @RequestMapping("/editNumberByDate.do")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.editNumberByDate(orderSetting);
            //预约设置成功
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            //预约设置失败
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
