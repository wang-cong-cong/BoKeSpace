package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.domain.Menu;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.service.MenuService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cong
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Reference
    private MenuService menuService;

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage.do")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = menuService.findPage(queryPageBean);
        return pageResult;
    }

    /**
     * 添加菜单
     * @param menu
     * @return
     */
    @RequestMapping("/add.do")
    public Result add(@RequestBody Menu menu){
        try {
            menuService.add(menu);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MENU_FAIL);
        }
        return new Result(true,MessageConstant.GET_MENU_SUCCESS);
    }

    /**
     * 根据id回显数据
     * @param id
     * @return
     */
    @RequestMapping("/findById.do")
    public Result findById(Integer id){
        try {
            Menu menu = menuService.findById(id);
            return new Result(true,MessageConstant.GET_MENU_SUCCESS,menu);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_MENU_FAIL);
        }
    }

    /**
     * 修改菜单
     * @param menu
     * @return
     */
    @RequestMapping("/edit.do")
    public Result edit(@RequestBody Menu menu){
        try {
            menuService.edit(menu);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_MENU_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_MENU_SUCCESS);
    }

    @RequestMapping("/delete.do")
    public Result delete(Integer id){
        try {
            menuService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_MENU_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_MENU_SUCCESS);
    }
}
