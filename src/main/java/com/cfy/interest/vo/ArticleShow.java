package com.cfy.interest.vo;

import com.cfy.interest.model.Article;
import lombok.Data;

/**/
@Data
public class ArticleShow extends Article {
    private boolean like;
    private boolean star;

    @Override
    public String toString() {
        return super.toString()+"ArticleShow{" +
                "isLike=" + like +
                ", isStar=" + star +
                '}';
    }
}
