package com.itheima.dao;

import com.itheima.domain.Role;
import com.itheima.domain.User;

import java.util.Set;

/**
 * @author cong
 */
public interface UserDao {

    User findByUserName(String username);
}
