package com.itheima.dao;

import com.itheima.domain.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author cong
 */
public interface OrderSettingDao {

    void add(OrderSetting orderSetting);

    void editNumberByOrderDate(OrderSetting orderSetting);

    long findCountByOrderDate(Date orderDate);

    List<OrderSetting> getOrderSettingByMonth(Map<String, String> map);

    void editReservationsByOrderDate(OrderSetting orderSetting);

    void deleteOrderByDate(Date firstDay4ThisMonth);

}
