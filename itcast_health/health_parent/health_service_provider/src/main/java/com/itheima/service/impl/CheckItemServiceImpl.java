package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckItemDao;
import com.itheima.dao.SetMealDao;
import com.itheima.domain.CheckItem;
import com.itheima.domain.Setmeal;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.service.CheckItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author cong
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional(rollbackFor = Exception.class)
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;

    @Autowired
    private SetMealDao setMealDao;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Value("${out_pages_path}")
    private String outPutPath;

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        //调用分页助手
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<CheckItem> page = checkItemDao.selectByCondition(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void delete(Integer id)throws RuntimeException {
        long count = checkItemDao.findCountByCheckItemId(id);
        //判断检查项是否已关联检查组
        if (count > 0){
            //当前检查项已经被引用，不能被删除
            throw  new RuntimeException("项目已引用，无法删除");
        }
        checkItemDao.deleteById(id);
    }

    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
        //判断是否有关联的检查组
        long count = checkItemDao.findCountByCheckItemId(checkItem.getId());
        if (count > 0 ){
            //有关联需重新生成静态页面
            generateMobileStaticHtml();
        }
    }

    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll() ;
    }


    //生成静态化页面
    private void generateMobileStaticHtml() {
        //获取套餐模板数据
        List<Setmeal> setmealList = setMealDao.findAll();
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
