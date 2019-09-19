package com.zongyi.channeladapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vivo.unionsdk.open.VivoAccountCallback;
import com.vivo.unionsdk.open.VivoExitCallback;
import com.vivo.unionsdk.open.VivoPayCallback;
import com.vivo.unionsdk.open.VivoPayInfo;
import com.vivo.unionsdk.open.VivoUnionSDK;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChannelAdapterVivo extends ChannelAdapterMain {

    private String openId = "";
    private String orderId ;
    private String accessKey;
    private boolean isLoginIn = false;
    public String cpId;
    public String appId;
    public String appKey;

    @Override
    public void login(Activity activity, final LoginCallback callback) {
        VivoUnionSDK.registerAccountCallback(activity,new VivoAccountCallback()
        {
            @Override
            public void onVivoAccountLogin(String userName, String _openId, String authToken) {
                openId = _openId;
                isLoginIn = true;
                if (callback != null) {
                    callback.onSuccess();
                }
            }

            @Override
            public void onVivoAccountLogout(int i) {
            }

            @Override
            public void onVivoAccountLoginCancel() {
                if (callback != null) {
                    callback.onFailure("用戶取消登錄");
                }
            }
        });
        VivoUnionSDK.login(activity);
    }

    @Override
    public void pay(final Activity activity, final ProductInfo productInfo, final PayCallback callback) {
        float a = Float.parseFloat(productInfo.productId);
        int amount = (int)(a) * 100; // 支付金额，单位分
        String s_amount =  Integer.toString(amount);
        if(s_amount.equals("0")){
            s_amount = "1";
        }
        //订单推送接口请在服务器端访问
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("notifyUrl", "http://113.98.231.125:8051/vcoin/notifyStubAction");
        params.put("orderAmount", s_amount); //单位为分；
        params.put("orderDesc", productInfo.description);
        params.put("orderTitle", productInfo.title);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        params.put("orderTime", format.format(new Date()));
        params.put("cpId", cpId);
        params.put("appId", appId);
        params.put("cpOrderNumber", UUID.randomUUID().toString().replaceAll("-", ""));
        params.put("version", "1.0");
        params.put("extInfo", "extInfo_test");
        String str = VivoSignUtils.getVivoSign(params, appKey); //20131030114035565895为app对应的signkey
        params.put("signature", str);
        params.put("signMethod", "MD5");

        RequestQueue mQueue = Volley.newRequestQueue(activity);
        HTTPSTrustManager.allowAllSSL();
        final String finalS_amount = s_amount;
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, "https://pay.vivo.com.cn/vcoin/trade",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if ("200".equals(JsonParser.getString(jsonObject, "respCode"))) {
                            VivoPayInfo.Builder builder = new VivoPayInfo.Builder();
                            builder.setProductName(productInfo.title)
                                    .setProductDes(productInfo.description)
                                    .setProductPrice(JsonParser.getString(jsonObject, "orderAmount"))
                                    .setVivoSignature(JsonParser.getString(jsonObject, "accessKey"))
                                    .setAppId(appId)
                                    .setTransNo(JsonParser.getString(jsonObject, "orderNumber"))
                                    .setUid(getOpenId());
                            VivoPayInfo vivoPayInfo = builder.build();

                            final JSONObject finalJsonObject = jsonObject;
                            VivoUnionSDK.pay(activity, vivoPayInfo, new VivoPayCallback() {
                                @Override
                                public void onVivoPayResult(String transNo, boolean success, String errorCode) {
                                    if (success) {
                                        orderId = transNo;
                                        accessKey = JsonParser.getString(finalJsonObject, "accessKey");
                                        callback.onSuccess(productInfo);
                                    } else {
                                        callback.onFailure(productInfo,"支付失败:" + errorCode);
                                    }
                                }
                            });
                        } else {
                            callback.onFailure(productInfo,"支付失败");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(productInfo,"支付失败");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        mQueue.add(jsonObjectRequest);
    }

    @Override
    public String marketPackageName() {
        return "com.bbk.appstore";
    }


    public void initSdk(Application application , String _appId , boolean isDebug)
    {
        VivoUnionSDK.initSdk(application, _appId, isDebug);
    }

    public void exitGame(final Activity activity)
    {
        VivoUnionSDK.exit(activity, new VivoExitCallback() {
            @Override
            public void onExitCancel() {

            }

            @Override
            public void onExitConfirm() {
                activity.finish();
            }
        });
    }

    public String getOpenId() {
        return openId;
    }

    public String getOrderId() {
        return orderId;
    }
}
