package com.example.demo.userservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.example.demo.userservice.common.result.R;
import com.example.demo.userservice.entity.Book;
import com.example.demo.userservice.mapper.BookMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author chengsir
 * @since 2022-06-22
 */
@Api("书籍管理")
@RestController
@RequestMapping("/userservice/book")
public class BookController {
    @Resource
    private BookMapper bookMapper;

    // 查询全部
    @ApiOperation("查询全部书籍信息")
    @GetMapping("findAll")
    public R findAll(){
        List<Book> list = bookMapper.selectList(null);
        return R.ok().data("items", list);
    }

    // 新增
    @ApiOperation("新增书籍信息")
    @PostMapping("addBook")
    public R save(@RequestBody Book book){
        bookMapper.insert(book);
        return R.ok();
    }

    // 根据 页码/当前页/搜索栏 进行条件查询  (这里搜索栏的内容是nickName)
    @ApiOperation("根据页码/当前页/搜索栏进行查询")
    @GetMapping("findByPageNum")
    public R findByPageNum(@RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "5") Integer limit, @RequestParam(required = false) String search){
        System.out.println(currentPage + limit);
        Page<Book> pageBook = new Page<>(currentPage, limit);
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        /*if(StrUtil.isNotBlank(search)){
            QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("nick_name", search);
            Page<Book> res = bookMapper.selectPage(pageBook, queryWrapper);
            long total = res.getTotal();
            List<Book> records = res.getRecords();
            System.out.println("total = " + total);
            return R.ok().data("total", total).data("rows", records);
        }else{
            bookMapper.page(pageBook, null);
            long total = pageBook.getTotal();
            List<Book> records = pageBook.getRecords();
            System.out.println("total = " + total);
            return R.ok().data("total", total).data("rows", records);
        }*/
        // 下面这种写法是上面注释写法的简化版
        if(StringUtils.isNotBlank(search)){
            queryWrapper.like("name", search);
        }
        Page<Book> res = bookMapper.selectPage(pageBook, queryWrapper);
        long total = res.getTotal();
        List<Book> records = res.getRecords();
        return R.ok().data("total", total).data("row", records);
    }

    // post -> 新增  get -> 查询   put -> 更新
    @ApiOperation("更新书籍数据")
    @PutMapping("update")
    public R updateById(@RequestBody Book book){
        bookMapper.updateById(book);
        return R.ok();
    }

    // delete -> 删除
    @ApiOperation("删除书籍数据")
    @DeleteMapping("/delete/{id}")
    public R deleteById(@PathVariable("id") Integer id){
        bookMapper.deleteById(id);
        return R.ok();
    }
}

