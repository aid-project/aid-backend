package com.aid.aidbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3FileService implements FileService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Override
    public String upload(MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            String extension = StringUtils.getFilenameExtension(filename);
            String uri = UUID.randomUUID() + "." + extension;

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(uri)
                    .contentType(MediaType.IMAGE_PNG_VALUE)
                    .build();

            PutObjectResponse putObjectResponse = s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));

            return uri;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String retrieve(String uri) {
        GetUrlRequest request = GetUrlRequest.builder()
                .bucket(bucketName)
                .key(uri)
                .build();

        return s3Client.utilities().getUrl(request).toString();
    }
}
