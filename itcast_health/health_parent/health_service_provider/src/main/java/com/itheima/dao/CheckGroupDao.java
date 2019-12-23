package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.domain.CheckGroup;

import java.util.List;
import java.util.Map;

/**
 * @author cong
 */
public interface CheckGroupDao {

    public void add(CheckGroup checkGroup);

    public void setCheckGroupAndCheckItem(Map map);

    Page<CheckGroup> selectByCondition(String queryString);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdByCheckGroupId(Integer id);

    void deleteAssociation(Integer id);

    void edit(CheckGroup checkGroup);

    void delete(Integer id);
}
