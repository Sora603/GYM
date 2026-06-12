package com.gym.controller;

import com.gym.common.Result;
import com.gym.entity.News;
import com.gym.entity.User;
import com.gym.service.NewsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Resource
    private NewsService newsService;

    // 公开 - 最新新闻
    @GetMapping("/latest")
    public Result<?> latest() {
        return Result.ok(newsService.getLatestNews());
    }

    // 公开 - 单条最新新闻（跑马灯用）
    @GetMapping("/ticker")
    public Result<?> ticker() {
        List<News> list = newsService.getLatestNews();
        if (list.isEmpty()) return Result.ok(null);
        return Result.ok(list.get(0));
    }

    @GetMapping("/all")
    public Result<?> all() {
        return Result.ok(newsService.getAllNews());
    }

    @GetMapping("/{id}")
    public Result<?> detail(@PathVariable Long id) {
        return Result.ok(newsService.getById(id));
    }

    // 管理端
    @PostMapping("/admin/add")
    public Result<?> add(@RequestBody News news, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) return Result.fail(403, "无权限");
        news.setAdminId(user.getId());
        return Result.ok(newsService.addNews(news));
    }

    @PutMapping("/admin/update")
    public Result<?> update(@RequestBody News news, HttpSession session) {
        if (!isAdmin(session)) return Result.fail(403, "无权限");
        return Result.ok(newsService.updateNews(news));
    }

    @DeleteMapping("/admin/delete/{id}")
    public Result<?> delete(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) return Result.fail(403, "无权限");
        newsService.deleteNews(id);
        return Result.ok("删除成功", null);
    }

    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null && "ADMIN".equals(user.getRole());
    }
}
