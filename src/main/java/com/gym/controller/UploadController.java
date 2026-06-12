package com.gym.controller;

import com.gym.common.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/gym-uploads/";

    @PostMapping("/image")
    public Result<?> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.fail("请选择文件");
        }

        // 校验文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return Result.fail("只支持上传图片文件");
        }

        try {
            // 确保上传目录存在
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 生成唯一文件名
            String originalName = file.getOriginalFilename();
            String ext = "";
            if (originalName != null && originalName.contains(".")) {
                ext = originalName.substring(originalName.lastIndexOf("."));
            }
            String newName = UUID.randomUUID().toString() + ext;

            // 保存文件
            Path filePath = Paths.get(UPLOAD_DIR, newName);
            Files.write(filePath, file.getBytes());

            // 返回可访问的URL
            Map<String, String> data = new HashMap<>();
            data.put("url", "/uploads/" + newName);
            return Result.ok("上传成功", data);
        } catch (IOException e) {
            return Result.fail("文件上传失败: " + e.getMessage());
        }
    }
}
