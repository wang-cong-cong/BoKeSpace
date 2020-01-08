package com.itheima.dao;

import com.itheima.domain.Menu;
import com.itheima.domain.Role;

import java.util.List;
import java.util.Set;

/**
 * @author cong
 */
public interface RoleDao {

    Set<Role> findById(Integer userId);

    List<Role> findAll();

    List<Menu> findMenuByRoleId(Integer roleId);
}
