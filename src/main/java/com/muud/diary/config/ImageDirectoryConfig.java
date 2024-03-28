package com.muud.diary.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties("cloud.aws.s3")
@RequiredArgsConstructor
public class ImageDirectoryConfig {

    private final String imageDirectory;
}
