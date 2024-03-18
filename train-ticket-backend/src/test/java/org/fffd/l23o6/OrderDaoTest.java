package org.fffd.l23o6;


import org.fffd.l23o6.dao.OrderDao;
import org.fffd.l23o6.pojo.entity.OrderEntity;
import org.fffd.l23o6.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class OrderDaoTest {

    @Mock
    private OrderDao orderDao;


    public OrderDaoTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByUserId() {
        // 准备测试数据
        Long userId = 123L;
        List<OrderEntity> orders = new ArrayList<>();
        // 添加orderEntity到orders列表

        // 模拟orderDao的行为
        when(orderDao.findByUserId(userId)).thenReturn(orders);

        // 调用orderDao的方法
        List<OrderEntity> result = orderDao.findByUserId(userId);

        // 验证返回结果是否符合预期
        assertEquals(orders, result);

        // 验证orderDao的方法是否被调用
        verify(orderDao, times(1)).findByUserId(userId);
    }


}
