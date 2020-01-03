package com.itheima.dao;

import com.itheima.domain.Order;
import com.itheima.domain.OrderSetting;
import org.apache.ibatis.annotations.Param;

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

    Integer findOrderCountByDate(String todayDate);

    Integer findVisitsCountByDate(String todayDate);

    Integer findOrderCountAfterDate(@Param("startWeek") String thisWeekDate,@Param("endWeek")String lastWeekDate);

    Integer findVisitsCountAfterDate(String thisWeekDate);

    List<Map> findHotSetmeal();

    Integer findVisitsCountAfterDate2(@Param("startWeek") String thisWeekDate,@Param("endWeek")String lastWeekDate);
}
