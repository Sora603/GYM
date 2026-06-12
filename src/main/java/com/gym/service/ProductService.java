package com.gym.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gym.entity.Product;
import com.gym.entity.SalesRecord;
import com.gym.mapper.ProductMapper;
import com.gym.mapper.SalesRecordMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {

    @Resource
    private ProductMapper productMapper;

    @Resource
    private SalesRecordMapper salesRecordMapper;

    // 在售商品
    public List<Product> getActiveProducts() {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus, 1);
        return productMapper.selectList(wrapper);
    }

    // 全部商品（管理用）
    public List<Product> getAllProducts() {
        return productMapper.selectList(null);
    }

    public Product addProduct(Product product) {
        productMapper.insert(product);
        return product;
    }

    public Product updateProduct(Product product) {
        productMapper.updateById(product);
        return productMapper.selectById(product.getId());
    }

    public void deleteProduct(Long id) {
        Product p = productMapper.selectById(id);
        if (p != null) {
            p.setStatus(0);
            productMapper.updateById(p);
        }
    }

    public Product getById(Long id) {
        return productMapper.selectById(id);
    }

    // 用户购买商品（减库存）
    @Transactional
    public void buyProduct(Long id, Long userId) {
        Product product = productMapper.selectById(id);
        if (product == null || product.getStatus() != 1) {
            throw new RuntimeException("商品不存在或已下架");
        }
        if (product.getStock() < 1) {
            throw new RuntimeException("商品库存不足");
        }
        product.setStock(product.getStock() - 1);
        productMapper.updateById(product);

        // 记录销售
        SalesRecord record = new SalesRecord();
        record.setUserId(userId);
        record.setItemType("PRODUCT");
        record.setItemName(product.getName());
        record.setAmount(product.getPrice());
        record.setCreateTime(LocalDateTime.now());
        salesRecordMapper.insert(record);
    }
}
