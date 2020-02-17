package com.cfy.interest.vo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateCircleFormVo {
    private String circleName;
    private MultipartFile avatar;
    private int province;
    private int city;
}
