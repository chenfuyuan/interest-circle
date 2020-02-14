package com.cfy.interest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cfy.interest.mapper.ArticleMapper;
import com.cfy.interest.mapper.ArticleOperationMessageMapper;
import com.cfy.interest.mapper.ArticleStickyMapper;
import com.cfy.interest.mapper.CircleMapper;
import com.cfy.interest.model.Article;
import com.cfy.interest.model.ArticleOperationMessage;
import com.cfy.interest.model.ArticleSticky;
import com.cfy.interest.provider.AliyunOSSProvider;
import com.cfy.interest.service.ArticleService;
import com.cfy.interest.service.vo.EditorArticleVo;
import com.cfy.interest.service.vo.GetArticleVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    private ArticleStickyMapper articleStickyMapper;

    @Autowired
    private RedisTemplate redisTemplate;
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

    @Transactional
    @Override
    public void publish(long uid, EditorArticleVo editorArticleVo) {
        int cid = editorArticleVo.getCid();
        String content = editorArticleVo.getContent();
        String title = editorArticleVo.getTitle();
        Article article = new Article(uid, cid, content,title);
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

    @Override
    public List<ArticleSticky> getStickys(int cid) {
        List<ArticleSticky> stickys = articleStickyMapper.findStickysByCid(cid);
        return stickys;
    }


    /**
     * 根据传递来的规则获取帖子列表
     * @param getArticleVo
     * @return
     */
    @Override
    public List<Article> getArticles(GetArticleVo getArticleVo) {
        String type = getArticleVo.getType();
        List<Article> articles = null;
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        int cid = getArticleVo.getCid();


        String sort = getArticleVo.getSort();

        //设置帖子类型
        if (type.equals("essence")) {
            //设置查找帖子类型为精华贴
            articles = articleMapper.findEssenceByCid(cid, sort);
        } else {
            articles = articleMapper.findByCid(cid, sort);
        }
        //根据查询规则查询数据
        return articles;
    }
}
