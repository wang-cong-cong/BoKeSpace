package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import org.aspectj.weaver.Lint;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author cong
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    /**
     * 会员统计
     * */
    @RequestMapping("/getMemberReport.do")
    public Result getMemberReport(){
        Calendar calendar = Calendar.getInstance();
        //获取当前日期往后12个月日期
        calendar.add(Calendar.MONTH,-12);

        LinkedList<String> linkedList = new LinkedList<>();

        for (int i = 0; i < 12 ; i++) {
            calendar.add(Calendar.MONTH,1);
            linkedList.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));
        }

        Map<String,Object> map = new HashMap<>();
        map.put("months",linkedList);

        List<Integer> memberCount =  memberService.findMemberCountByMonth(linkedList);

        map.put("memberCount",memberCount);

        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }
}
