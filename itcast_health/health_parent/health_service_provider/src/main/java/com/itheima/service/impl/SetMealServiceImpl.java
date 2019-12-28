package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.SetMealDao;
import com.itheima.domain.Setmeal;
import com.itheima.entity.PageResult;
import com.itheima.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author cong
 */
@Service(interfaceClass = SetMealService.class)
@Transactional(rollbackFor = Exception.class)
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealDao setMealDao;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 添加套餐
     * */
    @Override
    public void add(Setmeal setmeal, Integer[] checkGroupIds) {
        setMealDao.add(setmeal);
        if (checkGroupIds != null && checkGroupIds.length > 0) {
            setSetMealAndCheckGroup(setmeal.getId(), checkGroupIds);
        }
        //将图片形成保存到redis中
        savePic2Redis(setmeal.getImg());
    }

    /**
     * 分页查询
     * */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> page =  setMealDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 根据id查询套餐
     * */
    @Override
    public Setmeal findById(Integer id) {
        return setMealDao.findById(id);
    }

    /**
     * 根据套餐id查询该套餐关联的检查组id
     * */
    @Override
    public List<Integer> findSetMealAndCheckGroup(Integer id) {
        return setMealDao.findSetMealAndCheckGroup(id);
    }

    /**
     * 修改套餐
     * */
    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {
        //修改套餐基本信息
        setMealDao.edit(setmeal);
        //删除套餐与检查组之前的关联关系
        setMealDao.deleteAssociation(setmeal.getId());
        setSetMealAndCheckGroup(setmeal.getId(),checkgroupIds);
    }

    /**
     * 删除套餐
     * */
    @Override
    public void delete(Integer id) {
        //删除套餐与检查组之间的关联
        setMealDao.deleteAssociation(id);
        setMealDao.delete(id);
    }

    /**
     * 查询全部套餐
     * */
    @Override
    public List<Setmeal> findAll() {
        return setMealDao.findAll();
    }

    //创建将图片名称保存到
    private void savePic2Redis(String pic) {
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,pic);
    }

    //建立套餐和检查组之间的关系
    private void setSetMealAndCheckGroup(Integer id, Integer[] checkGroupIds) {
        if (checkGroupIds != null && checkGroupIds.length > 0){
            for (Integer checkGroupId : checkGroupIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("setmeal_id",id);
                map.put("checkgroup_id",checkGroupId);
                setMealDao.setSetMealAndCheckGroup(map);
            }
        }
    }
}
