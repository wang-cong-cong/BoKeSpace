package com.itheima.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.domain.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 预约设置服务类
 * @author cong
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void add(ArrayList<OrderSetting> orderSettingList) {
        if (orderSettingList != null && orderSettingList.size() >0){
            for (OrderSetting orderSetting : orderSettingList) {
                //查询该日期是否存在
               long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
               if (count > 0 ){
                   //已经存在则更新
                   orderSettingDao.editNumberByOrderDate(orderSetting);
               }else{
                   //不存在则添加
                   orderSettingDao.add(orderSetting);
               }
            }
        }
    }

    /**
     * 根据月份动态查询预约数据信息
     * */
    @Override
    public List<Map> getOrderSettingByMonth(String date) {


        //设定该月的最后一天和第一天的字符串对象封装到map集合让持久层从map集合中获取
        String begin = date+"-1";
        String end  = date+"-31";
        Map<String,String> map = new HashMap<>();
        map.put("dateBegin",begin);
        map.put("dateEnd",end);

        //根据map集合查询
        List<OrderSetting> orderSettingByMonth = orderSettingDao.getOrderSettingByMonth(map);
        //创建list集合存储map集合
        List<Map> m = new ArrayList<>();

        //获取calender抽象类对象，用它的静态方法，用于获取当前日期的几号
        /*Calendar calendar = Calendar.getInstance();
         calendar.get();*/

        //遍历查询出的List集合，创建map集合，将orderSetting数据封装到map集合，再讲map集合存到list集合返回
        for (OrderSetting orderSetting : orderSettingByMonth) {
            Map orderSettingMap = new HashMap();
            orderSettingMap.put("date",orderSetting.getOrderDate().getDate());//获取日期（几号）
            orderSettingMap.put("number",orderSetting.getNumber());
            orderSettingMap.put("reservations",orderSetting.getReservations());
            m.add(orderSettingMap);
        }
        return m;
    }

    /**
     *根据指定日期修改可预约人数
     * */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        //先查询该日期是否已做预约
        long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if (count>0){
            //已预约则修改
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else{
            //未预约则添加
            orderSettingDao.add(orderSetting);
        }
    }
}
