package com.example.demo.userservice.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.userservice.common.result.R;
import com.example.demo.userservice.entity.User;
import com.example.demo.userservice.mapper.UserMapper;
import com.example.demo.userservice.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author chengsir
 * @since 2022-06-18
 */
@Api("User管理")
@RestController
@RequestMapping("/userservice/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    // 查询全部
    @ApiOperation("查询全部用户信息")
    @GetMapping("findAll")
    public R findAll(){
        List<User> list = userService.list(null);
        return R.ok().data("items", list);
    }

    // 新增
    @ApiOperation("新增用户信息")
    @PostMapping("addUser")
    public R save(@RequestBody User user){
        userMapper.insert(user);
        return R.ok();
    }

    // 根据 页码/当前页/搜索栏 进行条件查询  (这里搜索栏的内容是nickName)
    @ApiOperation("根据页码/当前页/搜索栏进行查询")
    @GetMapping("findByPageNum")
    public R findByPageNum(@RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "5") Integer limit, @RequestParam(required = false) String search){
        System.out.println(currentPage + limit);
        Page<User> pageUser = new Page<>(currentPage, limit);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        /*if(StrUtil.isNotBlank(search)){
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("nick_name", search);
            Page<User> res = userMapper.selectPage(pageUser, queryWrapper);
            long total = res.getTotal();
            List<User> records = res.getRecords();
            System.out.println("total = " + total);
            return R.ok().data("total", total).data("rows", records);
        }else{
            userService.page(pageUser, null);
            long total = pageUser.getTotal();
            List<User> records = pageUser.getRecords();
            System.out.println("total = " + total);
            return R.ok().data("total", total).data("rows", records);
        }*/
        // 下面这种写法是上面注释写法的简化版
        if(StringUtils.isNotBlank(search)){
            queryWrapper.like("nick_name", search);
        }
        Page<User> res = userMapper.selectPage(pageUser, queryWrapper);
        long total = res.getTotal();
        List<User> records = res.getRecords();
        return R.ok().data("total", total).data("row", records);
    }

    // post -> 新增  get -> 查询   put -> 更新
    @ApiOperation("更新用户数据")
    @PutMapping("update")
    public R updateById(@RequestBody User user){
        userMapper.updateById(user);
        return R.ok();
    }

    // delete -> 删除
    @ApiOperation("删除用户数据")
    @DeleteMapping("/delete/{id}")
    public R deleteById(@PathVariable("id") Integer id){
        userMapper.deleteById(id);
        return R.ok();
    }

    // logn -> 登陆
    @ApiOperation("登陆")
    @PostMapping("login")
    public R login(@RequestBody User user){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", user.getUsername()).eq("password", user.getPassword());
        User res = userMapper.selectOne(wrapper);
        if(res == null){
            return R.error().message("用户名或密码错误");
        }
        return R.ok().data("items", res);

    }

    // 注册
    @ApiOperation("注册用户信息")
    @PostMapping("register")
    public R register(@RequestBody User user){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", user.getUsername());
        User res = userMapper.selectOne(wrapper);
        if(res != null){
            return R.error().message("用户名已存在");
        }
        userMapper.insert(user);
        return R.ok();
    }

    @ApiOperation("根据id查找用户信息")
    @GetMapping("/find/{id}")
    public R findById(@PathVariable Integer id){
        User user = userMapper.selectById(id);
        return R.ok().data("item", user);
    }
}

