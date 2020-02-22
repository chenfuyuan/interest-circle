package com.cfy.interest.vo;

import lombok.Data;

@Data
public class CircleDayStatistics {

    //新增成员
    private Integer join;


    //互动成员
    private Integer interact;


    //新增帖子
    private Integer newArticle;


    //新增回复
    private Integer newComment;


}
