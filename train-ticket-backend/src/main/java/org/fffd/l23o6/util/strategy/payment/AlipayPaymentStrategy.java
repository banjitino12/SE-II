package org.fffd.l23o6.util.strategy.payment;


import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import jakarta.servlet.ServletException;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.Random;

@Component
public class AlipayPaymentStrategy extends PaymentStrategy{

    public String payment(BigDecimal amount, Long id) throws ServletException, IOException {
        System.out.println("使用支付宝支付"+amount);
        doPost(amount,id);
        return doPost(amount,id);
    }

    public String doPost(BigDecimal amount,Long id){
        //System.out.println(id);
        /** 支付宝网关 **/
        String URL = " https://openapi-sandbox.dl.alipaydev.com/gateway.do";

        /** 应用id，如何获取请参考：https://opensupport.alipay.com/support/helpcenter/190/201602493024 **/
        String APP_ID = "9021000122698301";

        /** 应用私钥，如何获取请参考：https://opensupport.alipay.com/support/helpcenter/207/201602471154?ant_source=antsupport **/
        String APP_PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCSqov7OsUFoFM0ZhbaYi6xXyHYamJoibJdpvSTnTv8S25pDMLkP9cMXfWOM9TL/NiGbAFsqhqTPiHmdc7Hr0ugz5+sd/mkZzod9wY6kZjgShKNsbIE4AbGspnDUZqewp1wI3N+dT2V1399QpDzSU8/2Wajz1Tl/E5W1EQvRKLEA57etncxRzDRULr61NsCJ+XsrbmNxNm9jj/sbDF5anHONxy6adn/8bSmgHv2gRad2jZ5i++tWPr3kNyS2D6A86455y8GUrTHCSLOAvkCww/y/qq5E4TRVF4dKrKubdxXlRxFkfTsNznJzeKahxpCLFrF/yrmzppfJ6NMUfMtIdzVAgMBAAECggEAYspd36uGanPDl9fsLM1VhkP+GDgQcTnO2yZKN+QBM7/bbwcZf4VA5SOk81QWUhDOCD1X+Enb/LItQ362+eoplvhDJXjqvsKJcwWhPHI1913tgWPf9UixR8WlrNqau7V7Nr2qXdO61+OADG96E2Wd6/QUHuLY3NFGM1ZnVy90M3L3OyxUkNAgb5i/h3g80HJDMQ8sNlh+44XuUkjMRRVvpi/bQIAYcvDOohV/hRl4TFq/WqAK2s8ZHO0/WAXwW/Jb8fNRovzYYJy/6+8OGms2KaEKV1xspEU2untLh/8Vu394v19FltPYOF1RKtRYGNwnnP41Xhk4yE4t/3hE1ZSTQQKBgQDPak7YOuLKfXZL+YDPJDrg41/cAHY5IvCCsifwq1QLHPHpvhIRhi65uUAecoVcvk7D+FXYzHbAcGyO/JxMtMwQ4Tdhmkl47W3aIwD/ZUJfq50xEdx0cgGj3CfgdkkfbTxXgcz2Z/RmmSdwOeXZhM7uCK1fMHQMeqq389f90E2nXwKBgQC1BWkbaB9EFzW9x8FTK4BYP0rpfVIHtrPnlW9oWDzhYfCpYVKI/dZFU0sei1qpmgeHWce7yo6axXU8NH+hbsuGH+e7ej3HOzBjzebSrrUEaKZM4Yn+Fth2hWIr7OydZjACqRfTDo1WsQkd/JLC1/hgcEW4P5ggBglc1om1EtKsSwKBgQCzARxqoOd5ui2OBBaWrr3huFnSlNNzHCRVp0uw+SvfK2vcPp15YkSRJL+hh1RxZgy0NG5iXJNgIaaPAJQj3yT+rGAPbAhcxQw5ZlxGDi3qQ0G2R6PrGzkvIaGIpo0VAkFBhFRPlAfpQ+Q1hsJHp8aux+5YaIi+/F65w0h1VICwIQKBgQCh6xYKMseMw2oJuGzftRCXXv3l/nfqeG1Rn6RIZG0IeO+owmnuKYeI+Sk/SA0vmZxUYGU9P8DxBeCImrjzMESSU2WXl48871+oVlu6ZhV6vRTLvg4Nnme/FC4s9j7rx6T4LEIoQmMAgViTXwHzkPUVZjemyBONyXiEd040DoMtIwKBgQCgMa35MfFAY6VhJhysZmfzo8BXs6N4gh7RGUMKnhEb/0lSD/z1V7pqEX8+D9zj61mQvLk0h81gzDQ+WcZ6qIRz77f4sOtPoSHx8W8czRWLURhJJ2Bj5iLoOsorm5CgUZYn/CJ4oq+O+CjKIHksVOlFQxZj/lyITqLIJHGFKnz/pw==";

        /** 支付宝公钥，如何获取请参考：https://opensupport.alipay.com/support/helpcenter/207/201602487431 **/
        String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkfxIAoLtvhF7WNy7f7qKZmGNc57hkKl5DDNI3cqNwaufUChqkwiAd4UXlAsW5MfgdyUsH0P2RvxP9wbOn++JK3eF2C97K1m3Vn8r67ZGgEwsW3mtzUBqgF9NwCIqohvR1kzZENk5zF4cjg2NWIcRihuQWwD9qVexcMrelOd7rCZFOP1aCx0eOpG60YInNPGweeiqM5HBI6l3wSYfOsYwNTi0cewbuz2WjvPX8IIooi1kR7PVXAUBVyv/VKu2P8SGRXN+kpztkkPBw43TfX+GItNZoDsEIwhskZlukKfKjhaqO373yDtbFjBy49WpsiEO8syyztQ4nTKFOzluyov62QIDAQAB";

        /** 初始化 **/
        AlipayClient alipayClient = new DefaultAlipayClient(URL,APP_ID,APP_PRIVATE_KEY,"json","UTF-8",ALIPAY_PUBLIC_KEY,"RSA2");

        /** 实例化具体API对应的request类，类名称和接口名称对应,当前调用接口名称：alipay.trade.page.pay（电脑网站支付） **/
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();

        /** 设置业务参数  **/
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();

        /** 商户订单号,商户自定义，需保证在商户端不重复，如：20200612000001 **/
        //model.setOutTradeNo("20200612000032");
        model.setOutTradeNo(id.toString());

        /** 销售产品码,固定值：FAST_INSTANT_TRADE_PAY **/
        model.setProductCode("FAST_INSTANT_TRADE_PAY");

        /** 订单标题 **/
        model.setSubject("ticket order");

        /** 订单金额，精确到小数点后两位 **/
        model.setTotalAmount(amount.toString());

        /** 订单描述 **/
        model.setBody("pay for ticket");

        /** 将业务参数传至request中 **/
        alipayRequest.setBizModel(model);

        /** 注：支付结果以异步通知为准，不能以同步返回为准，因为如果实际支付成功，但因为外力因素，如断网、断电等导致页面没有跳转，则无法接收到同步通知；**/
        /** 同步通知地址，以http或者https开头，支付完成后跳转的地址，用于用户视觉感知支付已成功，传值外网可以访问的地址，如果同步未跳转可参考该文档进行确认：https://opensupport.alipay.com/support/helpcenter/193/201602474937 **/
        alipayRequest.setReturnUrl("http://localhost:5173/user");

        /** 异步通知地址，以http或者https开头的，商户外网可以post访问的异步地址，用于接收支付宝返回的支付结果，如果未收到该通知可参考该文档进行确认：https://opensupport.alipay.com/support/helpcenter/193/201602475759 **/
        alipayRequest.setNotifyUrl("http://localhost:5173/notify");

        /**第三方调用（服务商模式），传值app_auth_token后，会收款至授权app_auth_token对应商家账号，如何获传值app_auth_token请参考文档：https://opensupport.alipay.com/support/helpcenter/79/201602494631 **/
        //alipayRequest.putOtherTextParam("app_auth_token", "传入获取到的app_auth_token值");

        String form = null;
        try {

            /** 调用SDK生成表单form表单 **/
//                form = alipayClient.pageExecute(alipayRequest).getBody();

            /** 调用SDK生成支付链接，可在浏览器打开链接进入支付页面 **/
            form = alipayClient.pageExecute(alipayRequest,"GET").getBody();

        } catch (AlipayApiException e) {
            e.printStackTrace();
        }


//            /** 获取接口调用结果，如果调用失败，可根据返回错误信息到该文档寻找排查方案：https://opensupport.alipay.com/support/helpcenter/93 **/
        System.out.println(form);
        return form;
    }

    private String generateCode(){
        Random random = new Random(System.currentTimeMillis());
        StringBuilder code=new StringBuilder();
        int count=0;
        int x;
        while(count<11){
            x= random.nextInt(130);
            while(!((x>=48&&x<=57)||(x>=65&&x<=90)||(x>=97&&x<=122))){
                x=random.nextInt(130);
            }
            code.append((char)x);
            count++;
        }
        return code.toString();
    }


    public String refund(BigDecimal amount, Long id) {
        //System.out.println(id);

        String URL = " https://openapi-sandbox.dl.alipaydev.com/gateway.do";
        String APP_ID = "9021000122698301";
        String APP_PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCSqov7OsUFoFM0ZhbaYi6xXyHYamJoibJdpvSTnTv8S25pDMLkP9cMXfWOM9TL/NiGbAFsqhqTPiHmdc7Hr0ugz5+sd/mkZzod9wY6kZjgShKNsbIE4AbGspnDUZqewp1wI3N+dT2V1399QpDzSU8/2Wajz1Tl/E5W1EQvRKLEA57etncxRzDRULr61NsCJ+XsrbmNxNm9jj/sbDF5anHONxy6adn/8bSmgHv2gRad2jZ5i++tWPr3kNyS2D6A86455y8GUrTHCSLOAvkCww/y/qq5E4TRVF4dKrKubdxXlRxFkfTsNznJzeKahxpCLFrF/yrmzppfJ6NMUfMtIdzVAgMBAAECggEAYspd36uGanPDl9fsLM1VhkP+GDgQcTnO2yZKN+QBM7/bbwcZf4VA5SOk81QWUhDOCD1X+Enb/LItQ362+eoplvhDJXjqvsKJcwWhPHI1913tgWPf9UixR8WlrNqau7V7Nr2qXdO61+OADG96E2Wd6/QUHuLY3NFGM1ZnVy90M3L3OyxUkNAgb5i/h3g80HJDMQ8sNlh+44XuUkjMRRVvpi/bQIAYcvDOohV/hRl4TFq/WqAK2s8ZHO0/WAXwW/Jb8fNRovzYYJy/6+8OGms2KaEKV1xspEU2untLh/8Vu394v19FltPYOF1RKtRYGNwnnP41Xhk4yE4t/3hE1ZSTQQKBgQDPak7YOuLKfXZL+YDPJDrg41/cAHY5IvCCsifwq1QLHPHpvhIRhi65uUAecoVcvk7D+FXYzHbAcGyO/JxMtMwQ4Tdhmkl47W3aIwD/ZUJfq50xEdx0cgGj3CfgdkkfbTxXgcz2Z/RmmSdwOeXZhM7uCK1fMHQMeqq389f90E2nXwKBgQC1BWkbaB9EFzW9x8FTK4BYP0rpfVIHtrPnlW9oWDzhYfCpYVKI/dZFU0sei1qpmgeHWce7yo6axXU8NH+hbsuGH+e7ej3HOzBjzebSrrUEaKZM4Yn+Fth2hWIr7OydZjACqRfTDo1WsQkd/JLC1/hgcEW4P5ggBglc1om1EtKsSwKBgQCzARxqoOd5ui2OBBaWrr3huFnSlNNzHCRVp0uw+SvfK2vcPp15YkSRJL+hh1RxZgy0NG5iXJNgIaaPAJQj3yT+rGAPbAhcxQw5ZlxGDi3qQ0G2R6PrGzkvIaGIpo0VAkFBhFRPlAfpQ+Q1hsJHp8aux+5YaIi+/F65w0h1VICwIQKBgQCh6xYKMseMw2oJuGzftRCXXv3l/nfqeG1Rn6RIZG0IeO+owmnuKYeI+Sk/SA0vmZxUYGU9P8DxBeCImrjzMESSU2WXl48871+oVlu6ZhV6vRTLvg4Nnme/FC4s9j7rx6T4LEIoQmMAgViTXwHzkPUVZjemyBONyXiEd040DoMtIwKBgQCgMa35MfFAY6VhJhysZmfzo8BXs6N4gh7RGUMKnhEb/0lSD/z1V7pqEX8+D9zj61mQvLk0h81gzDQ+WcZ6qIRz77f4sOtPoSHx8W8czRWLURhJJ2Bj5iLoOsorm5CgUZYn/CJ4oq+O+CjKIHksVOlFQxZj/lyITqLIJHGFKnz/pw==";
        String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkfxIAoLtvhF7WNy7f7qKZmGNc57hkKl5DDNI3cqNwaufUChqkwiAd4UXlAsW5MfgdyUsH0P2RvxP9wbOn++JK3eF2C97K1m3Vn8r67ZGgEwsW3mtzUBqgF9NwCIqohvR1kzZENk5zF4cjg2NWIcRihuQWwD9qVexcMrelOd7rCZFOP1aCx0eOpG60YInNPGweeiqM5HBI6l3wSYfOsYwNTi0cewbuz2WjvPX8IIooi1kR7PVXAUBVyv/VKu2P8SGRXN+kpztkkPBw43TfX+GItNZoDsEIwhskZlukKfKjhaqO373yDtbFjBy49WpsiEO8syyztQ4nTKFOzluyov62QIDAQAB";
        AlipayClient alipayClient = new DefaultAlipayClient(URL,APP_ID,APP_PRIVATE_KEY,"json","UTF-8",ALIPAY_PUBLIC_KEY,"RSA2");

        // 构建退款请求对象
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        //request.setBizContent("{\"trade_no\":\"" + generateCode() + id + "\",\"out_trade_no\":\"" + generateCode() + id + "\",\"refund_amount\":\"" + amount + "\"}");
        request.setBizContent("{\"out_request_no\":\"" + generateCode() + "\",\"out_trade_no\":\"" +  id.toString() + "\",\"refund_amount\":\"" + amount.toString() + "\"}");

        // 调用退款接口
        AlipayTradeRefundResponse response;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "退款失败";
        }

        // 处理退款结果
        if (response.isSuccess()) {
            // 退款成功
            return "退款成功";
        } else {
            // 退款失败
            return "退款失败";
        }
    }






}
