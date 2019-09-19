package com.yiru.jxdl.at.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zongyi.channeladapter.ChannelAdapterMain;
import com.zongyi.channeladapter.wxbean.WXInfoBean;
import com.zongyi.channeladapter.wxbean.WXBean;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by CH
 * on 2019/9/16 0016 10:15
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "WXEntryActivity";
    private static final String APP_ID = "wx7e98f5e829328366";
    private static final String SECRET = "2a3cf7f3e87acbee0b623888967c9c8a";
    private IWXAPI api;
    private WXBean mWXBean;
    private Handler mHandler;
    private String accessToken;
    private String openId;
    private String openidStr;
    private String unionid;
    private String username;
    private String headurl;
    public static ChannelAdapterMain.LoginCallback loginCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        api = WXAPIFactory.createWXAPI(this, APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    Log.d("code0000", "用户成功");
                    String code = ((SendAuth.Resp) baseResp).code;
                    //获取用户信息
                    Log.d("code", code + "");
                    getAccessToken(code);
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED://用户拒绝授权
                    Log.d("code0000", "用户拒绝");
                    loginCallback.onFailure("用户拒绝");
                    finish();
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消
                    Log.d("code0000", "用户取消");
                    loginCallback.onFailure("用户取消");
                    finish();
                    break;
                case BaseResp.ErrCode.ERR_COMM:
                    Log.d("code0000", "fail");
                    loginCallback.onFailure("fail");
                    finish();
                    break;
                default:
                    finish();
                    break;
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    private void getAccessToken(String code) {
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + APP_ID
                        + "&secret=" + SECRET
                        + "&code=" + code
                        + "&grant_type=authorization_code")
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String body = response.body().string();
                if (!TextUtils.isEmpty(body)) {
                    Gson gson = new Gson();
                    JsonReader reader = new JsonReader(new StringReader(body));
                    mWXBean = gson.fromJson(reader, WXBean.class);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            accessToken = mWXBean.getAccess_token();
                            openId = mWXBean.getOpenid();
                            Log.d(TAG, "accessToken" + accessToken);
                            Log.d(TAG, "openId" + openId);
                            getWxInfo(accessToken, openId);
                        }
                    });
                } else {
                    Toast.makeText(WXEntryActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private WXInfoBean mWXInfoBean;

    private void getWxInfo(String access, String openid) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("https://api.weixin.qq.com/sns/userinfo?access_token=" + access
                        + "&openid=" + openid)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final String body = response.body().string();
                if (!TextUtils.isEmpty(body)) {
                    Gson gson = new Gson();
                    JsonReader reader = new JsonReader(new StringReader(body));
                    mWXInfoBean = gson.fromJson(reader, WXInfoBean.class);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            openidStr = mWXInfoBean.getOpenid();
                            unionid = mWXInfoBean.getUnionid();
                            username = mWXInfoBean.getNickname();
                            headurl = mWXInfoBean.getHeadimgurl();
                            loginCallback.onSuccess();
                            //Toast.makeText(WXEntryActivity.this, "微信登录成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(WXEntryActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    // post请求微信登录接口
    private void wxLogin() {
    }

}