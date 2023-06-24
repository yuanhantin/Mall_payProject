package com.hanlinyuan.service.Imp;

import com.hanlinyuan.pojo.Category;
import com.hanlinyuan.vo.CategoryVo;
import com.hanlinyuan.vo.ResponseVo;

import java.util.List;
import java.util.Set;

/**
 * @Author: 翰林猿
 * @Description: TODO
 **/
public interface impCategoryService {

    //查询所有分类
    ResponseVo<List<CategoryVo>> selectAll();

    //获取商品列表，由于查询一个商品也要把所有子类查出来，
    // 所以也是使用递归不断的获取子类，所以返回值就是他的参数，
    // 返回值应该是id的集合，由于id并不会重复，所以我们使用set集合
    void findSubCategoryId(Integer id, Set<Integer> resultSet);


    //为了递归时不反复调用selectAll方法，将查询出来的categories作为参数传递下去
    void findSubCategoryId(Integer id, Set<Integer> resultSet,List<Category> categories);



}
