package com.cfy.interest.vo;

import lombok.Data;

@Data
public class GetArticleVo {
    private String sort;
    private String type;
    private int pageNum;
    private int cid;
}
