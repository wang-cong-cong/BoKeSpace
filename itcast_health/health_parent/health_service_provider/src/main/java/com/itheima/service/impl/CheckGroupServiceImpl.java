package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.domain.CheckGroup;
import com.itheima.domain.Setmeal;
import com.itheima.entity.PageResult;
import com.itheima.service.CheckGroupService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author cong
 */
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;


    /**
     * 添加检查组，并且和检查项建立中间表关系
     **/
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkItemIds) {
        checkGroupDao.add(checkGroup);
        setCheckGroupAndCheckItem(checkGroup.getId(), checkItemIds);
    }

    /**
     * 查询检查组
     * */
    @Override
    public PageResult findAllByPlaceholder(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckGroup> page = checkGroupDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 根据id查询检查组
     * */
    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    /**
     * 根据检查组id查询检查项的id
     * */
    @Override
    public List<Integer> findCheckItemIdByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdByCheckGroupId(id);
    }

    /**
     * 修改检查组信息
     * */
    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkItemIds) {
        //更新检查组的基本信息
        checkGroupDao.edit(checkGroup);
        //修改检查组数据需先删除中间表数据
        checkGroupDao.deleteAssociation(checkGroup.getId());
        //向中间表插入和更新数据(建立检查组和检查项的关系)
        setCheckGroupAndCheckItem(checkGroup.getId(),checkItemIds);

    }

    /**
     * 删除检查组
     * */
    @Override
    public void deleteById(Integer id)throws RuntimeException {
      long count = checkGroupDao.findCountByCheckItemId(id);
      if (count > 0 ){
          throw new RuntimeException("当前检查组被引用，不能删除");
      }
        checkGroupDao.deleteById(id);
    }

    /**
     * 查询全部检查组
     * */
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    /**
     * 分页查询
     * */
    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckGroup> page = checkGroupDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    //创建建立检查组和检查项之间关系的方法
    public void setCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkItemIds) {
        if (checkItemIds != null && checkItemIds.length > 0) {
            for (Integer checkItemId : checkItemIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkgroup_id", checkGroupId);
                map.put("checkitem_id", checkItemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }
}
