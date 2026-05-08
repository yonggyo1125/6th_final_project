package org.sparta.fileservice.infrastructure.storage.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@ConditionalOnProperty(
        name = "file.storage.type",
        havingValue = "s3"
)
@RequiredArgsConstructor
@EnableConfigurationProperties(S3StorageProperties.class)
public class S3StorageConfig {

    private final S3StorageProperties properties;

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(
                properties.accessKey(),
                properties.secretKey()
        );

        return S3Client.builder()
                .region(Region.of(properties.region()))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }
}
