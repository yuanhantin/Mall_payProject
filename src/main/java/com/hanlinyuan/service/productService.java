package com.hanlinyuan.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hanlinyuan.dao.ProductMapper;
import com.hanlinyuan.pojo.Product;
import com.hanlinyuan.service.Imp.impProductService;
import com.hanlinyuan.vo.ProductDetailVo;
import com.hanlinyuan.vo.ResponseVo;
import com.hanlinyuan.vo.productVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.hanlinyuan.Enum.ProductStatusEnum.DELETE;
import static com.hanlinyuan.Enum.ProductStatusEnum.OFF_SALE;
import static com.hanlinyuan.Enum.ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE;

/**
 * @Author: 翰林猿
 * @Description: TODO
 **/
@Service
public class productService implements impProductService {
    @Resource
    ProductMapper productMapper;

    @Resource
    categoryService categoryService;

    @Override
    public ResponseVo<PageInfo> list(Integer categoryId, Integer pageNum,Integer pageSize) {
        Set<Integer> categoryIdSet = new HashSet<>();
        if(categoryId!=null){
            categoryService.findSubCategoryId(categoryId,categoryIdSet);
            categoryIdSet.add(categoryId);
        }

        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList = productMapper.selectByCategoryIdSet(categoryIdSet);
        List<productVo> products = productList.stream()
                        .map(e->{
            productVo productVo = new productVo();
            BeanUtils.copyProperties(e,productVo);
            return productVo;
        }).collect(Collectors.toList());
        PageInfo pageInfo = new PageInfo<>(productList);
        pageInfo.setList(products);
        return ResponseVo.success(pageInfo);
    }
    @Override
    public ResponseVo<ProductDetailVo> detail(Integer productId) {
        Product product = productMapper.selectByPrimaryKey(productId);

        //只对确定性条件判断
        if (product.getStatus().equals(OFF_SALE.getCode())
                || product.getStatus().equals(DELETE.getCode())) {
            return ResponseVo.error(PRODUCT_OFF_SALE_OR_DELETE);
        }

        ProductDetailVo productDetailVo = new ProductDetailVo();
        BeanUtils.copyProperties(product, productDetailVo);
        //敏感数据处理
        productDetailVo.setStock(product.getStock() > 100 ? 100 : product.getStock());
        return ResponseVo.success(productDetailVo);
    }
}
