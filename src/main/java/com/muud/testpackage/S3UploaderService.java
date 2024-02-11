package com.muud.testpackage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;

@Service
public class S3UploaderService {

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${spring.cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${spring.cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    public void uploadImageToS3(MultipartFile file) {
        try {
            // AWS S3 클라이언트 생성
            S3Client s3Client = S3Client.builder()
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                    .build();

            // 파일 업로드 요청 생성
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(file.getOriginalFilename()) // S3에 저장될 파일 이름 설정
                    .build();

            // 이미지 파일 S3에 업로드
            PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            // 업로드 성공 여부 확인
            if (response.sdkHttpResponse().isSuccessful()) {
                System.out.println("이미지 파일이 성공적으로 업로드되었습니다.");
            } else {
                System.err.println("이미지 파일 업로드 실패: " + response.sdkHttpResponse().statusCode());
            }
        } catch (IOException e) {
            System.err.println("이미지 파일 업로드 중 IOException 발생: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("이미지 파일 업로드 중 오류 발생: " + e.getMessage());
        }
    }
}