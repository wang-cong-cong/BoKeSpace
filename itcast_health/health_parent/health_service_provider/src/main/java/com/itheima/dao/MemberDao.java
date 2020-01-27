package com.itheima.dao;

import com.itheima.domain.Member;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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
