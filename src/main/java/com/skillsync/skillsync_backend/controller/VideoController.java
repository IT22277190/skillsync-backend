package com.skillsync.skillsync_backend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    private static final Logger logger = LoggerFactory.getLogger(VideoController.class);

    @Value("${video.upload.dir}")
    private String uploadDir;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file) {
        logger.info("Received upload request for file: {}", file.getOriginalFilename());
        try {
            // Ensure the upload directory exists
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Save the file to the upload directory
            Path filePath = Paths.get(uploadDir, file.getOriginalFilename());
            Files.write(filePath, file.getBytes());

            logger.info("File uploaded successfully: {}", file.getOriginalFilename());
            return ResponseEntity.ok("Video uploaded successfully: " + file.getOriginalFilename());
        } catch (IOException e) {
            logger.error("Failed to upload video: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload video");
        }
    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<byte[]> viewVideo(@PathVariable String fileName) {
        logger.info("Received view request for file: {}", fileName);
        try {
            Path filePath = Paths.get(uploadDir, fileName);
            byte[] videoBytes = Files.readAllBytes(filePath);
            logger.info("File retrieved successfully: {}", fileName);
            return ResponseEntity.ok().body(videoBytes);
        } catch (IOException e) {
            logger.error("Failed to retrieve video: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}