package com.cfy.interest.model;

public class ArticleCommentShow extends ArticleComment {
    private boolean like;

    @Override
    public String toString() {
        return super.toString() + "ArticleCommentShow{" +
                "like=" + like +
                '}';
    }
}
