package com.cfy.interest.vo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CircleSettingSaveVo {
    private Integer cid;
    private String name;
    private String introduce;
    private MultipartFile background;
    private MultipartFile avatar;
}
