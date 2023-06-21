package com.aid.aidbackend.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String upload(MultipartFile file);

    String retrieve(String uri);

}
