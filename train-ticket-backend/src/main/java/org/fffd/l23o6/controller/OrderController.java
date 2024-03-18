package org.fffd.l23o6.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import io.github.lyc8503.spring.starter.incantation.exception.BizException;
import io.github.lyc8503.spring.starter.incantation.exception.CommonErrorType;
import io.github.lyc8503.spring.starter.incantation.pojo.CommonResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fffd.l23o6.pojo.vo.order.CreateOrderRequest;
import org.fffd.l23o6.pojo.vo.order.OrderIdVO;
import org.fffd.l23o6.pojo.vo.order.OrderVO;
import org.fffd.l23o6.pojo.vo.order.PatchOrderRequest;
import org.fffd.l23o6.service.OrderService;
import org.springframework.web.bind.annotation.*;

import cn.dev33.satoken.stp.StpUtil;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/v1/")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("order")
    public CommonResponse<OrderIdVO> createOrder(@Valid @RequestBody CreateOrderRequest request) throws AlipayApiException, ServletException, IOException {
        StpUtil.checkLogin();
        return CommonResponse.success(new OrderIdVO(orderService.createOrder(StpUtil.getLoginIdAsString(), request.getTrainId(), request.getStartStationId(), request.getEndStationId(), request.getSeatType(), null,request.getPrice())));
    }


    @GetMapping("order")
    public CommonResponse<List<OrderVO>> listOrders(){
        StpUtil.checkLogin();
        return CommonResponse.success(orderService.listOrders(StpUtil.getLoginIdAsString()));
    }

    @GetMapping("order/{orderId}")
    public CommonResponse<OrderVO> getOrder(@PathVariable("orderId") Long orderId) {
        return CommonResponse.success(orderService.getOrder(orderId));
    }

    @PatchMapping("order/{orderId}")
    public CommonResponse<String> patchOrder(@PathVariable("orderId") Long orderId, @Valid @RequestBody PatchOrderRequest request) throws AlipayApiException, ServletException, IOException, ParseException {

        switch (request.getStatus()) {
            case PAID:
                return CommonResponse.success(orderService.payOrder(orderId,request.getType()));
            case CANCELLED:
                orderService.cancelOrder(orderId);
                break;
            default:
                throw new BizException(CommonErrorType.ILLEGAL_ARGUMENTS, "Invalid order status.");
        }

        return CommonResponse.success();
    }



    @GetMapping("order/usePoints/{orderId}")
    public CommonResponse<Integer> usePoints(@PathVariable("orderId") Long orderId){
        return CommonResponse.success(orderService.usePoints(orderId));
    }

    @GetMapping("order/cancelUsePoints/{orderId}")
    public CommonResponse<Integer> cancelUsePoints(@PathVariable("orderId") Long orderId){
        return CommonResponse.success(orderService.cancelUsePoints(orderId));
    }


    @PostMapping("order/check/{orderId}")
    public CommonResponse<Boolean> checkIllegal(@PathVariable("orderId") Long orderId){
        return CommonResponse.success(orderService.checkIllegal(orderId));
    }
}