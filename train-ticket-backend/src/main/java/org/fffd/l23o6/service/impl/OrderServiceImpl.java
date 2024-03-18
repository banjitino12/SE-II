package org.fffd.l23o6.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.alipay.api.AlipayApiException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.ServletException;
import org.fffd.l23o6.dao.OrderDao;
import org.fffd.l23o6.dao.RouteDao;
import org.fffd.l23o6.dao.TrainDao;
import org.fffd.l23o6.dao.UserDao;
import org.fffd.l23o6.pojo.entity.UserEntity;
import org.fffd.l23o6.pojo.enum_.OrderStatus;
import org.fffd.l23o6.exception.BizError;
import org.fffd.l23o6.pojo.entity.OrderEntity;
import org.fffd.l23o6.pojo.entity.RouteEntity;
import org.fffd.l23o6.pojo.entity.TrainEntity;
import org.fffd.l23o6.pojo.vo.order.OrderVO;
import org.fffd.l23o6.service.OrderService;
import org.fffd.l23o6.util.strategy.payment.AlipayPaymentStrategy;
import org.fffd.l23o6.util.strategy.payment.CreditPaymentStrategy;
import org.fffd.l23o6.util.strategy.payment.PaymentService;
import org.fffd.l23o6.util.strategy.payment.WechatPaymentStrategy;
import org.fffd.l23o6.util.strategy.train.GSeriesSeatStrategy;
import org.fffd.l23o6.util.strategy.train.KSeriesSeatStrategy;
import org.springframework.stereotype.Service;

import io.github.lyc8503.spring.starter.incantation.exception.BizException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;
    private final UserDao userDao;
    private final TrainDao trainDao;
    private final RouteDao routeDao;

    public Long createOrder(String username, Long trainId, Long fromStationId, Long toStationId, String seatType,
            Long seatNumber,Integer price) throws AlipayApiException, ServletException, IOException {
        Long userId = userDao.findByUsername(username).getId();
        TrainEntity train = trainDao.findById(trainId).get();
        RouteEntity route = routeDao.findById(train.getRouteId()).get();
        int startStationIndex = route.getStationIds().indexOf(fromStationId);
        int endStationIndex = route.getStationIds().indexOf(toStationId);
        String seat = null;
        switch (train.getTrainType()) {
            case HIGH_SPEED:
                seat = GSeriesSeatStrategy.INSTANCE.allocSeat(startStationIndex, endStationIndex,
                        GSeriesSeatStrategy.GSeriesSeatType.fromString(seatType), train.getSeats());
                break;
            case NORMAL_SPEED:
                seat = KSeriesSeatStrategy.INSTANCE.allocSeat(startStationIndex, endStationIndex,
                        KSeriesSeatStrategy.KSeriesSeatType.fromString(seatType), train.getSeats());
                break;
        }
        if (seat == null) {
            throw new BizException(BizError.OUT_OF_SEAT);
        }
        OrderEntity order = OrderEntity.builder().trainId(trainId).userId(userId).seat(seat)
                .status(OrderStatus.PENDING_PAYMENT).arrivalStationId(toStationId).departureStationId(fromStationId)
                .price(price).discount(0).build();
        train.setUpdatedAt(null);// force it to update
        //PaymentService paymentService=new PaymentService();
        //paymentService.payment(new AlipayPaymentStrategy(), BigDecimal.valueOf(price));
        trainDao.save(train);
        orderDao.save(order);
        return order.getId();
    }

    public List<OrderVO> listOrders(String username) {
        Long userId = userDao.findByUsername(username).getId();
        List<OrderEntity> orders = orderDao.findByUserId(userId);
        orders.sort((o1,o2)-> o2.getId().compareTo(o1.getId()));
        return orders.stream().map(order -> {
            TrainEntity train = trainDao.findById(order.getTrainId()).get();
            RouteEntity route = routeDao.findById(train.getRouteId()).get();
            int startIndex = route.getStationIds().indexOf(order.getDepartureStationId());
            int endIndex = route.getStationIds().indexOf(order.getArrivalStationId());
            return OrderVO.builder().id(order.getId()).trainId(order.getTrainId())
                    .seat(order.getSeat()).status(order.getStatus().getText())
                    .createdAt(order.getCreatedAt())
                    .startStationId(order.getDepartureStationId())
                    .endStationId(order.getArrivalStationId())
                    .departureTime(train.getDepartureTimes().get(startIndex))
                    .arrivalTime(train.getArrivalTimes().get(endIndex))
                    .build();
        }).collect(Collectors.toList());
    }

    public OrderVO getOrder(Long id) {
        OrderEntity order = orderDao.findById(id).get();
        TrainEntity train = trainDao.findById(order.getTrainId()).get();
        RouteEntity route = routeDao.findById(train.getRouteId()).get();
        int startIndex = route.getStationIds().indexOf(order.getDepartureStationId());
        int endIndex = route.getStationIds().indexOf(order.getArrivalStationId());
        return OrderVO.builder().id(order.getId()).trainId(order.getTrainId())
                .seat(order.getSeat()).status(order.getStatus().getText())
                .createdAt(order.getCreatedAt())
                .startStationId(order.getDepartureStationId())
                .endStationId(order.getArrivalStationId())
                .departureTime(train.getDepartureTimes().get(startIndex))
                .arrivalTime(train.getArrivalTimes().get(endIndex))
                .price(order.getPrice())
                .build();
    }

    public void cancelOrder(Long id) throws ServletException, AlipayApiException, IOException, ParseException {
        //TODO(solved): add seats after cancel order
        OrderEntity order = orderDao.findById(id).get();
        TrainEntity train = trainDao.findById(order.getTrainId()).get();
        RouteEntity route = routeDao.findById(train.getRouteId()).get();
        int startStationIndex = route.getStationIds().indexOf(order.getDepartureStationId());
        int endStationIndex = route.getStationIds().indexOf(order.getArrivalStationId());
        String seat = order.getSeat();

        int seatNumber = -1;
        switch (train.getTrainType()) {
            case HIGH_SPEED:
                seatNumber = GSeriesSeatStrategy.INSTANCE.findSeatNumberByInfo(seat);
                break;
            case NORMAL_SPEED:
                seatNumber = KSeriesSeatStrategy.INSTANCE.findSeatNumberByInfo(seat);
                break;
        }


        boolean[][] seatMap = train.getSeats();
        for (int i = startStationIndex; i < endStationIndex; i++) {
            boolean[] seats = seatMap[i];
            seats[seatNumber] = false;
        }


        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new BizException(BizError.ILLEAGAL_ORDER_STATUS);
        }

        // TODO(solved): refund user's money and credits if needed
        SimpleDateFormat ft=new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate=new Date();

        if(order.getStatus() == OrderStatus.COMPLETED ){
            long nd=1000*24*60*60;

            Date departeDate=ft.parse(train.getDate());

            long diff=departeDate.getTime() - nowDate.getTime();
            long day=diff/nd;
            double rate;
            if(day>=8) rate=1;
            else if(day>=2) rate=0.95;
            else if(day>=1) rate=0.9;
            else rate=0.8;

            Integer price=order.getPrice();
            PaymentService paymentService=new PaymentService();
            paymentService.refund(new AlipayPaymentStrategy(), BigDecimal.valueOf(price*rate), id);
            Long userId=order.getUserId();
            UserEntity userEntity=userDao.findById(userId).get();
            userEntity.setMileagePoints(userEntity.getMileagePoints()+discountToPoints(order.getDiscount())-price*5L);
            userDao.save(userEntity);
        }

        train.setUpdatedAt(null);// force it to update
        order.setStatus(OrderStatus.CANCELLED);
        orderDao.save(order);
        trainDao.save(train);

    }

    public String payOrder(Long id, String type) throws AlipayApiException, ServletException, IOException {
        OrderEntity order = orderDao.findById(id).get();
        Integer price=order.getPrice();

        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new BizException(BizError.ILLEAGAL_ORDER_STATUS);
        }

        //TODO:支付完再更新
        PaymentService paymentService=new PaymentService();
        switch (type){
            case "支付宝支付":
                String pay = paymentService.payment(new AlipayPaymentStrategy(), BigDecimal.valueOf(price), id);

                Long userId=order.getUserId();
                UserEntity userEntity=userDao.findById(userId).get();
                userEntity.setMileagePoints(userEntity.getMileagePoints()-discountToPoints(order.getDiscount())+price*5L);
                userDao.save(userEntity);
                order.setStatus(OrderStatus.COMPLETED);
                orderDao.save(order);

                return pay;
            case "微信支付":
                paymentService.payment(new WechatPaymentStrategy(), BigDecimal.valueOf(price), id);

                Long userId1=order.getUserId();
                UserEntity userEntity1=userDao.findById(userId1).get();
                userEntity1.setMileagePoints(userEntity1.getMileagePoints()-discountToPoints(order.getDiscount())+price*5L);
                userDao.save(userEntity1);
                order.setStatus(OrderStatus.COMPLETED);
                orderDao.save(order);
                break;
            case "积分支付":
                paymentService.payment(new CreditPaymentStrategy(), BigDecimal.valueOf(price), id);
                break;
        }

        return null;
    }

    public Integer cancelUsePoints(Long orderId){
        OrderEntity orderEntity=orderDao.findById(orderId).get();
        int newPrice=orderEntity.getPrice()+orderEntity.getDiscount();
        orderEntity.setPrice(newPrice);
        orderEntity.setDiscount(0);
        orderDao.save(orderEntity);
        return newPrice;
    }

    public Integer usePoints(Long orderId){
        OrderEntity orderEntity=orderDao.findById(orderId).get();
        Long userId = orderEntity.getUserId();
        int price= orderEntity.getPrice();
        UserEntity userEntity=userDao.findById(userId).get();
        Long points=userEntity.getMileagePoints();
        int discount=pointsToDiscount(points);
        int newPrice;
        if(price>=discount){
            newPrice=price-discount;
        }
        else {
            newPrice=0;
            discount=price;
        }
        orderEntity.setPrice(newPrice);
        orderEntity.setDiscount(discount);
        orderDao.save(orderEntity);
        return newPrice;
    }

    public boolean checkIllegal(Long id){
        OrderEntity order=orderDao.findById(id).get();
        SimpleDateFormat ft=new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate=new Date();
        int cancelCount=0;
        int buyCount=0;
        List<OrderEntity> myOrders=orderDao.findByUserId(order.getUserId());
        String today=ft.format(nowDate);
        for(OrderEntity orderEntity:myOrders){
            String updateDate=ft.format(new Date(orderEntity.getUpdatedAt().getTime()));
            if(updateDate.equals(today)){
                if(orderEntity.getStatus()==OrderStatus.CANCELLED) cancelCount++;
                else if(orderEntity.getStatus()==OrderStatus.COMPLETED) buyCount++;
            }
        }
        System.out.println(cancelCount);

        if(cancelCount>3||buyCount>5){
            UserEntity userEntity=userDao.findById(order.getUserId()).get();
            Long mileagePoints = userEntity.getMileagePoints();
            if(mileagePoints>=100) userEntity.setMileagePoints(userEntity.getMileagePoints()-100);
            else userEntity.setMileagePoints(0L);
            userDao.save(userEntity);

            return true;
        }

        return false;
    }

    private Long discountToPoints(Integer discount){
        int[] creditArray={50000,10000,3000,1000,0};
        double[] discountRate={0.3,0.25,0.2,0.15,0.1};
        double[] fixedDiscount={118,18,4,1,0};
        for(int i=0;i< fixedDiscount.length;i++){
            if(discount>fixedDiscount[i]){
                double points=(discount-fixedDiscount[i])/(discountRate[i]*0.01)+creditArray[i];
                return Math.round(points);
            }
        }
        return 0L;
    }

    private Integer pointsToDiscount(Long points){
        int[] creditArray={50000,10000,3000,1000,0};
        double[] discountRate={0.3,0.25,0.2,0.15,0.1};
        double[] fixedDiscount={118,18,4,1,0};
        for(int i=0;i<creditArray.length;i++){
            if(points>creditArray[i]){
                double discount=fixedDiscount[i]+(points-creditArray[i])*discountRate[i]*0.01;
                return (int)Math.round(discount);
            }
        }
        return 0;
    }

    public void updateOrderAfterPaymentSuccess(Long orderId) {
        // 获取订单信息
        OrderEntity order = orderDao.findById(orderId).get();
        Integer price = order.getPrice();
        Long userId = order.getUserId();
        UserEntity userEntity = userDao.findById(userId).get();
        userEntity.setMileagePoints(userEntity.getMileagePoints() - discountToPoints(order.getDiscount()) + price * 5L);
        userDao.save(userEntity);
        order.setStatus(OrderStatus.COMPLETED);
        orderDao.save(order);
    }

}
