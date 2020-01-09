package com.itheima.service;

import com.itheima.domain.OrderSetting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author cong
 */
public interface OrderSettingService {

    void add(ArrayList<OrderSetting> orderSettingList);

    List<Map> getOrderSettingByMonth(String date);

    void editNumberByDate(OrderSetting orderSetting);

    void deleteOrderByDate(Date firstDay4ThisMonth);
}
