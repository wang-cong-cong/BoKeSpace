package com.itheima.service;

import com.itheima.domain.Setmeal;
import com.itheima.entity.PageResult;

import java.util.List;


/**
 * @author cong
 */
public interface SetMealService {
    void add(Setmeal setmeal, Integer[] checkGroupIds);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    Setmeal findById(Integer id);

    List<Integer> findSetMealAndCheckGroup(Integer id);

    void edit(Setmeal setmeal, Integer[] checkgroupIds);

    void delete(Integer id);

    List<Setmeal> findAll();
}
