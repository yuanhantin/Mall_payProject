package com.hanlinyuan.service;

import com.hanlinyuan.dao.CategoryMapper;
import com.hanlinyuan.pojo.Category;
import com.hanlinyuan.service.Imp.impCategoryService;
import com.hanlinyuan.vo.CategoryVo;
import com.hanlinyuan.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static com.hanlinyuan.Const.mallConst.ROOT_PARENT_ID;

/**
 * @Author: 翰林猿
 * @Description: 实现分类目录
 **/
@Service
public class categoryService implements impCategoryService {
    @Resource
    CategoryMapper categoryMapper;
    @Override
    public ResponseVo<List<CategoryVo>> selectAll() {
        List<Category> categoryList = categoryMapper.selectAll();   //存放查出来的对象
        List<CategoryVo> categoryVoList = new ArrayList<>();        //存放要返回的对象

        for (Category category : categoryList) {
            if (category.getParentId().equals(ROOT_PARENT_ID)){     //就是一级目录
                CategoryVo categoryVo = new CategoryVo();
                BeanUtils.copyProperties(category,categoryVo);
                categoryVoList.add(categoryVo);
            }
        }
        categoryVoList.sort(Comparator.comparing(CategoryVo::getSortOrder).reversed());
        findSub(categoryList,categoryVoList);
        return ResponseVo.success(categoryVoList);
    }

    private void findSub(List<Category> categoryList,List<CategoryVo> categoryVoList){      //查找已查出来的类别的子类
        for (CategoryVo categoryVo : categoryVoList) {      //遍历每个已经被查出来的Vo类,对比数据库pojo类的id
            List<CategoryVo> subCategoryVoList = new ArrayList<>();
            for (Category category : categoryList) {
                //如果查出来的Vo类的id与数据库某项的父类id相同，说明Vo有子类,那么一直继续查他的子项到为null为止
                if (categoryVo.getId().equals(category.getParentId())){
                    //把查出来的category转成categoryVo，因为从数据库里拿出来的有些字段不需要返回给前端
                    CategoryVo subCategoryVo = category2CategoryVo(category);
                    subCategoryVoList.add(subCategoryVo);
                }
                //排序，按照sort_order字段大小排，大的靠前,所以反转一下即可
                subCategoryVoList.sort(Comparator.comparing(CategoryVo::getSortOrder).reversed());
                categoryVo.setSubCategories(subCategoryVoList);
                findSub(categoryList,subCategoryVoList);
            }
        }
    }

    private CategoryVo category2CategoryVo(Category category){
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }

    @Override
    public void findSubCategoryId(Integer id, Set<Integer> resultSet) {
        List<Category> categories = categoryMapper.selectAll();
        findSubCategoryId(id,resultSet,categories);
    }

    @Override
    public void findSubCategoryId(Integer id, Set<Integer> resultSet, List<Category> categories) {
        for (Category category : categories) {          //逐个拿出来对比，如果数据库的某商品的父类是当前查询的id，说明有子类，要继续递归下去
            if (category.getParentId().equals(id)){
                resultSet.add(category.getId());        //先把子类id添加进集合
                findSubCategoryId(category.getId(),resultSet,categories);
            }
        }
    }
}
