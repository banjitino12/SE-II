package org.fffd.l23o6.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.alipay.api.AlipayApiException;
import jakarta.servlet.ServletException;
import org.fffd.l23o6.pojo.vo.order.OrderVO;

public interface OrderService {
    Long createOrder(String username, Long trainId, Long fromStationId, Long toStationId, String seatType, Long seatNumber,Integer price) throws AlipayApiException, ServletException, IOException;
    List<OrderVO> listOrders(String username);
    OrderVO getOrder(Long id);

    void cancelOrder(Long id) throws ServletException, AlipayApiException, IOException, ParseException;
    String payOrder(Long id, String type) throws AlipayApiException, ServletException, IOException;
    Integer usePoints(Long orderId);
    Integer cancelUsePoints(Long orderId);
    void updateOrderAfterPaymentSuccess(Long id);
    boolean checkIllegal(Long id);
}
