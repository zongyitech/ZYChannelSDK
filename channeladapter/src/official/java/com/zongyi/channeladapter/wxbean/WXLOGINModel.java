package com.zongyi.channeladapter.wxbean;

/**
 * Created by CH
 * on 2018/9/12 13:23
 */
public class WXLOGINModel {

    /**
     * code : 1
     * msg : 验证成功
     * data : {"userid":"308fHa3TBrHWhVxSD2feNEx5DyrLJtRe78%2FkF4%2BEVdI","phone":"18888888888","nickname":"简单快乐","headimg":"https://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83errWrsR5RIlevbx5ghYT2KyTF9snp6uIhYxTstj0ZKMsPYFUCDYneQ6KJu5DuE8b4wt5yhpZPwItQ/132","identity":0,"integral":500,"code":"f0ecfd"}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * userid : 308fHa3TBrHWhVxSD2feNEx5DyrLJtRe78%2FkF4%2BEVdI
         * phone : 18888888888
         * nickname : 简单快乐
         * headimg : https://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83errWrsR5RIlevbx5ghYT2KyTF9snp6uIhYxTstj0ZKMsPYFUCDYneQ6KJu5DuE8b4wt5yhpZPwItQ/132
         * identity : 0
         * integral : 500
         * code : f0ecfd
         */

        private String userid;
        private String phone;
        private String nickname;
        private String headimg;
        private int identity;
        private int integral;
        private String code;

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHeadimg() {
            return headimg;
        }

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
        }

        public int getIdentity() {
            return identity;
        }

        public void setIdentity(int identity) {
            this.identity = identity;
        }

        public int getIntegral() {
            return integral;
        }

        public void setIntegral(int integral) {
            this.integral = integral;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
