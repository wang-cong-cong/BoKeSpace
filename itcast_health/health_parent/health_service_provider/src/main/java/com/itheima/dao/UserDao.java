package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.domain.User;

import java.util.List;
import java.util.Map;


/**
 * @author cong
 */
public interface UserDao {

    User findByUserName(String username);

    Page<User> selectCondition(String queryString);

    void add(User user);

    void setUserAndRole(Map<String, Integer> map);

    User findById(Integer id);

    List<Integer> findRoleIdByUserId(Integer id);

    void deleteAssociation(Integer id);

    void edit(User user);

    long selectCountByUserId(Integer id);

    void delete(Integer id);
}
