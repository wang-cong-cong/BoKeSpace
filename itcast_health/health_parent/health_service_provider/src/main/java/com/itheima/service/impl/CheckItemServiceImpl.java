package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.container.page.PageHandler;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckItemDao;
import com.itheima.domain.CheckItem;
import com.itheima.entity.PageResult;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author cong
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional(rollbackFor = Exception.class)
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        //调用分页助手
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckItem> page = checkItemDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void delete(Integer id) {
        long count = checkItemDao.findCountByCheckItemId(id);
        if (count > 0){
            //当前检查项已经被引用，不能被删除
            throw new RuntimeException("该检查项已被引用，不能删除");
        }
        checkItemDao.deleteById(id);
    }
}
