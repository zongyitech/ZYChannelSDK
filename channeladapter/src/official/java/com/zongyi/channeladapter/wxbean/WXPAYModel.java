package com.zongyi.channeladapter.wxbean;

/**
 * Created by CH
 * on 2018/9/26 14:08
 */
public class WXPAYModel {


    /**
     * code : 0
     * message : succ
     * data : {"timeStamp":"1568799280","packageValue":"Sign=WXPay","appId":"wx7e98f5e829328366","sign":"B2E559CA852B0F5D804841B82B4689CF","partnerId":"1554626671","prepayId":"wx18173442727949b6beed4eb31858339500","nonceStr":"1568799280392"}
     */

    private int code;
    private String message;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * timeStamp : 1568799280
         * packageValue : Sign=WXPay
         * appId : wx7e98f5e829328366
         * sign : B2E559CA852B0F5D804841B82B4689CF
         * partnerId : 1554626671
         * prepayId : wx18173442727949b6beed4eb31858339500
         * nonceStr : 1568799280392
         */

        private String timeStamp;
        private String packageValue;
        private String appId;
        private String sign;
        private String partnerId;
        private String prepayId;
        private String nonceStr;

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        public String getPackageValue() {
            return packageValue;
        }

        public void setPackageValue(String packageValue) {
            this.packageValue = packageValue;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getPartnerId() {
            return partnerId;
        }

        public void setPartnerId(String partnerId) {
            this.partnerId = partnerId;
        }

        public String getPrepayId() {
            return prepayId;
        }

        public void setPrepayId(String prepayId) {
            this.prepayId = prepayId;
        }

        public String getNonceStr() {
            return nonceStr;
        }

        public void setNonceStr(String nonceStr) {
            this.nonceStr = nonceStr;
        }
    }
}
