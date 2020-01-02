package com.itheima.controller;


import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cong
 */
@RestController
@RequestMapping("/authority")
public class AuthorityController {

    @RequestMapping("/checkItem.do")
    public Result checkItem() {

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("add",false);
        map.put("edit",false);
        map.put("delete",false);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Collection<GrantedAuthority> authorities = user.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            String keywords = authority.getAuthority();
            if ("CHECKITEM_ADD".equals(keywords)){
                map.put("add",true);
            }
            if ("CHECKITEM_DELETE".equals(keywords)){
                map.put("delete",true);
            }
            if ("CHECKITEM_EDIT".equals(keywords)){
                map.put("edit",true);
            }
        }
        return new Result(true,"",map);
    }

    @RequestMapping("/checkGroup.do")
    public Result checkGroup() {

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("add",false);
        map.put("edit",false);
        map.put("delete",false);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Collection<GrantedAuthority> authorities = user.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            String keywords = authority.getAuthority();
            if ("CHECKGROUP_ADD".equals(keywords)){
                map.put("add",true);
            }
            if ("CHECKGROUP_DELETE".equals(keywords)){
                map.put("delete",true);
            }
            if ("CHECKGROUP_EDIT".equals(keywords)){
                map.put("edit",true);
            }
        }
        return new Result(true,"",map);
    }

    @RequestMapping("/setMeal.do")
    public Result setMeal() {

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("add",false);
        map.put("edit",false);
        map.put("delete",false);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Collection<GrantedAuthority> authorities = user.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            String keywords = authority.getAuthority();
            if ("SETMEAL_ADD".equals(keywords)){
                map.put("add",true);
            }
            if ("SETMEAL_DELETE".equals(keywords)){
                map.put("delete",true);
            }
            if ("SETMEAL_EDIT".equals(keywords)){
                map.put("edit",true);
            }
        }
        return new Result(true,"",map);
    }
}
