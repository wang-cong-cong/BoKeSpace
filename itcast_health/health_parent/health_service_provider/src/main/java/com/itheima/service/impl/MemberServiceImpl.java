package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.domain.Member;
import com.itheima.service.MemberService;
import com.itheima.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author cong
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    /**
     * 根据手机号查询会员信息
     * */
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    /**
     * 添加会员信息
     * */
    @Override
    public void add(Member member) {
        //对会员密码的加密
        if (member.getPassword() != null){
            member.setPassword(MD5Utils.md5(member.getPassword()));
        }
        memberDao.add(member);
    }

    /**
     * 根据日期查询会员人数
     * */
    @Override
    public List<Integer> findMemberCountByMonth(List<String> linkedList) {
        List<Integer> integerList = new ArrayList<>();
        for (String month : linkedList) {
           month = month+"-31";
         Integer count  =  memberDao.findMemberCountBeforeDate(month);
            integerList.add(count);
        }
        return integerList;
    }

}
