package com.itheima.service;

import com.itheima.domain.User;

/**
 * @author cong
 */
public interface UserService {

    User findByUserName(String username);
}
