package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.domain.CheckItem;
import com.itheima.entity.PageResult;


/**
 * @author cong
 */
public interface CheckItemDao {

    //添加检查项
    public void add(CheckItem checkItem);

    //检查项分页
    Page<CheckItem> selectByCondition(String queryString);

    //查询检查项是否与检查组关联
    long findCountByCheckItemId(Integer id);
    //删除检查项
    void deleteById(Integer id);
}
