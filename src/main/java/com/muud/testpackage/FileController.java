package com.muud.testpackage;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final S3UploaderService s3UploaderService;

    @PostMapping("/upload")
    public String uploadImageToS3(@RequestPart("file") MultipartFile file) {
        try {
            s3UploaderService.uploadImageToS3(file);
            return "Image uploaded successfully to S3!";
        } catch (Exception e) {
            return "Failed to upload image to S3. Error: " + e.getMessage();
        }
    }
}