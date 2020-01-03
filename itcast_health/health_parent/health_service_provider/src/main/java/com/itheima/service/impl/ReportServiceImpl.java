package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.service.ReportService;
import com.itheima.utils.DateUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 运营数据统计类
 * "data":{
 *     "todayVisitsNumber":0, 今日到诊数
 *     "reportDate":"2019‐04‐25",  当前日期
 *     "todayNewMember":0,  新增会员数
 *     "thisWeekVisitsNumber":0, 本周到诊数
 *     "thisMonthNewMember":2, 本月新增会员数
 *     "thisWeekNewMember":0,  本周新增会员数
 *     "totalMember":10,  总会员数
 *     "thisMonthOrderNumber":2,   本月订单数
 *     "thisMonthVisitsNumber":0, 本月预约人数
 *     "todayOrderNumber":0,  今日预约数
 *     "thisWeekOrderNumber":0,  本周订单数
 *     "hotSetmeal":[  热门套餐
 *       {"proportion":0.4545,"name":"粉红珍爱(女)升级TM12项筛查体检套餐","setmeal_count":5},
 *       {"proportion":0.1818,"name":"阳光爸妈升级肿瘤12项筛查体检套餐","setmeal_count":2}
 *     ],
 *   },
 *   "flag":true,
 *   "message":"获取运营统计数据成功"
 *
 * @author cong
 */
@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;


    @Override
    public Map<String, Object> getBusinessReport() throws Exception {
        //获取当前日期
        String todayDate = DateUtils.parseDate2String(DateUtils.getToday());
        //获取当前日期的本周一的日期
        String thisWeekDate = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        //获取当前日期的本周日的日期
        String lastWeekDate = DateUtils.parseDate2String(DateUtils.getLastDayOfWeek(DateUtils.getToday()));
        //获取本月第一天的日期
        String firstDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
        //获取本月最后一天的日期
        String lastDay = DateUtils.parseDate2String(DateUtils.getLastDay4ThisMonth());

        //今日新增会员数
        Integer todayNewMember = memberDao.findMemberCountBeforeDate(todayDate);

        //总会员数
        Integer totalMember = memberDao.findMemberTotalCount();

        //本周新增会员数
        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(thisWeekDate);

        //本月新增会员数
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDay4ThisMonth);

        //今日预约数
        Integer todayOrderNumber = orderDao.findOrderCountByDate(todayDate);

        //今日到诊数
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(todayDate);

        //本周预约数
        Integer thisWeekOrderNumber = orderDao.findOrderCountAfterDate(thisWeekDate,lastWeekDate);

        //本周到诊数
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate2(thisWeekDate,lastWeekDate);

        //本月预约数
        Integer thisMonthOrderNumber = orderDao.findOrderCountAfterDate(firstDay4ThisMonth,lastDay);

        //本月到诊数
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDay4ThisMonth);

        //热门套餐（前四）
        List<Map> hotSetmeal = orderDao.findHotSetmeal();

        Map<String, Object> result = new HashMap<>();
        result.put("reportDate", todayDate);
        result.put("todayNewMember", todayNewMember);
        result.put("totalMember", totalMember);
        result.put("thisWeekNewMember", thisWeekNewMember);
        result.put("thisMonthNewMember", thisMonthNewMember);
        result.put("todayOrderNumber", todayOrderNumber);
        result.put("thisWeekOrderNumber", thisWeekOrderNumber);
        result.put("thisMonthOrderNumber", thisMonthOrderNumber);
        result.put("todayVisitsNumber", todayVisitsNumber);
        result.put("thisWeekVisitsNumber", thisWeekVisitsNumber);
        result.put("thisMonthVisitsNumber", thisMonthVisitsNumber);
        result.put("hotSetmeal", hotSetmeal);


        return result;
    }
}
