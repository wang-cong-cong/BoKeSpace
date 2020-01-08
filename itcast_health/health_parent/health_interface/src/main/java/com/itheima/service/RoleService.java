package com.itheima.service;

import com.itheima.domain.Menu;
import com.itheima.domain.Role;

import java.util.List;

/**
 * @author cong
 */
public interface RoleService {
    List<Role> findAll();

    List<Menu> findById(Integer roleId);
}
