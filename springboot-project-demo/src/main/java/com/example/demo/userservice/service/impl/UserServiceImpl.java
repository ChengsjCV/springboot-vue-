package com.example.demo.userservice.service.impl;

import com.example.demo.userservice.entity.User;
import com.example.demo.userservice.mapper.UserMapper;
import com.example.demo.userservice.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chengsir
 * @since 2022-06-18
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
