package org.fffd.l23o6;


import org.fffd.l23o6.dao.RouteDao;
import org.fffd.l23o6.dao.TrainDao;
import org.fffd.l23o6.pojo.entity.RouteEntity;
import org.fffd.l23o6.pojo.entity.TrainEntity;
import org.fffd.l23o6.pojo.enum_.TrainType;
import org.fffd.l23o6.pojo.vo.route.RouteVO;
import org.fffd.l23o6.service.impl.RouteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RouteServiceImplTest {

    private RouteServiceImpl routeService;

    @Mock
    private RouteDao routeDao;

    @Mock
    private TrainDao trainDao;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        routeService = new RouteServiceImpl(routeDao, trainDao);
    }

//    @Test
//    public void testAddRoute() {
//        // 设置测试数据
//        String name = "Route 1";
//        List<Long> stationIds = List.of(1L, 2L, 3L);
//        Date createdAt = new Date();
//        Date updatedAt = new Date();
//
//        // 调用方法
//        routeService.addRoute(name, stationIds);
//
//        // 验证是否调用了routeDao的save方法，并检查保存的RouteEntity是否符合预期
//        verify(routeDao, times(1)).save(argThat(route -> {
//            assertEquals(name, route.getName());
//            assertEquals(stationIds, route.getStationIds());
//            assertEquals(createdAt, route.getCreatedAt());
//            assertEquals(updatedAt, route.getUpdatedAt());
//            return true;
//        }));
//    }

    @Test
    public void testListRoutes() {
        // 设置测试数据
        List<RouteEntity> routeEntities = new ArrayList<>();
        routeEntities.add(new RouteEntity(1L, "Route 1", List.of(1L, 2L, 3L), new Date(), new Date()));
        routeEntities.add(new RouteEntity(2L, "Route 2", List.of(4L, 5L, 6L), new Date(), new Date()));

        // 设置routeDao的行为
        when(routeDao.findAll(Sort.by(Sort.Direction.ASC, "name"))).thenReturn(routeEntities);

        // 调用方法
        List<RouteVO> result = routeService.listRoutes();

        // 验证返回结果是否正确
        assertEquals(2, result.size());
        assertEquals("Route 1", result.get(0).getName());
        assertEquals("Route 2", result.get(1).getName());
    }

    @Test
    public void testGetRoute() {
        // 设置测试数据
        Long routeId = 1L;
        RouteEntity routeEntity = new RouteEntity(routeId, "Route 1", List.of(1L, 2L, 3L), new Date(), new Date());

        // 设置routeDao的行为
        when(routeDao.findById(routeId)).thenReturn(Optional.of(routeEntity));

        // 调用方法
        RouteVO result = routeService.getRoute(routeId);

        // 验证返回结果是否正确
        assertEquals("Route 1", result.getName());
        assertEquals(3, result.getStationIds().size());
    }

    @Test
    public void testEditRoute() {
        // 设置测试数据
        Long routeId = 1L;
        String newName = "New Route";
        List<Long> newStationIds = List.of(4L, 5L, 6L);
        Date updatedAt = new Date();

        // 设置routeDao的行为
        RouteEntity routeEntity = new RouteEntity(routeId, "Route 1", List.of(1L, 2L, 3L), new Date(), new Date());
        when(routeDao.findById(routeId)).thenReturn(Optional.of(routeEntity));

        // 调用方法
        routeService.editRoute(routeId, newName, newStationIds);

        // 验证是否调用了routeDao的save方法，并检查保存的RouteEntity是否符合预期
        verify(routeDao, times(1)).save(argThat(route -> {
            assertEquals(routeId, route.getId());
            assertEquals(newName, route.getName());
            assertEquals(newStationIds, route.getStationIds());
            assertEquals(updatedAt, route.getUpdatedAt());
            return true;
        }));
    }

    @Test
    public void testDeleteRoute() {
        // 设置测试数据
        Long routeId = 1L;
        List<TrainEntity> trainEntities = List.of(
                new TrainEntity(1L, "Train 1", routeId, new boolean[0][0], TrainType.HIGH_SPEED,
                        "2023-07-07", List.of(new Date()), List.of(new Date()), List.of("info"),new Date(),new Date()),
                new TrainEntity(2L, "Train 2", routeId, new boolean[0][0], TrainType.HIGH_SPEED,
                        "2023-07-07", List.of(new Date()), List.of(new Date()), List.of("info"),new Date(),new Date())
        );
        RouteEntity routeEntity = new RouteEntity(routeId, "Route 1", List.of(1L, 2L, 3L), new Date(), new Date());

        // 设置trainDao的行为
        when(trainDao.findByRouteId(routeId)).thenReturn(trainEntities);

        // 设置routeDao的行为
        when(routeDao.findById(routeId)).thenReturn(Optional.of(routeEntity));

        // 调用方法
        routeService.deleteRoute(routeId);

        // 验证是否调用了trainDao的deleteAll方法和routeDao的delete方法
        verify(trainDao, times(1)).deleteAll(trainEntities);
        verify(routeDao, times(1)).delete(routeEntity);
    }
}
