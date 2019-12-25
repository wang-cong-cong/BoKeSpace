package com.itheima.service;

import com.itheima.domain.CheckGroup;
import com.itheima.entity.PageResult;

import java.util.List;

/**
 * 检查组服务接口
 * @author cong
 */
public interface CheckGroupService {
    public void add(CheckGroup checkGroup, Integer[] checkGroupIds);

    PageResult findAllByPlaceholder(Integer currentPage, Integer pageSize, String queryString);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdByCheckGroupId(Integer id);

    void edit(CheckGroup checkGroup, Integer[] checkItemIds);

    void deleteById(Integer id);

    List<CheckGroup> findAll();

    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);
}
