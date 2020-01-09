package com.itheima.jobs;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.DateUtils;


import java.util.Date;

/**
 * 定时清理已过期的订单
 * @author cong
 */
public class ClearOrderJob {

    @Reference
    private OrderSettingService orderSettingService;

    public void clearOrder(){
        //获取本月一日的日期
        Date firstDay4ThisMonth = DateUtils.getFirstDay4ThisMonth();
        //根据日期删除之前的订单
         orderSettingService.deleteOrderByDate(firstDay4ThisMonth);
    }
}
