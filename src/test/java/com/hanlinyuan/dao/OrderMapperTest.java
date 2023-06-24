package com.hanlinyuan.dao;

import com.hanlinyuan.pay_wechat.MallApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @Author: 翰林猿
 * @Description: TODO
 **/
//@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMapperTest extends MallApplicationTests {
    @Autowired
    private OrderMapper orderMapper;
    @Resource
    private DataSource database;
@Test
    public void JDBC() throws SQLException {
    System.out.println(database.getConnection());
}

    public void testDeleteByPrimaryKey() {
    }

    public void testInsert() {
    }

    public void testInsertSelective() {
    }
    @Test
    public void testSelectByPrimaryKey() {
        System.out.println(orderMapper.selectByPrimaryKey(1).toString());
    }

    public void testUpdateByPrimaryKeySelective() {
    }

    public void testUpdateByPrimaryKey() {
    }
}