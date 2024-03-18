package org.fffd.l23o6.util.strategy.payment;

import com.alipay.api.AlipayApiException;
import jakarta.servlet.ServletException;

import java.io.IOException;
import java.math.BigDecimal;

public abstract class PaymentStrategy {

    // TODO(solved): implement this by adding necessary methods and implement specified strategy
    public String payment(BigDecimal amount, Long id) throws AlipayApiException, ServletException, IOException {
        return null;
    }
    public String refund(BigDecimal amount, Long id) throws AlipayApiException, ServletException, IOException {
        return null;
    }
}
