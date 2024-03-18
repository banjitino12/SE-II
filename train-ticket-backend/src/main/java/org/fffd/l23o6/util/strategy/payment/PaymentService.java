package org.fffd.l23o6.util.strategy.payment;

import com.alipay.api.AlipayApiException;
import jakarta.servlet.ServletException;
import org.fffd.l23o6.util.strategy.payment.PaymentStrategy;

import java.io.IOException;
import java.math.BigDecimal;

public class PaymentService {
    public String payment(PaymentStrategy strategy, BigDecimal amount, Long id) throws AlipayApiException, ServletException, IOException {
        return strategy.payment(amount, id);
    }
    public String refund(PaymentStrategy strategy, BigDecimal amount, Long id) throws AlipayApiException, ServletException, IOException {
        return strategy.refund(amount, id);
    }
}
