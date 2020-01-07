package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.domain.Permission;

import java.util.List;
import java.util.Set;

/**
 * @author cong
 */
public interface PermissionDao {

    Set<Permission> findById(Integer roleId);

    Page<Permission> selectByCondition(String queryString);

    void add(Permission permission);

    Permission findBy2Id(Integer id);

    void edit(Permission permission);

    void deleteById(Integer id);
}
