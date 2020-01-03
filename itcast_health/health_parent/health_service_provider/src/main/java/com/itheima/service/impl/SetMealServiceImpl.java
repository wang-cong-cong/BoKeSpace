package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.asm.FieldWriter;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.SetMealDao;
import com.itheima.domain.Setmeal;
import com.itheima.entity.PageResult;
import com.itheima.service.SetMealService;
import com.sun.javadoc.SeeTag;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;


import java.io.*;
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
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    private SetMealDao setMealDao;

    @Autowired
    private JedisPool jedisPool;
    @Value("${out_pages_path}")
    private String outPutPath;

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

        //添加成功后生成静态化页面
        generateMobileStaticHtml();
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

        //添加成功生成静态网页
        generateMobileStaticHtml();
    }

    /**
     * 删除套餐
     * */
    @Override
    public void delete(Integer id) {
        //删除套餐与检查组之间的关联
        setMealDao.deleteAssociation(id);
        setMealDao.delete(id);
        //删除成功后生成静态网页
        generateMobileStaticHtml();
    }

    /**
     * 查询全部套餐
     * */
    @Override
    public List<Setmeal> findAll() {
        return setMealDao.findAll();
    }

    /**
     * 获取套餐统计占比
     * */
    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setMealDao.findSetmealCount();
    }

    //创建将图片名称保存到redis中
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

    //生成静态化页面
    private void generateMobileStaticHtml() {
        //获取套餐模板数据
        List<Setmeal> setmealList = this.findAll();
        //生成套餐静态化页面
        generateMobileSetmealListHtml(setmealList);
        //生成套餐详情静态化页面(会有多个)
        generateMobileSetmealDetailHtml(setmealList);
    }

    //生成套餐静态化页面
    private void generateMobileSetmealListHtml(List<Setmeal> setmealList) {
        Map<String, Object> Map = new HashMap<>();
        Map.put("setmealList",setmealList);
        generateHtml("mobile_setmeal.ftl","m_setmeal.html",Map);
    }

    //生成套餐详情静态化页面(会有多个)
    private void generateMobileSetmealDetailHtml(List<Setmeal> setmealList) {
        for (Setmeal setmeal : setmealList) {
            Map<String,Object> map = new HashMap<>();
            map.put("setmeal",setMealDao.findById(setmeal.getId()));
            generateHtml("mobile_setmeal_detail.ftl","setmeal_detail_"+setmeal.getId()+".html",map);
        }
    }


    //生成静态页面通用类
    public void generateHtml(String templateName,String pageName,Map<String,Object> dateMap){
        //获取configuration对象
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Writer out = null;
        try {
            //获取模板对象
            Template template = configuration.getTemplate(templateName);
            //获取数据对象
            File file = new File(outPutPath + "\\" + pageName);
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            //生成文件
            template.process(dateMap,out);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (null != out){
                    out.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
