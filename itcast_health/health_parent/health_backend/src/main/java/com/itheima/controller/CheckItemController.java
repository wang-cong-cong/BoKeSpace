package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.domain.CheckItem;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author cong
 */
@RestController
@RequestMapping("/checkItem")
public class CheckItemController {

    @Reference
    private CheckItemService checkItemService;

    /**
     * 添加检查项
     */
    @PreAuthorize("hasAnyAuthority('CHECKITEM_ADD')")//权限校验，表示执行此功能必须有检查项添加权限
    @RequestMapping("/add.do")
    public Result add(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.add(checkItem);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    /**
     * 检查项分页
     */
    @PreAuthorize("hasAnyAuthority('CHECKITEM_QUERY')")//权限校验，表示执行此功能必须有检查项查询权限
    @RequestMapping("/findPage.do")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = checkItemService.pageQuery(queryPageBean);
        return pageResult;
    }

    /**
     * 删除检查项
     */
    @PreAuthorize("hasAnyAuthority('CHECKITEM_DELETE')")//权限校验，表示执行此功能必须有检查项删除权限
    @RequestMapping("/delete.do")
    public Result delete(Integer id) {
        try {
            checkItemService.delete(id);
        } catch (RuntimeException e) {
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    /**
     * 编辑检查项
     */
    @PreAuthorize("hasAnyAuthority('CHECKITEM_EDIT')")//权限校验，表示执行此功能必须有检查项修改权限
    @RequestMapping("/edit.do")
    public Result edit(@RequestBody CheckItem checkItem) {
        try {
            //编辑成功
            checkItemService.edit(checkItem);
        } catch (Exception e) {
            //编辑失败，返回错误信息
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        //返回成功信息
        return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    /**
     * 编辑页面回显数据
     */
    @RequestMapping("/findById.do")
    public Result findById(Integer id) {
        try {
            //回显数据成功
            CheckItem checkItem = checkItemService.findById(id);
            //返回成功数据
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItem);
        } catch (Exception e) {
            //失败返回错误信息
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    /**
     * 查询全部检查项
     *
     * @return
     */
    @RequestMapping("/findAll.do")
    public Result findAll() {
        List<CheckItem> list = checkItemService.findAll();
        if (list != null && list.size() != 0) {
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, list);
        }
        return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
    }

}
