package top.jjquiet.simplesavephoto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileUploadController {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);
    private static final String UPLOAD_DIR = "uploads/"; // 指定上传目录
    @PostMapping("/upload")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("未接收到文件");
        }
        try {
            // 确保上传目录存在
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) uploadDir.mkdirs();
            // 构建文件的保存路径
            String filename = file.getOriginalFilename(); // 获取原始文件名
            Path savePath = Paths.get(UPLOAD_DIR + filename);
            // 保存文件
            Files.copy(file.getInputStream(), savePath);
            logger.debug("文件上传成功, 文件名: {}", filename);
            return ResponseEntity.ok("文件上传成功，文件名: " + filename);
        } catch (IOException e) {
            logger.error("文件上传失败: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("文件上传失败");
        }
    }
}
