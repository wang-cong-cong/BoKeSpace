package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.MenuDao;
import com.itheima.domain.Menu;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author cong
 */
@Service(interfaceClass = MenuService.class)
@Transactional
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<Menu> page = menuDao.findByCondition(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void add(Menu menu) {
        menuDao.add(menu);
    }

    @Override
    public Menu findById(Integer id) {
        return menuDao.findById(id);
    }

    @Override
    public void edit(Menu menu) {
        menuDao.edit(menu);
    }

    @Override
    public void delete(Integer id) {
        menuDao.delete(id);
    }

    @Override
    public List<Menu> findMenuIdByParentId(Integer parentMenuId) {
        return menuDao.findMenuIdByParentId(parentMenuId);
    }

}
