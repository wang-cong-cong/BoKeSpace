package com.itheima.service;

import com.itheima.domain.CheckItem;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;

import java.util.List;


/**
 * @author cong
 */
public interface CheckItemService {

    public void add(CheckItem checkItem);

    PageResult pageQuery(QueryPageBean queryPageBean);

    void delete(Integer id);

    void edit(CheckItem checkItem);

    CheckItem findById(Integer id);

    List<CheckItem> findAll();

}
