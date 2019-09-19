package com.zongyi.channeladapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yiru.jxdl.at.wxapi.WXEntryActivity;
import com.yiru.jxdl.at.wxapi.WXPayEntryActivity;
import com.zongyi.channeladapter.alipay.AliPayBean;
import com.zongyi.channeladapter.alipay.AuthResult;
import com.zongyi.channeladapter.alipay.PayResult;
import com.zongyi.channeladapter.wxbean.WXPAYModel;
import com.zongyi.zychannelsdk_demo.R;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

public class ChannelAdapterOfficial extends ChannelAdapterMain {
    private static final String TAG = "ChannelAdapterOfficial";
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    // 微信登录
    private static final String APP_ID = "wx7e98f5e829328366";
    private IWXAPI api;

    //QQ登录
//    private Tencent mTencent;
    private String openid = "";
    private String nickname = "";
    private String headimgurl = "";

    // 微信支付
    private WXPAYModel mWXPAYModel;

    private PayCallback mPayCallback;
    private ProductInfo mProductInfo;

    // 支付宝支付
    private AliPayBean mAliPayBean;

    public enum LoginPlatform {
        QQ,
        Wechat
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 支付成功
                        mPayCallback.onSuccess(mProductInfo);
                    } else {
                        /**
                         * 支付失败
                         * 8000 正在处理中
                         * 4000 订单支付失败
                         * 6001 用户中途取消
                         * 6002 网络连接出错
                         */

                        Log.d("111111", resultInfo);
                        Log.d("111111", resultStatus);
                        switch (resultStatus) {
                            case "6001":
                                mPayCallback.onFailure(mProductInfo, "用户中途取消");
                                break;
                            case "6002":
                                mPayCallback.onFailure(mProductInfo, "网络连接出错");
                                break;
                            case "4000":
                                mPayCallback.onFailure(mProductInfo, "订单支付失败");
                                break;
                            case "8000":
                                mPayCallback.onFailure(mProductInfo, "正在处理中");
                                break;
                        }
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                    } else {       // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
//                        showAlert(ChannelAdapterOfficial.this, "授权成功:" + authResult);
                        // 其他状态值则为授权失败
//                        showAlert(ChannelAdapterOfficial.this, "授权失败:" + authResult);
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };


    private static void showAlert(Context ctx, String info) {
        showAlert(ctx, info, null);
    }

    private static void showAlert(Context ctx, String info, DialogInterface.OnDismissListener onDismiss) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            new AlertDialog.Builder(ctx)
                    .setMessage(info)
                    .setPositiveButton(R.string.confirm, null)
                    .setOnDismissListener(onDismiss)
                    .show();
        }
    }


    //调后台接口下单,返回成功,调aliPay方法
    public void aliPay(View v, String orderInfo, final Activity activity) {
        //orderInfo 的获取必须来源于服务端
        final String authInfo = orderInfo;
        final Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(authInfo, true);
                Log.i("msp", result.toString());
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    Activity mActivity = null;

    @Override
    public void login(final Activity activity, final LoginCallback callback) {
        // appId注册到微信
        api = WXAPIFactory.createWXAPI(activity, APP_ID, true);
        api.registerApp(APP_ID);
        mActivity = activity;

        LoginDialog loginDialog = new LoginDialog(activity, new ILoginDialogDelegate() {
            @Override
            public void onWechatButtonClicked() {
                WXEntryActivity.loginCallback = callback;
                if (!api.isWXAppInstalled()) {
                    Toast.makeText(activity, "请安装微信", Toast.LENGTH_SHORT).show();
                } else {
                    final SendAuth.Req req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";
                    req.state = "wechat_sdk_demo_test";
                    api.sendReq(req);
                }
                /**
                 * 登录成功后的操作,在回调WXEntryActivity.class中进行
                 */
            }

            @Override
            public void onQQButtonClicked() {
//                mTencent = Tencent.createInstance("", activity.getApplicationContext());
//                mTencent.login(activity, "all", new BaseUiListener());
//                ChannelAdapterOfficial.this.loginWithPlatform(activity, qqPlatform, callback);
            }

            @Override
            public void onCloseButtonClicked() {
                callback.onFailure("user cancel");
            }
        });
        loginDialog.show();
    }


    public String getNowTimeStamp() {
        long time = System.currentTimeMillis();
        return ((Long) (time / 1000L)).toString();
    }

    @Override
    public void pay(Activity activity, final ProductInfo productInfo, final PayCallback callback) {
        // appId注册到微信
        api = WXAPIFactory.createWXAPI(activity, APP_ID, true);
        api.registerApp(APP_ID);
        mActivity = activity;

        PayDialog payDialog = new PayDialog(activity, new IPayDialogDelegate() {
            @Override
            public void onWxPayButtonClicked() {
                WXPayEntryActivity.payCallback = callback;
                wxPay(productInfo.description, productInfo.amount);

            }

            @Override
            public void onAliPayButtonClicked() {
                mPayCallback = callback;
                mProductInfo = productInfo;
                aliPay(activity, productInfo.title, productInfo.description, productInfo.amount);
            }

            @Override
            public void onCloseButtonClicked() {
                callback.onFailure(productInfo, "user cancel");
            }
        });
        payDialog.show();
    }


    @Override
    public void exitGame(Activity activity) {

    }

    @Override
    public String marketPackageName() {
        return null;
    }

    // QQ登录回调
//    private class BaseUiListener implements IUiListener {
//
//        //这个类需要实现三个方法 onComplete（）：登录成功需要做的操作写在这里
//        public void onComplete(Object response) {       //登录成功
//            // TODO Auto-generated method stub
//            Toast.makeText(mActivity.getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
//            try {
//                //获得的数据是JSON格式的，获得你想获得的内容
//                //如果你不知道你能获得什么，看一下下面的LOG
//                Log.i("textShow", "-------------" + response.toString());
//                String openidString = ((JSONObject) response).getString("openid");
//                mTencent.setOpenId(openidString);
//                mTencent.setAccessToken(((JSONObject) response).getString("access_token"), ((JSONObject) response).getString("expires_in"));
//                openid = openidString;
//                Log.i("textShow", "-------------" + openidString);
//            } catch (JSONException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            QQToken qqToken = mTencent.getQQToken();
//            UserInfo info = new UserInfo(mActivity.getApplicationContext(), qqToken);
//
//            info.getUserInfo(new IUiListener() {
//                @Override
//                public void onComplete(Object o) {
//                    //用户信息获取到了
//                    try {
//                        Log.i("textShow", ((JSONObject) o).getString("nickname"));
//                        Log.i("textShow", ((JSONObject) o).getString("gender"));
//                        Log.i("textShow", ((JSONObject) o).getString("figureurl_qq_2"));
//                        Log.i("textShow", o.toString());
//                        nickname = ((JSONObject) o).getString("nickname");
//                        headimgurl = ((JSONObject) o).getString("figureurl_qq_2");
//                        Toast.makeText(mActivity.getApplicationContext(), "QQ登录成功", Toast.LENGTH_SHORT).show();
//                    } catch (JSONException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onError(UiError uiError) {
//                    Log.i("textShow", "onError");
//                }
//
//                @Override
//                public void onCancel() {
//                    Log.i("textShow", "onCancel");
//                }
//            });
//        }
//
//        @Override
//        public void onError(UiError uiError) {      //登录错误
//            Toast.makeText(mActivity.getApplicationContext(), "onError", Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onCancel() {            //取消登录
//            Toast.makeText(mActivity.getApplicationContext(), "onCancel", Toast.LENGTH_SHORT).show();
//        }
//    }


//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // Tencent QQ登录回调
//        Tencent.onActivityResultData(requestCode, resultCode, data, new BaseUiListener());
//        if (requestCode == Constants.REQUEST_API) {
//            if (resultCode == Constants.REQUEST_LOGIN) {
//                Tencent.handleResultData(data, new BaseUiListener());
//            }
//        }
//
//    }

    private void aliPay(Activity activity, String body, String subject, String totalAmount) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("body", body);
        builder.add("subject", subject);
        builder.add("totalAmount", totalAmount);
        Request request = new Request.Builder().url(Url.ALIPAY).post(builder.build()).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) {
                String body = null;
                try {
                    body = response.body().string();
                    Gson gson = new Gson();
                    JsonReader reader = new JsonReader(new StringReader(body));
                    mAliPayBean = gson.fromJson(reader, AliPayBean.class);
                    if (mAliPayBean.getCode() == 0) {
                        // 支付宝支付必须异步调起
                        Runnable payRunnable = new Runnable() {
                            @Override
                            public void run() {
                                PayTask alipay = new PayTask(activity);
                                Map<String, String> result = alipay.payV2(mAliPayBean.getMessage(), true);
                                Log.i(TAG, "result: " + result.toString());
                                Message msg = new Message();
                                msg.what = SDK_PAY_FLAG;
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                            }
                        };
                        Thread payThread = new Thread(payRunnable);
                        payThread.start();
                    } else {
                        Looper.prepare();
                        mPayCallback.onFailure(mProductInfo, mAliPayBean.getMessage());
                        Looper.loop();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
//        OkHttpClientManager.postAsyn(Url.ALIPAY, new OkHttpClientManager.ResultCallback<String>() {
//                    @Override
//                    public void onError(Request request, Exception e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("111111", response);
//                        Gson gson = new Gson();
//                        JsonReader reader = new JsonReader(new StringReader(response));
//                        mAliPayBean = gson.fromJson(reader, AliPayBean.class);
//                        if (mAliPayBean.getCode() == 0) {
//                            // 支付宝支付必须异步调起
//                            Runnable payRunnable = new Runnable() {
//                                @Override
//                                public void run() {
//                                    PayTask alipay = new PayTask(activity);
//                                    Map<String, String> result = alipay.payV2(mAliPayBean.getMessage(), true);
//                                    Log.i(TAG, "result: " + result.toString());
//                                    Message msg = new Message();
//                                    msg.what = SDK_PAY_FLAG;
//                                    msg.obj = result;
//                                    mHandler.sendMessage(msg);
//                                }
//                            };
//                            Thread payThread = new Thread(payRunnable);
//                            payThread.start();
//                        } else {
//                            mPayCallback.onFailure(mProductInfo, mAliPayBean.getMessage());
//                        }
//                    }
//                }
//                , new OkHttpClientManager.Param("body", body)
//                , new OkHttpClientManager.Param("subject", subject)
//                , new OkHttpClientManager.Param("totalAmount", totalAmount));

    }

    private void wxPay(String body, String totalFee) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("body", body);
        builder.add("totalFee", totalFee);
        Request request = new Request.Builder().url(Url.WXPAY).post(builder.build()).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) {
                String body = null;
                try {
                    body = response.body().string();
                    Gson gson = new Gson();
                    JsonReader reader = new JsonReader(new StringReader(body));
                    mWXPAYModel = gson.fromJson(reader, WXPAYModel.class);
                    if (mWXPAYModel.getCode() == 0) {
                        PayReq request = new PayReq();
                        request.appId = mWXPAYModel.getData().getAppId();
                        request.partnerId = mWXPAYModel.getData().getPartnerId();
                        request.prepayId = mWXPAYModel.getData().getPrepayId();
                        request.packageValue = "Sign=WXPay";
                        request.nonceStr = mWXPAYModel.getData().getNonceStr();
                        request.timeStamp = mWXPAYModel.getData().getTimeStamp();
                        request.sign = mWXPAYModel.getData().getSign();
                        api.sendReq(request);
                    } else {
                        mPayCallback.onFailure(mProductInfo, "支付调起失败");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
//        OkHttpClientManager.postAsyn(Url.WXPAY, new OkHttpClientManager.ResultCallback<String>() {
//                    @Override
//                    public void onError(Request request, Exception e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(String response) {
//                        Gson gson = new Gson();
//                        JsonReader reader = new JsonReader(new StringReader(response));
//                        mWXPAYModel = gson.fromJson(reader, WXPAYModel.class);
//                        if (mWXPAYModel.getCode() == 0) {
//                            PayReq request = new PayReq();
//                            request.appId = mWXPAYModel.getData().getAppId();
//                            request.partnerId = mWXPAYModel.getData().getPartnerId();
//                            request.prepayId = mWXPAYModel.getData().getPrepayId();
//                            request.packageValue = "Sign=WXPay";
//                            request.nonceStr = mWXPAYModel.getData().getNonceStr();
//                            request.timeStamp = mWXPAYModel.getData().getTimeStamp();
//                            request.sign = mWXPAYModel.getData().getSign();
//                            api.sendReq(request);
//                        } else {
//                            mPayCallback.onFailure(mProductInfo, "支付调起失败");
//                        }
//
//                    }
//                }
//                , new OkHttpClientManager.Param("body", body)
//                , new OkHttpClientManager.Param("totalFee", totalFee)
//
//
//        );
    }


}
