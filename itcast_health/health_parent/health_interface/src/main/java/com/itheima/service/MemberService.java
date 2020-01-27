package com.itheima.service;

import com.itheima.domain.Member;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author cong
 */
public interface MemberService {

    Member findByTelephone(String telephone);

    void add(Member member);

    List<Integer> findMemberCountByMonth(List<String> linkedList);




}
