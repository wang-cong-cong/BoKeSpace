package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.domain.Permission;
import com.itheima.domain.Role;
import com.itheima.domain.User;
import com.itheima.entity.PageResult;
import com.itheima.service.UserService;
import com.itheima.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<User> page = userDao.selectCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 添加用户并与角色建立关系
     * @param user
     * @param roleIds
     */
    @Override
    public void add(User user, Integer[] roleIds) {
        String ps = user.getPassword();
        String password = MD5Utils.md5(ps);
        user.setPassword(password);
        userDao.add(user);
     setUserAndRole(user.getId(),roleIds);
    }

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    @Override
    public User findById(Integer id) {
        return userDao.findById(id);
    }

    /**
     * 根据用户id查询角色id
     * @param id
     * @return
     */
    @Override
    public List<Integer> findRoleIdByUserId(Integer id) {
        return userDao.findRoleIdByUserId(id);
    }

    /**
     * 修改数据
     * @param user
     * @param roleIds
     */
    @Override
    public void edit(User user, Integer[] roleIds) {
        //更新用户基本信息
        String ps = user.getPassword();
        String password = MD5Utils.md5(ps);
        user.setPassword(password);
        userDao.edit(user);
        //删除已关联的中间表信息
        userDao.deleteAssociation(user.getId());
        //建立新的关系
        setUserAndRole(user.getId(),roleIds);
    }

    /**
     * 删除用户
     * @param id
     */
    @Override
    public void delete(Integer id)throws RuntimeException {
        long count = userDao.selectCountByUserId(id);
        if (count > 0){
            throw new RuntimeException("当前用户已经被引用，请先删除引用");
        }
        userDao.delete(id);
    }


    /**
     * 用户与角色添加关系的方法
     * @param userId
     * @param roleIds
     */
    private void setUserAndRole(Integer userId, Integer[] roleIds) {
        if (roleIds != null && roleIds.length > 0){
            for (Integer roleId : roleIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("user_id",userId);
                map.put("role_id",roleId);
                userDao.setUserAndRole(map);
            }
        }
    }
}
