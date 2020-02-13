package com.cfy.interest.service.impl;

import com.cfy.interest.mapper.ArticleMapper;
import com.cfy.interest.mapper.ArticleOperationMessageMapper;
import com.cfy.interest.mapper.CircleMapper;
import com.cfy.interest.model.Article;
import com.cfy.interest.model.ArticleOperationMessage;
import com.cfy.interest.provider.AliyunOSSProvider;
import com.cfy.interest.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private AliyunOSSProvider provider;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleOperationMessageMapper articleOperationMessageMapper;

    @Autowired
    private CircleMapper circleMapper;
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

    /**
     *
     * @param uid
     * @param cid
     * @param content
     */
    @Transactional
    @Override
    public void publish(long uid, Integer cid, String content) {
        Article article = new Article(uid, cid, content);
        //插入帖子数据
        articleMapper.insert(article);

        ArticleOperationMessage articleOperationMessage = new ArticleOperationMessage();
        articleOperationMessage.setAid(article.getId());
        articleOperationMessage.setUid(uid);
        articleOperationMessage.setMessage("发布帖子");
        articleOperationMessage.setType(1);

        articleOperationMessageMapper.insert(articleOperationMessage);

        //圈子新增帖子数
        circleMapper.addArticle(cid);

    }
}
