package com.example.demo.userservice.service.impl;

import com.example.demo.userservice.entity.Book;
import com.example.demo.userservice.mapper.BookMapper;
import com.example.demo.userservice.service.BookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chengsir
 * @since 2022-06-22
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

}
