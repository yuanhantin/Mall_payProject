package com.hanlinyuan.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: 翰林猿
 * @Description: TODO
 **/
@Data
public class CategoryVo {
    private Integer id;

    private Integer parentId;

    private String name;

    private Integer sortOrder;

    private List<CategoryVo> subCategories;
}
