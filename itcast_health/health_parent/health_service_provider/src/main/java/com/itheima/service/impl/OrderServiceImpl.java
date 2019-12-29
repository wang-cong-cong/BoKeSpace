package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.domain.Member;
import com.itheima.domain.Order;
import com.itheima.domain.OrderSetting;
import com.itheima.entity.Result;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author cong
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Override
    public Result order(Map map) throws Exception {
        /*1、检查用户所选择的预约日期是否已经提前进行了预约设置，
                如果没有设置则无法进行预约*/
        //获取日期
        String orderDate = (String) map.get("orderDate");
        //调用工具类将字符串日期转换为日期类
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = orderDao.findByOrderDate(date);
        if (orderSetting == null){
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        /*2、检查用户所选择的预约日期是否已经约满，
            如果已经约满则无法预约
            步骤:获取预约人数和可预约人数进行比较
            */
        int number = orderSetting.getNumber();//可预约人数
        int reservations = orderSetting.getReservations();//已预约人数
        if (reservations >= number){
            return new Result(false,MessageConstant.ORDER_FULL);
        }

        /*3、检查当前用户是否为会员，如果是会员则直接完成预约，
            如果不是会员则自动完成注册并ea进行预约*/
        String  telephone = (String) map.get("telephone");
        Member member =  memberDao.findByTelephone(telephone);
        //判断是否是会员
        if (member != null){
            //是会员，检查是否重复预约根据套餐id会员id日期判断
            Integer memberId = member.getId();
            int i = Integer.parseInt(map.get("setmealId").toString());
            Order order = new Order(memberId,date,null,null,i);
            List<Order> list = orderDao.findByCondition(order);
            if (list != null && list.size() > 0){
                //已经预约，不能重复预约
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        }else{
            //不是会员自动添加成会员
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);//自动完成会员注册
        }

        /*4、预约成功，更新当日的已预约人数*/
        Order order = new Order();
        order.setMemberId(member.getId());//设置会员ID
        order.setOrderDate(DateUtils.parseString2Date(orderDate));//预约日期
        order.setOrderType((String) map.get("orderType"));//预约类型
        order.setOrderStatus(Order.ORDERSTATUS_NO);//到诊状态
        order.setSetmealId(Integer.parseInt((String) map.get("setmealId")));//套餐ID
        orderDao.add(order);

        orderSetting.setReservations(orderSetting.getReservations() + 1);//设置已预约人数+1
        orderSettingDao.editReservationsByOrderDate(orderSetting);

        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }
}
