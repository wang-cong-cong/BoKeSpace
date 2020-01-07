package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.domain.Permission;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.service.PermissionService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author cong
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Reference
    private PermissionService permissionService;

    /***
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage.do")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
       PageResult pageResult =  permissionService.pageQuery(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
       return pageResult;
    }

    /**
     * 添加权限
     * @param permission
     * @return
     */
    @RequestMapping("add.do")
    public Result add(@RequestBody Permission permission){
        try {
            permissionService.add(permission);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_PERMISSION_FAIL);
        }
        return new Result(true, MessageConstant.ADD_PERMISSION_SUCCESS);
    }

    /**
     * 根据id查询权限
     * @param id
     * @return
     */
    @RequestMapping("/findById.do")
    public Result findById(Integer id){
        try {
            Permission permission =  permissionService.findById(id);
            return new Result(true,MessageConstant.QUERY_PERMISSION_SUCCESS,permission);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_PERMISSION_FAIL);
        }
    }

    /**
     * 修改权限
     * @param permission
     * @return
     */
    @RequestMapping("/edit.do")
    public Result edit(@RequestBody Permission permission){
        try {
            permissionService.edit(permission);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_PERMISSION_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_PERMISSION_SUCCESS);
    }

    @RequestMapping("/deleteById.do")
    public Result deleteById(Integer id){
        try {
            permissionService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_PERMISSION_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_PERMISSION_SUCCESS);
    }

}
