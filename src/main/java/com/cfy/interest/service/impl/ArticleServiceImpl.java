package com.cfy.interest.service.impl;

import com.cfy.interest.provider.AliyunOSSProvider;
import com.cfy.interest.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private AliyunOSSProvider provider;

    @Override
    public List<String> uploadImages(MultipartFile[] files) {

        List<String> urls = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            //上传到OSS服务器上，并获取url路径
            provider.initOssClient();
            String url = provider.uploadImg2Oss(file);
            urls.add(url);
        }
        return urls;
    }
}
