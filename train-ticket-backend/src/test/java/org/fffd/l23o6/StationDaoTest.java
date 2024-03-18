package org.fffd.l23o6;

import org.fffd.l23o6.dao.StationDao;
import org.fffd.l23o6.pojo.entity.StationEntity;
import org.fffd.l23o6.service.StationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class StationDaoTest {

    @Mock
    private StationDao stationDao;


    public StationDaoTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByName() {
        // 准备测试数据
        String name = "station1";
        StationEntity station = new StationEntity();
        // 设置station的属性

        // 模拟stationDao的行为
        when(stationDao.findByName(name)).thenReturn(station);

        // 调用stationDao的方法
        StationEntity result = stationDao.findByName(name);

        // 验证返回结果是否符合预期
        assertEquals(station, result);

        // 验证stationDao的方法是否被调用
        verify(stationDao, times(1)).findByName(name);
    }

    // 其他测试方法...

}
