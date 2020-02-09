package com.cfy.interest.service.vo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data

public class UserInfoVo {
    private String userName;

    private MultipartFile avatar;
}
