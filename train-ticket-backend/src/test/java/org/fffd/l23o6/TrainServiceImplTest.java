package org.fffd.l23o6;

import org.fffd.l23o6.dao.RouteDao;
import org.fffd.l23o6.dao.TrainDao;
import org.fffd.l23o6.pojo.entity.RouteEntity;
import org.fffd.l23o6.pojo.entity.TrainEntity;
import org.fffd.l23o6.pojo.enum_.TrainType;
import org.fffd.l23o6.pojo.vo.train.AdminTrainVO;
import org.fffd.l23o6.pojo.vo.train.TrainDetailVO;
import org.fffd.l23o6.pojo.vo.train.TrainVO;
import org.fffd.l23o6.service.TrainService;
import org.fffd.l23o6.service.impl.TrainServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.util.*;

import static org.mockito.Mockito.*;

public class TrainServiceImplTest {

    @Mock
    private TrainDao trainDao;

    @Mock
    private RouteDao routeDao;

    @InjectMocks
    private TrainServiceImpl trainService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTrain() {
        Long trainId = 1L;
        Long routeId = 1L;
        String trainName = "Train 1";
        String trainDate = "2023-07-07";
        List<Long> stationIds = Arrays.asList(1L, 2L);
        List<Date> arrivalTimes = Arrays.asList(new Date(), new Date());
        List<Date> departureTimes = Arrays.asList(new Date(), new Date());
        List<String> extraInfos = Arrays.asList("Extra Info 1", "Extra Info 2");

        TrainEntity trainEntity = new TrainEntity();
        trainEntity.setId(trainId);
        trainEntity.setName(trainName);
        trainEntity.setRouteId(routeId);
        trainEntity.setDate(trainDate);
        trainEntity.setArrivalTimes(arrivalTimes);
        trainEntity.setDepartureTimes(departureTimes);
        trainEntity.setExtraInfos(extraInfos);

        RouteEntity routeEntity = new RouteEntity();
        routeEntity.setId(routeId);
        routeEntity.setStationIds(stationIds);

        when(trainDao.findById(trainId)).thenReturn(Optional.of(trainEntity));
        when(routeDao.findById(routeId)).thenReturn(Optional.of(routeEntity));

        TrainDetailVO trainDetailVO = trainService.getTrain(trainId);

        Assertions.assertEquals(trainId, trainDetailVO.getId());
        Assertions.assertEquals(trainName, trainDetailVO.getName());
        Assertions.assertEquals(trainDate, trainDetailVO.getDate());
        Assertions.assertEquals(stationIds, trainDetailVO.getStationIds());
        Assertions.assertEquals(arrivalTimes, trainDetailVO.getArrivalTimes());
        Assertions.assertEquals(departureTimes, trainDetailVO.getDepartureTimes());
        Assertions.assertEquals(extraInfos, trainDetailVO.getExtraInfos());

        verify(trainDao, times(1)).findById(trainId);
        verify(routeDao, times(1)).findById(routeId);
    }

    @Test
    public void testListTrains() {
        Long startStationId = 1L;
        Long endStationId = 2L;
        String date = "2023-07-07";

        RouteEntity routeEntity = new RouteEntity();
        routeEntity.setId(1L);
        routeEntity.setStationIds(Arrays.asList(1L, 2L));

        TrainEntity trainEntity = new TrainEntity();
        trainEntity.setId(1L);
        trainEntity.setName("Train 1");
        trainEntity.setRouteId(1L);
        trainEntity.setDate(date);
        trainEntity.setArrivalTimes(Arrays.asList(new Date(), new Date()));
        trainEntity.setDepartureTimes(Arrays.asList(new Date(), new Date()));
        trainEntity.setTrainType(TrainType.HIGH_SPEED);
        trainEntity.setSeats(new boolean[2][2]);

        when(routeDao.findAll()).thenReturn(Arrays.asList(routeEntity));
        when(trainDao.findByRouteIdAndDate(routeEntity.getId(), date)).thenReturn(Arrays.asList(trainEntity));

        List<TrainVO> trainVOs = trainService.listTrains(startStationId, endStationId, date);

        Assertions.assertEquals(1, trainVOs.size());
        TrainVO trainVO = trainVOs.get(0);
        Assertions.assertEquals(1L, trainVO.getId());
        Assertions.assertEquals("Train 1", trainVO.getName());
//        Assertions.assertEquals(date, trainVO.getDate());
//        Assertions.assertEquals(Arrays.asList(1L, 2L), trainVO.getStationIds());
//        Assertions.assertEquals(Arrays.asList(new Date(), new Date()), trainVO.getArrivalTimes());
//        Assertions.assertEquals(Arrays.asList(new Date(), new Date()), trainVO.getDepartureTimes());
//        Assertions.assertEquals(2, trainVO.getTicketInfo().size());

        verify(routeDao, times(1)).findAll();
        verify(trainDao, times(1)).findByRouteIdAndDate(routeEntity.getId(), date);
    }

    @Test
    public void testListTrainsAdmin() {
        TrainEntity train1 = new TrainEntity();
        train1.setId(1L);
        train1.setName("Train 1");

        TrainEntity train2 = new TrainEntity();
        train2.setId(2L);
        train2.setName("Train 2");

        when(trainDao.findAll(Sort.by(Sort.Direction.ASC, "name"))).thenReturn(Arrays.asList(train1, train2));

        List<AdminTrainVO> adminTrainVOs = trainService.listTrainsAdmin();

        Assertions.assertEquals(2, adminTrainVOs.size());
        Assertions.assertEquals(1L, adminTrainVOs.get(0).getId());
        Assertions.assertEquals("Train 1", adminTrainVOs.get(0).getName());
        Assertions.assertEquals(2L, adminTrainVOs.get(1).getId());
        Assertions.assertEquals("Train 2", adminTrainVOs.get(1).getName());

        verify(trainDao, times(1)).findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Test
    public void testAddTrain() {
        String trainName = "New Train";
        Long routeId = 1L;
        TrainType trainType = TrainType.HIGH_SPEED;
        String trainDate = "2023-07-07";
        List<Date> arrivalTimes = Arrays.asList(new Date(), new Date());
        List<Date> departureTimes = Arrays.asList(new Date(), new Date());

        RouteEntity routeEntity = new RouteEntity();
        routeEntity.setId(routeId);
        routeEntity.setStationIds(Arrays.asList(1L, 2L));

        when(routeDao.findById(routeId)).thenReturn(Optional.of(routeEntity));

        trainService.addTrain(trainName, routeId, trainType, trainDate, arrivalTimes, departureTimes);

        verify(routeDao, times(1)).findById(routeId);
        verify(trainDao, times(1)).save(any(TrainEntity.class));
    }

    @Test
    public void testDeleteTrain() {
        Long trainId = 1L;

        trainService.deleteTrain(trainId);

        verify(trainDao, times(1)).deleteById(trainId);
    }
}
