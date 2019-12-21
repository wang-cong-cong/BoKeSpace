package com.itheima.service;

import com.itheima.domain.CheckItem;
import com.itheima.entity.PageResult;


/**
 * @author cong
 */
public interface CheckItemService {

    public void add(CheckItem checkItem);

    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    void delete(Integer id);
}
