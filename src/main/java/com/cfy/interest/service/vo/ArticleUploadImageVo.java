package com.cfy.interest.service.vo;

import lombok.Data;

import java.util.List;

@Data
public class ArticleUploadImageVo {
    private int errno;

    private List<String> data;
}
