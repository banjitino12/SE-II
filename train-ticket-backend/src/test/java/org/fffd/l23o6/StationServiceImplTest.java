package org.fffd.l23o6;


import io.github.lyc8503.spring.starter.incantation.exception.BizException;
import org.fffd.l23o6.dao.RouteDao;
import org.fffd.l23o6.dao.StationDao;
import org.fffd.l23o6.dao.TrainDao;
import org.fffd.l23o6.exception.BizError;
import org.fffd.l23o6.pojo.entity.RouteEntity;
import org.fffd.l23o6.pojo.entity.StationEntity;
import org.fffd.l23o6.pojo.entity.TrainEntity;
import org.fffd.l23o6.pojo.vo.station.StationVO;
import org.fffd.l23o6.service.StationService;
import org.fffd.l23o6.service.impl.StationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class StationServiceImplTest {

    @Mock
    private StationDao stationDao;

    @Mock
    private RouteDao routeDao;

    @Mock
    private TrainDao trainDao;

    @InjectMocks
    private StationServiceImpl stationService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetStation() {
        Long stationId = 1L;
        StationEntity stationEntity = new StationEntity();
        stationEntity.setId(stationId);
        stationEntity.setName("Station 1");

        when(stationDao.findById(stationId)).thenReturn(Optional.of(stationEntity));

        StationVO stationVO = stationService.getStation(stationId);

        Assertions.assertEquals(stationId, stationVO.getId());
        Assertions.assertEquals("Station 1", stationVO.getName());

        verify(stationDao, times(1)).findById(stationId);
    }

    @Test
    public void testListStations() {
        StationEntity station1 = new StationEntity();
        station1.setId(1L);
        station1.setName("Station 1");

        StationEntity station2 = new StationEntity();
        station2.setId(2L);
        station2.setName("Station 2");

        when(stationDao.findAll(Sort.by(Sort.Direction.ASC, "name"))).thenReturn(Arrays.asList(station1, station2));

        List<StationVO> stationVOs = stationService.listStations();

        Assertions.assertEquals(2, stationVOs.size());
        Assertions.assertEquals(1L, stationVOs.get(0).getId());
        Assertions.assertEquals("Station 1", stationVOs.get(0).getName());
        Assertions.assertEquals(2L, stationVOs.get(1).getId());
        Assertions.assertEquals("Station 2", stationVOs.get(1).getName());

        verify(stationDao, times(1)).findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Test
    public void testAddStation() {
        String stationName = "New Station";

        when(stationDao.findByName(stationName)).thenReturn(null);

        stationService.addStation(stationName);

        verify(stationDao, times(1)).findByName(stationName);
        verify(stationDao, times(1)).save(any(StationEntity.class));
    }

    @Test
    public void testAddStation_StationNameExists() {
        String stationName = "Existing Station";
        StationEntity existingStation = new StationEntity();
        existingStation.setId(1L);
        existingStation.setName(stationName);

        when(stationDao.findByName(stationName)).thenReturn(existingStation);

        Assertions.assertThrows(BizException.class, () -> stationService.addStation(stationName));

        verify(stationDao, times(1)).findByName(stationName);
        verify(stationDao, never()).save(any(StationEntity.class));
    }

    @Test
    public void testEditStation() {
        Long stationId = 1L;
        String newName = "Updated Station";

        StationEntity stationEntity = new StationEntity();
        stationEntity.setId(stationId);
        stationEntity.setName("Old Station");

        when(stationDao.findById(stationId)).thenReturn(Optional.of(stationEntity));
        when(stationDao.save(stationEntity)).thenReturn(stationEntity);

        stationService.editStation(stationId, newName);

        Assertions.assertEquals(newName, stationEntity.getName());

        verify(stationDao, times(1)).findById(stationId);
        verify(stationDao, times(1)).save(stationEntity);
    }

    @Test
    public void testDeleteStation() {
        Long stationId = 1L;

        StationEntity stationEntity = new StationEntity();
        stationEntity.setId(stationId);
        stationEntity.setName("Station to Delete");

        RouteEntity routeEntity = new RouteEntity();
        routeEntity.setId(1L);
        routeEntity.setStationIds(Arrays.asList(1L, 2L));

        TrainEntity trainEntity1 = new TrainEntity();
        trainEntity1.setId(1L);
        trainEntity1.setRouteId(1L);

        TrainEntity trainEntity2 = new TrainEntity();
        trainEntity2.setId(2L);
        trainEntity2.setRouteId(1L);

        when(stationDao.findById(stationId)).thenReturn(Optional.of(stationEntity));
        when(routeDao.findAll()).thenReturn(Arrays.asList(routeEntity));
        when(trainDao.findByRouteId(routeEntity.getId())).thenReturn(Arrays.asList(trainEntity1, trainEntity2));

        stationService.deleteStation(stationId);

        verify(stationDao, times(1)).findById(stationId);
        verify(stationDao, times(1)).delete(stationEntity);
        verify(routeDao, times(1)).delete(routeEntity);
        verify(trainDao, times(1)).deleteAll(Arrays.asList(trainEntity1, trainEntity2));
    }
}
