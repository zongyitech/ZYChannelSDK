package com.zongyi.channeladapter;

/**
 * Created by CH
 * on 2019/9/16 0016 14:38
 */
public class Url {
    public static final String URL = "http://road.yirutech.com/";//http://172.28.60.96:8822
    // 支付宝支付
    public static final String ALIPAY = URL + "/ServerAPI/alipay/trade/createticket";
    // 微信支付
    public static final String WXPAY = URL + "/ServerAPI/wx/pay/unifiedorder";
}
