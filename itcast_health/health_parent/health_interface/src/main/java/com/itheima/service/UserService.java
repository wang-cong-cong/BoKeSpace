package com.itheima.service;

import com.itheima.domain.User;
import com.itheima.entity.PageResult;

import java.util.List;

/**
 * @author cong
 */
public interface UserService {

    User findByUserName(String username);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    void add(User user, Integer[] roleIds);

    User findById(Integer id);

    List<Integer> findRoleIdByUserId(Integer id);

    void edit(User user, Integer[] roleIds);

    void delete(Integer id);
}
