package com.gym.controller;

import com.gym.common.Result;
import com.gym.entity.Product;
import com.gym.entity.User;
import com.gym.service.ProductService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Resource
    private ProductService productService;

    // 公开 - 在售商品
    @GetMapping("/list")
    public Result<?> list() {
        return Result.ok(productService.getActiveProducts());
    }

    // 用户购买商品
    @PostMapping("/buy/{id}")
    public Result<?> buy(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return Result.fail(401, "请先登录");
        try {
            productService.buyProduct(id, user.getId());
            return Result.ok("购买成功，请到前台领取", null);
        } catch (RuntimeException e) {
            return Result.fail(e.getMessage());
        }
    }

    // 管理端 - 全部商品
    @GetMapping("/admin/all")
    public Result<?> adminList(HttpSession session) {
        if (!isAdmin(session)) return Result.fail(403, "无权限");
        return Result.ok(productService.getAllProducts());
    }

    @PostMapping("/admin/add")
    public Result<?> add(@RequestBody Product product, HttpSession session) {
        if (!isAdmin(session)) return Result.fail(403, "无权限");
        return Result.ok(productService.addProduct(product));
    }

    @PutMapping("/admin/update")
    public Result<?> update(@RequestBody Product product, HttpSession session) {
        if (!isAdmin(session)) return Result.fail(403, "无权限");
        return Result.ok(productService.updateProduct(product));
    }

    @DeleteMapping("/admin/delete/{id}")
    public Result<?> delete(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) return Result.fail(403, "无权限");
        productService.deleteProduct(id);
        return Result.ok("下架成功", null);
    }

    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null && "ADMIN".equals(user.getRole());
    }
}
