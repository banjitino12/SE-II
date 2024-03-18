package org.fffd.l23o6;


import org.fffd.l23o6.dao.TrainDao;
import org.fffd.l23o6.pojo.entity.TrainEntity;
import org.fffd.l23o6.service.TrainService;
import org.fffd.l23o6.service.impl.TrainServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TrainDaoTest {

    @Mock
    private TrainDao trainDao;

    @InjectMocks
    private TrainServiceImpl trainService;

    public TrainDaoTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByDate() {
        // 准备测试数据
        String date = "2023-07-07";
        List<TrainEntity> trains = new ArrayList<>();
        // 添加trainEntity到trains列表

        // 模拟trainDao的行为
        when(trainDao.findByDate(date)).thenReturn(trains);

        // 调用trainDao的方法
        List<TrainEntity> result = trainDao.findByDate(date);

        // 验证返回结果是否符合预期
        assertEquals(trains, result);

        // 验证trainDao的方法是否被调用
        verify(trainDao, times(1)).findByDate(date);
    }

    @Test
    public void testFindByName() {
        // 准备测试数据
        String name = "train1";
        TrainEntity train = new TrainEntity();
        // 设置train的属性

        // 模拟trainDao的行为
        when(trainDao.findByName(name)).thenReturn(train);

        // 调用trainDao的方法
        TrainEntity result = trainDao.findByName(name);

        // 验证返回结果是否符合预期
        assertEquals(train, result);

        // 验证trainDao的方法是否被调用
        verify(trainDao, times(1)).findByName(name);
    }

    @Test
    public void testFindByRouteId() {
        // 准备测试数据
        Long routeId = 123L;
        List<TrainEntity> trains = new ArrayList<>();
        // 添加trainEntity到trains列表

        // 模拟trainDao的行为
        when(trainDao.findByRouteId(routeId)).thenReturn(trains);

        // 调用trainDao的方法
        List<TrainEntity> result = trainDao.findByRouteId(routeId);

        // 验证返回结果是否符合预期
        assertEquals(trains, result);

        // 验证trainDao的方法是否被调用
        verify(trainDao, times(1)).findByRouteId(routeId);
    }

    @Test
    public void testFindByRouteIdAndDate() {
        // 准备测试数据
        Long routeId = 123L;
        String date = "2023-07-07";
        List<TrainEntity> trains = new ArrayList<>();
        // 添加trainEntity到trains列表

        // 模拟trainDao的行为
        when(trainDao.findByRouteIdAndDate(routeId, date)).thenReturn(trains);

        // 调用trainDao的方法
        List<TrainEntity> result = trainDao.findByRouteIdAndDate(routeId, date);

        // 验证返回结果是否符合预期
        assertEquals(trains, result);

        // 验证trainDao的方法是否被调用
        verify(trainDao, times(1)).findByRouteIdAndDate(routeId, date);
    }


}
