package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.domain.CheckGroup;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.service.CheckGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author cong
 */
@RestController
@RequestMapping("/checkGroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    /**
     * 添加检查组
     */
    @RequestMapping("/add.do")
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkItemIds) {
        try {
            checkGroupService.add(checkGroup, checkItemIds);
        } catch (Exception e) {
            //新增失败
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        //新增成功
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    /**
     * 查询检查组
     */
    @RequestMapping("/findAll.do")
    public PageResult findAll(@RequestBody QueryPageBean queryPageBean) {
        return checkGroupService.findAll(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());
    }

    /**
     * 根据ID查询检查组
     */
    @RequestMapping("/findById.do")
    public Result findById(Integer id) {
        CheckGroup checkGroup = checkGroupService.findById(id);
        if (checkGroup != null) {
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroup);
        }
        return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);

    }

    /**
     * 根据检查组id查询已勾选的检查项的id
     */
    @RequestMapping("/findCheckItemIdByCheckGroupId.do")
    public Result findCheckItemIdByCheckGroupId(Integer id) {
        try {
            List<Integer> checkItemIds = checkGroupService.findCheckItemIdByCheckGroupId(id);
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemIds);
        } catch (Exception e) {
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    /**
     * 修改检查项
     * */
    @RequestMapping("/edit.do")
    public Result edit(@RequestBody CheckGroup checkGroup,Integer[] checkItemIds){
        try {
            checkGroupService.edit(checkGroup,checkItemIds);
        } catch (Exception e) {
            return new Result(false,MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }
}
