package com.itheima.dao;

import com.itheima.domain.Member;

/**
 * @author cong
 */
public interface MemberDao {

    Member findByTelephone(String telephone);

    void add(Member member);

    Integer findMemberCountBeforeDate(String month);

    Integer findMemberTotalCount();

    Integer findMemberCountAfterDate(String thisWeekDate);
}
