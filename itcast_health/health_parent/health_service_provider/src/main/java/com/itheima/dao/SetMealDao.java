package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.domain.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @author cong
 */
public interface SetMealDao {

    void add(Setmeal setmeal);

    void setSetMealAndCheckGroup(Map<String, Integer> map);

    Page<Setmeal> selectByCondition(String queryString);

    Setmeal findById(Integer id);

    List<Integer> findSetMealAndCheckGroup(Integer id);

    void edit(Setmeal setmeal);

    void deleteAssociation(Integer id);

    void delete(Integer id);

    List<Setmeal> findAll();

    List<Map<String, Object>> findSetmealCount();

}
