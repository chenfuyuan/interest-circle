package com.cfy.interest.service.impl;

import com.cfy.interest.mapper.*;
import com.cfy.interest.model.Article;
import com.cfy.interest.model.ArticleLike;
import com.cfy.interest.model.ArticleOperationMessage;
import com.cfy.interest.model.ArticleStar;
import com.cfy.interest.provider.AliyunOSSProvider;
import com.cfy.interest.service.ArticleService;
import com.cfy.interest.vo.AjaxMessage;
import com.cfy.interest.vo.ArticleShow;
import com.cfy.interest.vo.EditorArticleVo;
import com.cfy.interest.vo.GetArticleVo;
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
        ArticleOperationMessage articleOperationMessage = new ArticleOperationMessage(uid,article.getId(),
                ArticleOperationMessage.CREATE,cid);
        articleOperationMessageMapper.insert(articleOperationMessage);
        //圈子新增帖子数
        circleMapper.addArticle(cid);
    }

    @Override
    public List<Article> getStickys(int cid) {
        List<Article> stickys = articleMapper.findStickysByCid(cid);
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
        String search = getArticleVo.getSearch();
        List<ArticleShow> articleShows;
        //设置帖子类型
        if (type.equals("essence")) {
            //设置查找帖子类型为精华贴
            if (search.equals("")) {
                articleShows = articleMapper.findEssenceByCid(cid);
            } else {
                search = "%" + search + "%";
                articleShows = articleMapper.findEssenceSearchByCid(cid, search);
            }
        } else {
            if (search.equals("")) {
                articleShows = articleMapper.findByCid(cid);
            } else {
                search = "%" + search + "%";
                articleShows = articleMapper.findSearchByCid(cid, search);
            }
        }
        for (ArticleShow articleShow : articleShows) {
            //判断是否点赞
            int likes = articleLikeMapper.isLike(uid, articleShow.getId());
            articleShow.setLike(likes>0);
            int stars = articleStarMapper.isStar(uid, articleShow.getId());
            articleShow.setStar(stars>0);

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
    public AjaxMessage like(long uid, Integer aid,Integer cid) {
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
        ArticleOperationMessage articleOperationMessage = new ArticleOperationMessage(uid,aid,
                ArticleOperationMessage.LIKE,cid);
        articleOperationMessageMapper.insert(articleOperationMessage);

        AjaxMessage ajaxMessage = new AjaxMessage(true, "点赞成功");
        //无，插入新的点赞数据
        return ajaxMessage;
    }


    @Transactional
    @Override
    public AjaxMessage cancelLike(long uid, Integer aid,Integer cid) {
        //减少帖子点赞人数
        int total = articleMapper.cancelLike(aid);
        if (total < 1) {
            return new AjaxMessage(false, "取消点赞失败，帖子不存在");
        }


        //更新帖子点赞状态
        articleLikeMapper.cancelLike(aid, uid);

        //生成帖子操作日志
        ArticleOperationMessage articleOperationMessage = new ArticleOperationMessage(uid,aid,
                ArticleOperationMessage.CANCELLIKE,cid);
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
        int like = articleLikeMapper.isLike(uid, aid);
        return like>0;
    }

    @Transactional
    @Override
    public Article sticky(long uid, Integer aid,Integer cid) {
        //更改为置顶状态
        int changrow = articleMapper.stickyArticle(aid);
        if (changrow < 1) {
            return null;
        }
        //记录操作记录
        ArticleOperationMessage articleOperationMessage = new ArticleOperationMessage(uid,aid,
                ArticleOperationMessage.STICKY,cid);
        articleOperationMessageMapper.insert(articleOperationMessage);


        return articleMapper.selectById(aid);
    }

    @Transactional
    @Override
    public AjaxMessage essence(long uid, Integer aid,Integer cid) {
        int changrow = articleMapper.essenceByAid(aid);
        if (changrow < 1) {
            return new AjaxMessage(false, "加精失败，帖子不存在");
        }

        //日志
        ArticleOperationMessage articleOperationMessage = new ArticleOperationMessage(uid,aid,
                ArticleOperationMessage.ESSENCE,cid);
        articleOperationMessageMapper.insert(articleOperationMessage);

        return new AjaxMessage(true, "加精成功");
    }

    @Override
    public AjaxMessage cancelSticky(long uid, Integer aid,Integer cid) {
        int changeRow = articleMapper.cancelSticky(aid);

        if (changeRow < 1) {
            return new AjaxMessage(false,"撤除置顶帖子失败");
        }

        //写日志
        ArticleOperationMessage articleOperationMessage = new ArticleOperationMessage(uid,aid,
                ArticleOperationMessage.CANCELSTICKY,cid);
        articleOperationMessageMapper.insert(articleOperationMessage);
        return new AjaxMessage(true, "撤销置顶帖子成功");
    }

    /**
     * 取消加精
     * @param uid
     * @param aid
     * @return
     */
    @Transactional
    @Override
    public AjaxMessage cancelEssence(long uid, Integer aid,Integer cid) {
        int changeRow = articleMapper.cancelEssence(aid);

        if (changeRow < 1) {
            return new AjaxMessage(false,"取消加精帖子失败");
        }

        //写日志
        ArticleOperationMessage articleOperationMessage = new ArticleOperationMessage(uid,aid,
                ArticleOperationMessage.CANCELESSENCE,cid);
        articleOperationMessageMapper.insert(articleOperationMessage);
        return new AjaxMessage(true, "取消加精帖子成功");
    }

    @Transactional
    @Override
    public AjaxMessage star(long uid, Integer aid,Integer cid) {
        //调整帖子的收藏数
        int articleChangeRow = articleMapper.star(aid);
        if (articleChangeRow < 1) {
            return new AjaxMessage(false, "帖子不存在");
        }

        //查看数据库是否原来就有数据
        int articleStarChangeRow = articleStarMapper.star(aid,uid);
        //插入收藏记录
        if (articleStarChangeRow < 1) {
            ArticleStar articleStar = new ArticleStar(uid, aid);
            articleStarMapper.insert(articleStar);
        }

        //日志
        ArticleOperationMessage articleOperationMessage = new ArticleOperationMessage(uid, aid,
                ArticleOperationMessage.STAR,cid);
        articleOperationMessageMapper.insert(articleOperationMessage);
        return new AjaxMessage(true, "收藏成功");
    }

    /**
     * 用户取消收藏
     * @param uid
     * @param aid
     * @return
     */
    @Transactional
    @Override
    public AjaxMessage cancelStar(long uid, Integer aid,Integer cid) {
        int articleChangeRow = articleMapper.cancelStar(aid);
        if (articleChangeRow < 1) {
            return new AjaxMessage(false, "帖子不存在");
        }

        int articleStarChangeRow = articleStarMapper.cancelStar(aid,uid);
        if (articleStarChangeRow < 1) {
            return new AjaxMessage(false, "您未收藏该贴子");
        }

        ArticleOperationMessage articleOperationMessage = new ArticleOperationMessage(uid, aid,
                ArticleOperationMessage.CANCELSTAR,cid);
        articleOperationMessageMapper.insert(articleOperationMessage);
        return new AjaxMessage(true, "取消收藏帖子成功");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxMessage delete(Long uid, Integer aid,Integer cid) throws Exception{

        int changeCircleRow = circleMapper.deleteArticle(cid);
        if (changeCircleRow < 1) {
            throw new Exception("该帖子所属的圈子不存在");
        }

        int changeArticleRow =  articleMapper.deleteById(aid,cid);
        if (changeArticleRow < 1) {
            throw new Exception("该帖子不存在");
        }

        //删除收藏，置顶,点赞等信息
        articleStarMapper.cancelStarByAid(aid);
        articleLikeMapper.cancelLikeByAid(aid);


        ArticleOperationMessage articleOperationMessage = new ArticleOperationMessage(uid, aid,
                ArticleOperationMessage.DELETE,cid);
        articleOperationMessageMapper.insert(articleOperationMessage);

        return new AjaxMessage(true, "删除帖子成功");

    }

    @Override
    public ArticleShow getArticle(Integer aid, int cid,Long uid) {
        ArticleShow articleShow = articleMapper.selectShowById(aid);
        log.info("articleShow = " +articleShow);
        if (cid != articleShow.getCid()) {
            log.info("帖子与圈子不匹配");
            return null;
        } else {
            log.info("开始填充是否点赞或收藏");
            int likes = articleLikeMapper.isLike(uid, articleShow.getId());
            articleShow.setLike(likes > 0);
            int stars = articleStarMapper.isStar(uid, articleShow.getId());
            articleShow.setStar(stars > 0);
            return articleShow;
        }
    }

    @Override
    public List<ArticleShow> getStickList(Long uid,Integer cid) {
        List<ArticleShow> articleList = articleMapper.getStickList(cid);
        for (ArticleShow articleShow : articleList) {
            //判断是否点赞
            int likes = articleLikeMapper.isLike(uid, articleShow.getId());
            articleShow.setLike(likes>0);
            int stars = articleStarMapper.isStar(uid, articleShow.getId());
            articleShow.setStar(stars>0);
        }
        return articleList;
    }
}
