package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.domain.Menu;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.service.MenuService;
import com.itheima.service.RoleService;
import com.itheima.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author cong
 */
@RestController
@RequestMapping("/user")
public class UserController {


    @Reference
    private UserService userService;

    @Reference
    private RoleService roleService;

    @Reference
    private MenuService menuService;

    /**
     * 获取当前用户名
     *
     * @return
     */
    @RequestMapping("/getUserName.do")
    public Result getUserName() {

        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal != null) {
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, principal.getUsername());
        } else {
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }

    }

    /**
     * 分页查询
     *
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage.do")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = userService.findPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());
        return pageResult;
    }

    /**
     * 添加用户并设立关系
     *
     * @param user
     * @param roleIds
     * @return
     */
    @RequestMapping("/add.do")
    public Result add(@RequestBody com.itheima.domain.User user, Integer[] roleIds) {
        try {
            userService.add(user, roleIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_USER_FAIL);
        }
        return new Result(true, MessageConstant.ADD_USER_SUCCESS);
    }

    /**
     * 根据id查询用户
     *
     * @param id
     * @return
     */
    @RequestMapping("/findById.do")
    public Result findById(Integer id) {
        try {
            com.itheima.domain.User user = userService.findById(id);
            return new Result(true, MessageConstant.QUERY_USER_SUCCESS, user);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_USER_FAIL);
        }
    }

    /**
     * 查询被勾选的角色
     *
     * @param id
     * @return
     */
    @RequestMapping("/findRoleIdByUserId.do")
    public Result findRoleIdByUserId(Integer id) {
        try {
            List<Integer> list = userService.findRoleIdByUserId(id);
            return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ROLE_FAIL);
        }
    }


    /**
     * 修改用户信息
     *
     * @param user
     * @param roleIds
     * @return
     */
    @RequestMapping("/edit.do")
    public Result edit(@RequestBody com.itheima.domain.User user, Integer[] roleIds) {
        try {
            userService.edit(user, roleIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_USER_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_USER_SUCCESS);
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @RequestMapping("/deleteById.do")
    public Result deleteById(Integer id) {
        try {
            userService.delete(id);
        } catch (RuntimeException r) {
            return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL + ",请先移除关联角色项");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_USER_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_USER_SUCCESS);
    }



    @RequestMapping("/getMenu.do")
    public Result getMenu(){

        //获取用户名
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();
        com.itheima.domain.User name = userService.findByUserName(username);
        //根据用户名id查询角色id
        List<Integer> roleIdByUserId = userService.findRoleIdByUserId(name.getId());

        List<Menu> resurtion = null;
        for (Integer roleId : roleIdByUserId) {
            List<Menu> menuList =  roleService.findById(roleId);
            int  level = 1;
            //查询一级菜单
            List<Menu> menuList1 = findByMenu(menuList, level);

            //递归遍历
             resurtion = resurtion(menuList1);
        }
        return new Result(true,"",resurtion);
    }

    //子菜单递归遍历
    private  List<Menu> resurtion(List<Menu> menus_p){
        //遍历父菜单列表
        for (Menu menu_p : menus_p) {
            //根据父菜单id获取子菜单列表
           List<Menu> ziMenuList =  menuService.findMenuIdByParentId(menu_p.getId());
            List<Menu> menus_p_c = ziMenuList.size() > 0 ? resurtion(ziMenuList) : ziMenuList;
            //将子菜单放入对应的父菜单
            menu_p.setChildren(menus_p_c);
        }
        return menus_p;
    }

    //查询一级菜单方法
    private List<Menu> findByMenu(List<Menu> menuList,Integer level){
        List<Menu> list = new ArrayList<>();
        for (Menu menu : menuList) {
            if (level.equals(menu.getLevel())){
                list.add(menu);
            }
        }
        return list;
    }
}