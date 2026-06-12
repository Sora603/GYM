package com.gym.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gym.entity.News;
import com.gym.mapper.NewsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class NewsService {

    @Resource
    private NewsMapper newsMapper;

    // 获取最新10条新闻
    public List<News> getLatestNews() {
        LambdaQueryWrapper<News> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(News::getPublishTime);
        wrapper.last("LIMIT 10");
        return newsMapper.selectList(wrapper);
    }

    public List<News> getAllNews() {
        LambdaQueryWrapper<News> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(News::getPublishTime);
        return newsMapper.selectList(wrapper);
    }

    public News addNews(News news) {
        newsMapper.insert(news);
        return news;
    }

    public News updateNews(News news) {
        newsMapper.updateById(news);
        return newsMapper.selectById(news.getId());
    }

    public void deleteNews(Long id) {
        newsMapper.deleteById(id);
    }

    public News getById(Long id) {
        return newsMapper.selectById(id);
    }
}
