package com.example.demo.userservice.service.impl;

import com.example.demo.userservice.entity.News;
import com.example.demo.userservice.mapper.NewsMapper;
import com.example.demo.userservice.service.NewsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chengsir
 * @since 2022-06-25
 */
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {

}
