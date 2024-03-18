package org.fffd.l23o6.util.strategy.payment;

import java.math.BigDecimal;

public class CreditPaymentStrategy extends PaymentStrategy{
    public String payment(BigDecimal amount, Long id){
        System.out.println("使用积分支付"+amount);
        return "支付成功";
    }
}
