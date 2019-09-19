package com.zongyi.channeladapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

import cn.m4399.operate.OperateCenter;
import cn.m4399.operate.OperateCenterConfig;
import cn.m4399.operate.User;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChannelAdapterM4399 extends ChannelAdapterMain{
    private static final int MAX_MARK_LENTH = 32;
    private OperateCenter mOpeCenter;
    private String uid;
    private String name;
    private String nick;
    private String orderId;

    @Override
    public void login(Activity activity, final LoginCallback callback) {
        mOpeCenter.login(activity, new OperateCenter.OnLoginFinishedListener() {

            @Override
            public void onLoginFinished(boolean success, int resultCode, User userInfo) {
                String msg = OperateCenter.getResultMsg(resultCode) + ": " + userInfo;
                Log.d("m4399", "用户信息：" + userInfo);
                Log.d("m4399", "登录信息：" + msg);
                if(success)
                {
                    uid = userInfo.getUid();
                    name = userInfo.getName();
                    nick = userInfo.getNick();
                    callback.onSuccess();
                }else
                {
                    callback.onFailure("登录失败");
                }
            }
        });
    }

    @Override
    public void pay(final Activity activity, final ProductInfo productInfo, final PayCallback callback) {
        String mark = "year|mon-hour_" + System.currentTimeMillis();
        if (mark.length() > MAX_MARK_LENTH)
            mark = mark.substring(0, MAX_MARK_LENTH);

        float a = Float.parseFloat(productInfo.productId);
        int amount = (int)(a); // 支付金额，单位元
        if(amount == 0)
        {
            amount = 1;
        }
        final String urlEncode= URLEncoder.encode(mark);
        final String finalMark = mark;
        mOpeCenter.recharge(activity,
                amount,             //充值金额（元）
                mark,           //游戏方订单号
                productInfo.title,    //商品名称
                new OperateCenter.OnRechargeFinishedListener() {
                    @Override
                    public void onRechargeFinished(boolean success, int resultCode, String msg)
                    {
                        if(success){
                            Log.d("m4399", resultCode + ":yy " + msg);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    OkHttpClient client = new OkHttpClient();
                                    Request request = new Request.Builder().url("http://recharge.1hourlife.com/ServerAPI/pay/4399/checkmark?mark=" + urlEncode).build();
                                    try {
                                        //发送请求
                                        Response response = client.newCall(request).execute();
                                        String result = response.body().string();
                                        Log.d("m4399", "result: " + result);
                                        JSONObject resultJson = new JSONObject(result);
                                        if((resultJson.has("code") ? resultJson.getInt("code") : -1) == 0)
                                        {
                                            orderId = finalMark;
                                            //请求游戏服，获取充值结果
                                            callback.onSuccess(productInfo);
                                        }else
                                        {
                                            callback.onFailure(productInfo, "支付失败");
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();

                        }else{
                            //充值失败逻辑
                            callback.onFailure(productInfo, "支付失败");
                        }
                    }
                });
    }

    @Override
    public void exitGame(final Activity activity) {
        mOpeCenter.shouldQuitGame(activity, new OperateCenter.OnQuitGameListener() {

            @Override
            public void onQuitGame(boolean shouldQuit) {
                Log.v("m4399", "Quit game? " + shouldQuit);
                if (shouldQuit) {
                    if (mOpeCenter != null) {
                        mOpeCenter.destroy();
                        mOpeCenter = null;
                    }
                    activity.finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }
        });
    }

    @Override
    public String marketPackageName() {
        return null;
    }

    public void initSdkInActivity(Activity activity, String GAME_KEY) {
        // 游戏接入SDK；
        mOpeCenter = OperateCenter.getInstance();

        // 配置sdk属性,比如可扩展横竖屏配置
        OperateCenterConfig opeConfig = new OperateCenterConfig.Builder(activity)
                .setDebugEnabled(false)
                .setOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT)
                .setPopLogoStyle(OperateCenterConfig.PopLogoStyle.POPLOGOSTYLE_FOUR)
                .setPopWinPosition(OperateCenterConfig.PopWinPosition.POS_LEFT)
                .setSupportExcess(false)
                .setGameKey(GAME_KEY)
                .build();
        mOpeCenter.setConfig(opeConfig);

        //初始化SDK，在这个过程中会读取各种配置和检查当前帐号是否在登录中
        //只有在init之后， isLogin()返回的状态才可靠
        mOpeCenter.init(activity, new OperateCenter.OnInitGloabListener() {

            // 初始化结束执行后回调
            @SuppressLint("Assert")
            @Override
            public void onInitFinished(boolean isLogin, User userInfo) {
                assert(isLogin == mOpeCenter.isLogin());
            }

            // 注销帐号的回调， 包括个人中心里的注销和logout()注销方式
            @Override
            public void onUserAccountLogout(boolean fromUserCenter, int resultCode) {
                String tail = fromUserCenter ? "从用户中心退出" : "不是从用户中心退出";
                Log.d("m4399", "onUserAccountLogout resultCode: " + resultCode);
            }

            // 个人中心里切换帐号的回调
            @Override
            public void onSwitchUserAccountFinished(boolean fromUserCenter, User userInfo) {
                String tail = fromUserCenter ? "从用户中心切换用户" : "不是从用户中心切换用户";
                Log.d("m4366", "Switch Account: " + userInfo.toString());
            }
        });
    }

    public String getName() {
        if (mOpeCenter.isLogin()) {
            return name;
        }
        return null;
    }

    public String getNick() {
        if (mOpeCenter.isLogin()) {
            return nick;
        }
        return null;
    }

    public String getUid() {
        if (mOpeCenter.isLogin()) {
            return uid;
        }
        return null;
    }

    public void onDestroy()
    {
        if (mOpeCenter != null) {
            mOpeCenter.destroy();
            mOpeCenter = null;
        }
    }

    public String getOrderId() {
        return orderId;
    }
}
