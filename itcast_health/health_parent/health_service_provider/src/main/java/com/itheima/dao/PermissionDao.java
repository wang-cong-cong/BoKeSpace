package com.itheima.dao;

import com.itheima.domain.Permission;

import java.util.Set;

/**
 * @author cong
 */
public interface PermissionDao {

    Set<Permission> findById(Integer roleId);
}
