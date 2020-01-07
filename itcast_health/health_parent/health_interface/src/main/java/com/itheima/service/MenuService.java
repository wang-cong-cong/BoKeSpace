package com.itheima.service;

import com.itheima.domain.Menu;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;

/**
 * @author cong
 */
public interface MenuService {

    PageResult findPage(QueryPageBean queryPageBean);

    void add(Menu menu);

    Menu findById(Integer id);

    void edit(Menu menu);

    void delete(Integer id);
}
