package com.zongyi.flavoradapter;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.zongyi.channeladapter.ChannelAdapterMain;
import com.zongyi.channeladapter.ChannelAdapterTencent;

public class FlavorAdapterTencent extends FlavorAdapter{

    private ChannelAdapterTencent _channelAdapterTencent;
    private Activity _activity;
    @Override
    public void onCreate(Activity activity) {
        _activity = activity;
        _channelAdapterTencent = new ChannelAdapterTencent();
        _channelAdapterTencent.MIDAS_APPKEY = "y1Z2zWiOO7SeQeWj";
        _channelAdapterTencent.initsdk(activity);
        _channelAdapterTencent.setListener(activity, new ChannelAdapterMain.LoginCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(_activity, "登录成功: " , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(_activity, "登录失败", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void initSdk(Activity activity) {
        _channelAdapterTencent = new ChannelAdapterTencent();
        _channelAdapterTencent.initsdk(activity);
    }

    @Override
    public void login(Activity activity) {
        _channelAdapterTencent.loginAccount("qq");
    }

    @Override
    public void pay(String productId) {
        ChannelAdapterMain.ProductInfo productInfo = new ChannelAdapterMain.ProductInfo();
        productInfo.productId = "1";
        productInfo.amount = productId;
        productInfo.description = "去广告";
        productInfo.title = "去广告超值礼包";
        _channelAdapterTencent.pay(_activity, productInfo, new ChannelAdapterMain.PayCallback() {
            @Override
            public void onSuccess(ChannelAdapterMain.ProductInfo productInfo) {
                Toast.makeText(_activity, "购买成功", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(ChannelAdapterMain.ProductInfo productInfo, String error) {
                Toast.makeText(_activity, "购买失败", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void exitGame(Activity activity) {
        _channelAdapterTencent.loginAccount("wx");
    //      Toast.makeText(_activity, _channelAdapterTencent.getOpenId()+" "+ _channelAdapterTencent.getUserId(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume(Activity activity) {
        _channelAdapterTencent.onResume(activity);
    }

    @Override
    public void onPause(Activity activity) {
        _channelAdapterTencent.onPause(activity);
    }

    @Override
    public void onStart(Activity activity) {

    }

    @Override
    public void onRestart(Activity activity) {
        _channelAdapterTencent.onRestart(activity);
    }

    @Override
    public void onStop(Activity activity) {
        _channelAdapterTencent.onStop(activity);
    }

    @Override
    public void onDestroy(Activity activity) {
        _channelAdapterTencent.onDestroy(activity);
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        _channelAdapterTencent.onActivityResult(requestCode,resultCode, data);
    }

    @Override
    public void onNewIntent(Intent intent) {
        _channelAdapterTencent.onNewIntent(_activity,intent);
    }
}
