package com.hanlinyuan.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: 翰林猿
 * @Description: 分类表的 Vo类
 **/
@Data
public class CategoryVo {
    private Integer id;

    private Integer parentId;

    private String name;

    private Integer sortOrder;

    private List<CategoryVo> subCategories;
}
