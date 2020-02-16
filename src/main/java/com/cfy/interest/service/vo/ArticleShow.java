package com.cfy.interest.service.vo;

import com.cfy.interest.model.Article;
import lombok.Data;

/**/
@Data
public class ArticleShow extends Article {
    private boolean isLike;
    private boolean isStar;

    @Override
    public String toString() {
        return super.toString()+"ArticleShow{" +
                "isLike=" + isLike +
                ", isStar=" + isStar +
                '}';
    }
}
