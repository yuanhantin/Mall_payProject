package com.hanlinyuan.service.Imp;

import com.github.pagehelper.PageInfo;
import com.hanlinyuan.vo.ProductDetailVo;
import com.hanlinyuan.vo.ResponseVo;
import com.hanlinyuan.vo.productVo;

import java.util.List;

/**
 * @Author: 翰林猿
 * @Description: TODO
 **/
public interface impProductService {
    ResponseVo<PageInfo> list(Integer categoryId , Integer pageNum, Integer pageEnd);     //返回和前端约定好的list格式

    ResponseVo<ProductDetailVo> detail(Integer productId);
}
