package com.cfy.interest.vo;

import lombok.Data;

import java.util.List;

@Data
public class AddAdminVo {
    private Integer cid;
    private List<Integer> addAdminArray;
    private List<Integer> deleteAdminArray;
}
