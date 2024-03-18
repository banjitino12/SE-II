package org.fffd.l23o6.mapper;

import org.fffd.l23o6.dao.RouteDao;
import org.fffd.l23o6.pojo.entity.TrainEntity;
import org.fffd.l23o6.pojo.entity.RouteEntity;
import org.fffd.l23o6.pojo.vo.train.AdminTrainVO;
import org.fffd.l23o6.pojo.vo.train.TrainVO;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Date;
import java.util.List;

@Mapper
@Service
public interface TrainMapper {
    TrainMapper INSTANCE = Mappers.getMapper(TrainMapper.class);

    @Mapping(source = "TrainEntity.trainType.text", target = "trainType")
    AdminTrainVO toAdminTrainVO(TrainEntity TrainEntity);

    @Mapping(source = "TrainEntity.trainType.text", target = "trainType")
    @Mapping(source = "startStationId", target = "startStationId")
    @Mapping(source = "endStationId", target = "endStationId")
    @Mapping(target = "TrainEntity.ticketInfo", ignore = true)
    TrainVO toTrainVO(TrainEntity TrainEntity, Long startStationId, Long endStationId);

}

