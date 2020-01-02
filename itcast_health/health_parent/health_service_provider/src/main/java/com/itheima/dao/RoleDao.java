package com.itheima.dao;

import com.itheima.domain.Role;

import java.util.Set;

/**
 * @author cong
 */
public interface RoleDao {

    Set<Role> findById(Integer userId);
}
