package com.itheima.dao;

import com.itheima.domain.Order;
import com.itheima.domain.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author cong
 */
public interface OrderDao {

    OrderSetting findByOrderDate(Date date);

    List<Order> findByCondition(Order order);

    void add(Order order);

    Map findById4Detail(Integer id);
}
