package com.cfy.interest.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArticleService {
    List<String> uploadImages(MultipartFile[] files);

    void publish(long uid, Integer cid, String article);
}
