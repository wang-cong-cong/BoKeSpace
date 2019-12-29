package com.itheima.service;

import com.itheima.entity.Result;

import java.util.Map;

/**
 * 体检预约接口
 * @author cong
 */
public interface OrderService {
    //体检预约
    Result order(Map map) throws Exception;
}
