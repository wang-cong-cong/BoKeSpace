package com.itheima.service;

import com.itheima.domain.Permission;
import com.itheima.entity.PageResult;

import java.util.List;

/**
 * @author cong
 */
public interface PermissionService {

    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    void add(Permission permission);

    Permission findById(Integer id);

    void edit(Permission permission);

    void deleteById(Integer id);
}
