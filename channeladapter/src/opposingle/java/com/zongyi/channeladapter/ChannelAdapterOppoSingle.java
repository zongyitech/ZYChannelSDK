package com.zongyi.channeladapter;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.nearme.game.sdk.GameCenterSDK;
import com.nearme.game.sdk.callback.ApiCallback;
import com.nearme.game.sdk.callback.GameExitCallback;
import com.nearme.game.sdk.callback.SinglePayCallback;
import com.nearme.game.sdk.common.model.biz.PayInfo;
import com.nearme.game.sdk.common.model.biz.ReqUserInfoParam;
import com.nearme.game.sdk.common.util.AppUtil;
import com.nearme.platform.opensdk.pay.PayResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class ChannelAdapterOppoSingle extends ChannelAdapterMain {

    private String appSecret;
    private String paymentCallbackUrl;
    private boolean isLoginIn = false;
    private String TOKEN;
    private String SSOID;
    private String NICKNAME;


    @Override
    public void login(Activity activity, final LoginCallback callback) {
        initSdk(appSecret,activity);
        GameCenterSDK.getInstance().doLogin(activity, new ApiCallback() {
            @Override
            public void onSuccess(String resultMsg) {
                isLoginIn = true;
                doGetTokenAndSsoid(callback);
            }

            @Override
            public void onFailure(String resultMsg, int resultCode) {
                isLoginIn = false;
                if (callback != null){
                    callback.onFailure(resultMsg);
                }
            }
        });
    }

    @Override
    public void pay(final Activity activity, final ProductInfo productInfo, final PayCallback callback) {
        int amount = 1; // 支付金额，单位分
        try {
            amount = Integer.parseInt(productInfo.amount);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if(amount == 0){
            amount = 1;
        }
        PayInfo payInfo = new PayInfo(System.currentTimeMillis()
                + new Random().nextInt(1000) + "", "自定义字段", amount);
        payInfo.setProductDesc(productInfo.description);
        payInfo.setProductName(productInfo.title);
        payInfo.setCallbackUrl(paymentCallbackUrl);

        GameCenterSDK.getInstance().doSinglePay(activity, payInfo,
                new SinglePayCallback() {

                    @Override
                    public void onSuccess(String resultMsg) {
                        // add OPPO 支付成功处理逻辑~
                        if (callback != null){
                            callback.onSuccess(productInfo);
                        }
                    }

                    @Override
                    public void onFailure(String resultMsg, int resultCode) {
                        // add OPPO 支付失败处理逻辑~
                        if (callback != null){
                            callback.onFailure(productInfo, resultMsg);
                        }
                    }
                    @Override
                    public void onCallCarrierPay(PayInfo payInfo, boolean bySelectSMSPay) {
                        // TODO Auto-generated method stub
                        Toast.makeText(activity, "运营商支付",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void exitGame(final Activity activity) {
        GameCenterSDK.getInstance().onExit(activity,
                new GameExitCallback() {

                    @Override
                    public void exitGame() {
                        // CP 实现游戏退出操作，也可以直接调用
                        // AppUtil工具类里面的实现直接强杀进程~
                        AppUtil.exitGameProcess(activity);
                    }
                });
    }

    @Override
    public String marketPackageName() {
        return "com.oppo.market";
    }

    public void doGetTokenAndSsoid(final LoginCallback callback) {
        GameCenterSDK.getInstance().doGetTokenAndSsoid(new ApiCallback() {

            @Override
            public void onSuccess(String resultMsg) {
                try {
                    JSONObject json = new JSONObject(resultMsg);
                    TOKEN = json.getString("token");
                    SSOID = json.getString("ssoid");
                    doGetUserInfoByCpClient(TOKEN, SSOID,callback);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String resultMsg, int resultCode) {
                if (callback != null){
                    callback.onFailure(resultMsg);
                }
            }
        });
    }

    private void doGetUserInfoByCpClient(String token, String ssoid,final LoginCallback callback) {
        GameCenterSDK.getInstance().doGetUserInfo(
                new ReqUserInfoParam(token, ssoid), new ApiCallback() {
                    @Override
                    public void onSuccess(String resultMsg) {
                        NICKNAME = resultMsg ;
                        if (callback != null){
                            callback.onSuccess();
                        }
                    }

                    @Override
                    public void onFailure(String resultMsg, int resultCode) {
                        if (callback != null){
                            callback.onFailure(resultMsg);
                        }
                    }
                });
    }

    public String getNickName() {
        if(isLoginIn){
            return NICKNAME;
        }
        return null;
    }

    public String getTOKEN() {
        if(isLoginIn){
            return TOKEN;
        }
        return null;
    }

    public String getSSOID() {
        if(isLoginIn){
            return SSOID;
        }
        return null;
    }

    public void initSdk(String appId, Context activity)
    {
        GameCenterSDK.init(appId, activity);
    }

    public void jumpGameRecommend(String appName, String _appSecret)
    {
        GameCenterSDK.getInstance().jumpGameRecommend(appName, _appSecret);
    }
}
