package com.zongyi.channeladapter.alipay;

/**
 * Created by CH
 * at 2019-09-18  17:54
 */
public class AliPayBean {

    /**
     * code : 0
     * message : alipay_sdk=alipay-sdk-java-4.6.0.ALL&app_id=2019070265743448&biz_content=%7B%22out_trade_no%22%3A%2220190919013517000001%22%2C%22timeout_express%22%3A%2230m%22%7D&charset=utf-8&format=json&method=alipay.trade.app.pay&sign=UqoDuXiDKGuDd6EOEORkStUgGlqJADc5whVW%2FpyDb56GCPhAqUiqfEzHtLuqCFtnrHoxfChjEreIU3bgm5Js2e%2BgUY45027Y1HOVrggp%2BwmXZatC9%2BOkW%2BXR1TFLT4%2BM%2B8HfUb0SSrHPaQkeP5A9jTpnca2DOZzdRatJNkjMHk35xrq7Hf8OeDJr%2FH9aSvWNzffRAXqUybbxbpGyEpgSFLzQ%2Fh%2FmQmXadkb74gUTiaKsaRZvdC96JxxRbZ35yTKVh8Te5Ww63FzLNB%2BAyZS%2BWIiH8z0YN3QS5DeeuJaNmRT0VMYbZG5QHQMALyEdRG0E6SomV9rPENQ0Tgqc9I0Ekw%3D%3D&sign_type=RSA2×tamp=2019-09-18+17%3A35%3A17&version=1.0
     */

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
