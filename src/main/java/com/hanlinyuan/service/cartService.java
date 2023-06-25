package com.hanlinyuan.service;

import com.google.gson.Gson;
import com.hanlinyuan.Enum.ProductStatusEnum;
import com.hanlinyuan.Enum.ResponseEnum;
import com.hanlinyuan.dao.ProductMapper;
import com.hanlinyuan.form.CartAddForm;
import com.hanlinyuan.form.CartUpdateForm;
import com.hanlinyuan.pojo.Cart;
import com.hanlinyuan.pojo.Product;
import com.hanlinyuan.service.Imp.impCartService;
import com.hanlinyuan.vo.CartProductVo;
import com.hanlinyuan.vo.CartVo;
import com.hanlinyuan.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: 翰林猿
 * @Description:
 **/
@Service
public class cartService implements impCartService {
    private final static String CART_REDIS_KEY = "cart_%d";
    @Autowired
    private StringRedisTemplate redisTemplate;      //一个封装好的redis使用类
    @Resource
    private ProductMapper productMapper;
    Gson gson = new Gson();
    @Override
    public ResponseVo<CartVo> add(Integer uid , CartAddForm cartAddForm) {
        Integer quantity =1;
        //添加商品前先验证是否存在这个商品
        Product product = productMapper.selectByPrimaryKey(cartAddForm.getProductId());

        if (product == null) {
            return ResponseVo.error(ResponseEnum.PRODUCT_NOT_EXIST);
        }
        //是否在售
        if (!product.getStatus().equals(ProductStatusEnum.ON_SALE.getCode())) {
            return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE);
        }
        //库存是否充足
        if (product.getStock() <= 0) {
            return ResponseVo.error(ResponseEnum.PROODUCT_STOCK_ERROR);
        }

        //都没有问题，就写入到redis里
        //我们希望K是以这个模式:cart_uid,规范起见改成一个常量CART_REDIS_KEY
        //V的话需要前端传入几个，其他的从数据库里拿，保证数据是最新的,V注意是string类型，所以我们把他转成json格式也可以


        //使用redis的Hash（相当于Java的Map）格式，是一种K HashK HashV 的模式，这样就可以变成一个高性能的购物车。HashOperations 就是Java中对redis的hash操作的接口
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();

        String redisKey = String.format(CART_REDIS_KEY, uid);   //把cart_%d作为K
        String productIdKey = String.valueOf(product.getId());  //把productId作为HK
        String value = opsForHash.get(redisKey,productIdKey);   //cart对象的json格式就是HV

        Cart cart = null;

        if(StringUtils.isEmpty(value)){     //没有HV=没有该商品,new一个出来再put进去
            cart = new Cart(product.getId(), quantity, cartAddForm.getSelected());
        }else {                             //购物车里已经有了，那就从数据库里取出来（从json转成cart类），给cart的数量加1再放回去
            cart = gson.fromJson(value, Cart.class);
            cart.setQuantity(cart.getQuantity()+quantity);
        }

        opsForHash.put(redisKey,productIdKey,gson.toJson(cart));
        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> list(Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY, uid);   //把cart_%d作为K

        boolean selectAll = true;       //全选
        Integer totalQuantity = 0;
        BigDecimal cartTotalPrice = BigDecimal.ZERO;

        CartVo cartVo = new CartVo();
        List<CartProductVo> cartProductVoList = new ArrayList<>();
        Map<String, String> entries = opsForHash.entries(redisKey);//获取 Redis 哈希表中指定键的所有字段和对应值
        for (Map.Entry<String, String> entry: entries.entrySet()) {//遍历
            Integer productId = Integer.valueOf(entry.getKey());
            Cart cart = gson.fromJson(entry.getValue(), Cart.class);

            // TODO 需要优化，尽量不要在for循环中查sql，使用mysql的in关键字。
            Product product = productMapper.selectByPrimaryKey(productId);
            if (product != null){       //如果商品存在
                CartProductVo cartProductVo = new CartProductVo(productId,
                        cart.getQuantity(),
                        product.getName(),
                        product.getSubtitle(),
                        product.getMainImage(),
                        product.getPrice(),
                        product.getStatus(),
                        product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())),//价格要乘以数量，注意类型要相同。
                        product.getStock(),
                        cart.getProductSelected()
                        );
                cartProductVoList.add(cartProductVo);       //加入list里

                if (!cart.getProductSelected()){            //如果没有选中，就改为false代表没有全选
                    selectAll = false;
                }
                //计算选中的总价
                if (cart.getProductSelected()){
                    cartTotalPrice = cartTotalPrice.add(cartProductVo.getProductTotalPrice());
                }
            }
            totalQuantity += cart.getQuantity();
        }
        cartVo.setSelectedAll(selectAll);
        cartVo.setCartTotalQuantity(totalQuantity);
        cartVo.setCartTotalPrice(cartTotalPrice);

        cartVo.setCartProductVoList(cartProductVoList);     //最后把list放进cartvo类
        return ResponseVo.success(cartVo);
    }

    @Override
    public ResponseVo<CartVo> update(Integer uid, Integer productId, CartUpdateForm form) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey  = String.format(CART_REDIS_KEY, uid);

        String value = opsForHash.get(redisKey, String.valueOf(productId));
        if (StringUtils.isEmpty(value)) {
            //没有该商品, 报错
            return ResponseVo.error(ResponseEnum.CART_PRODUCT_NOT_EXIST);
        }

        //已经有了，修改内容
        Cart cart = gson.fromJson(value, Cart.class);
        if (form.getQuantity() != null              //如果数量不为空且大于0才能进行修改，如果数量为空就不能修改
                && form.getQuantity() >= 0) {
            cart.setQuantity(form.getQuantity());
        }
        if (form.getSelected() != null) {           //如果是否选中参数本身为null的话就不能修改
            cart.setProductSelected(form.getSelected());
        }

        opsForHash.put(redisKey, String.valueOf(productId), gson.toJson(cart));
        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> delete(Integer uid, Integer productId) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey  = String.format(CART_REDIS_KEY, uid);

        String value = opsForHash.get(redisKey, String.valueOf(productId));
        if (StringUtils.isEmpty(value)) {
            //没有该商品, 报错
            return ResponseVo.error(ResponseEnum.CART_PRODUCT_NOT_EXIST);
        }

        opsForHash.delete(redisKey, String.valueOf(productId));
        return list(uid);
    }
}
