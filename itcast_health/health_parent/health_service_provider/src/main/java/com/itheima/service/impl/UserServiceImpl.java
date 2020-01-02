package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.domain.Permission;
import com.itheima.domain.Role;
import com.itheima.domain.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * @author cong
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    /**
     * 根据用户名查询角色和权限
     * */
    @Override
    public User findByUserName(String username) {
        User user =  userDao.findByUserName(username);
         if (user == null){
             return null;
         }
        Integer userId = user.getId();
         //角色和用户是多对多关联，根据用户id查询角色
        Set<Role> roles = roleDao.findById(userId);
        if (roles != null && roles.size() >0) {
            for (Role role : roles) {
                Integer roleId = role.getId();
                //角色和权限是多对多关系，根据角色id查询权限
               Set<Permission> permissions  = permissionDao.findById(roleId);
               if (permissions != null && permissions.size()>0){
                   for (Permission permission : permissions) {
                       role.setPermissions(permissions);
                   }
               }
            }
            user.setRoles(roles);
        }
        return user;
    }
}
