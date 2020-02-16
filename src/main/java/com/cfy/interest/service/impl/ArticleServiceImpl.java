package com.cfy.interest.service.impl;

import com.cfy.interest.mapper.*;
import com.cfy.interest.model.*;
import com.cfy.interest.provider.AliyunOSSProvider;
import com.cfy.interest.service.ArticleService;
import com.cfy.interest.service.vo.AjaxMessage;
import com.cfy.interest.service.vo.ArticleShow;
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

    @Autowired
    private ArticleLikeMapper articleLikeMapper;

    @Autowired
    private ArticleStarMapper articleStarMapper;
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
        articleOperationMessage.setType(ArticleOperationMessage.CREATE);

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
    public List<ArticleShow> getArticles(GetArticleVo getArticleVo,long uid) {
        String type = getArticleVo.getType();

        int cid = getArticleVo.getCid();

        List<ArticleShow> articleShows;
        //设置帖子类型
        if (type.equals("essence")) {
            //设置查找帖子类型为精华贴
            articleShows = articleMapper.findEssenceByCid(cid);
        } else {
            articleShows = articleMapper.findByCid(cid);
        }

        for (ArticleShow articleShow : articleShows) {
            //判断是否点赞
            ArticleLike like = articleLikeMapper.isLike(uid, articleShow.getId());
            articleShow.setLike(like!=null);
            ArticleStar star = articleStarMapper.isStar(uid, articleShow.getId());
            articleShow.setStar(star!=null);

        }

        return articleShows;
    }

    @Override
    public int selectCountByCId(int cid) {
        return articleMapper.selectCountByCid(cid);
    }

    /**
     * @param uid
     * @param aid
     * @return
     */
    @Transactional
    @Override
    public AjaxMessage like(long uid, Integer aid) {
        //判断数据库是否有该用户点赞的记录，有？直接更新状态码为1
        int total = articleLikeMapper.like(aid, uid);

        log.info("total = " + total);
        //数据库无数据，插入新的点赞数据
        if (total < 1) {
            ArticleLike articleLike = new ArticleLike();
            articleLike.setAid(aid);
            articleLike.setUid(uid);
            articleLike.setState(1);
            articleLikeMapper.insert(articleLike);
        }

        //更新帖子的点赞数
        articleMapper.like(aid);

        //更新帖子操作日志
        ArticleOperationMessage articleOperationMessage = new ArticleOperationMessage();
        articleOperationMessage.setType(ArticleOperationMessage.LIKE);
        articleOperationMessage.setUid(uid);
        articleOperationMessage.setAid(aid);
        articleOperationMessageMapper.insert(articleOperationMessage);

        AjaxMessage ajaxMessage = new AjaxMessage(true, "点赞成功");
        //无，插入新的点赞数据
        return ajaxMessage;
    }


    @Transactional
    @Override
    public AjaxMessage cancelLike(long uid, Integer aid) {
        //减少帖子点赞人数
        int total = articleMapper.cancelLike(aid);
        if (total < 1) {
            return new AjaxMessage(false, "取消点赞失败，帖子不存在");
        }


        //更新帖子点赞状态
        articleLikeMapper.cancelLike(aid, uid);

        //生成帖子操作日志
        ArticleOperationMessage articleOperationMessage = new ArticleOperationMessage();
        articleOperationMessage.setAid(aid);
        articleOperationMessage.setUid(uid);
        articleOperationMessage.setType(ArticleOperationMessage.CANCELLIKE);
        articleOperationMessageMapper.insert(articleOperationMessage);

        return new AjaxMessage(true, "取消点赞成功");
    }

    /**
     * 判断用户是否对该帖子点赞
     * @param uid
     * @param aid
     * @return
     */
    @Override
    public boolean isLike(long uid, Integer aid) {
        ArticleLike like = articleLikeMapper.isLike(uid, aid);
        if (like != null) {
            return true;
        } else {
            return false;
        }
    }
}
