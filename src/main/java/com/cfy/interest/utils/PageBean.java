package com.cfy.interest.utils;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class PageBean<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    //当前页
    private Integer pageNum = 1;
    //每页条数
    private Integer pageSize;
    //总记录数
    private Long total;
    //总页数
    private Integer totalPage;
    //查询条件实体对象
    private T queryModel;
    //数据
    private List<T> dataList;

    /**
     * 构造函数
     * @param pageNum
     * @param pageSize
     */
    public PageBean(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    /**
     * 转换分页对象
     * @param pageNum
     * @param pageSize
     * @param models
     * @param <T>
     * @return
     */
//    public static <T> PageBean<T> toPageBean(Integer pageNum, Integer pageSize, List models) {
//        PageBean<T> result = new PageBean();
//        if (null != models){
//            PageInfo<T> page = new PageInfo<>(models);
//            result.setPageNum(page.getPageNum());
//            result.setPageSize(page.getSize());
//            result.setTotal(page.getTotal());
//            result.setTotalPage(page.getPages());
//            result.setDataList(models);
//        } else {
//            result.setPageNum(pageNum);
//            result.setPageSize(pageSize);
//        }
//        //页数为0时，前端显示会有问题，默认为1
//        if (result.getPageNum() == 0) {
//            result.setPageNum(1);
//        }
//        return result;
//    }

}
