package com.example.afaloan.configurations.s3

import io.minio.BucketExistsArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class S3Configuration {

    @Bean
    fun s3Client(s3Properties: S3Properties): MinioClient {
        val minioClient = MinioClient.builder()
            .endpoint(s3Properties.endpoint)
            .credentials(s3Properties.accessKey, s3Properties.secretKey)
            .region(s3Properties.region)
            .build()

        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(s3Properties.bucket).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(s3Properties.bucket).build())
        }
        return minioClient
    }
}