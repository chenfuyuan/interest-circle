package com.cfy.interest.vo;

import lombok.Data;

@Data
public class ArticleCommentReplyVo {
    private Long ruid;
    private Integer acid;
    private Integer type;
    private Integer aid;
    private String content;
    private Integer cid;
}
