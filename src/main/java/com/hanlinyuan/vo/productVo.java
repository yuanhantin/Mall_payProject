package com.hanlinyuan.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: 翰林猿
 * @Description: Vo类
 **/
@Data
public class productVo {
    private Integer id;

    private Integer categoryId;

    private String name;

    private String subtitle;

    private String mainImage;
    private Integer status;
    private BigDecimal price;
}
