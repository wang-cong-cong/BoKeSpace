package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.domain.Menu;

/**
 * @author cong
 */
public interface MenuDao {

    Page<Menu> findByCondition(String queryString);

    void add(Menu menu);

    Menu findById(Integer id);

    void edit(Menu menu);

    void delete(Integer id);
}
