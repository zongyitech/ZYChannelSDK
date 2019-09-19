package com.zongyi.flavoradapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Looper;
import android.widget.Toast;

import com.zongyi.channeladapter.ChannelAdapterMain;
import com.zongyi.channeladapter.ChannelAdapterM4399;

public class FlavorAdapterM4399 extends FlavorAdapter{

    private ChannelAdapterM4399 _channelAdapterM4399;
    private Activity _activity;

    @Override
    public void onCreate(Activity activity) {
        _activity = activity;
        _channelAdapterM4399 = new ChannelAdapterM4399();
    }

    @Override
    public void initSdk(Activity activity) {
        _channelAdapterM4399.initSdkInActivity(activity,"121179");//40027
    }

    @Override
    public void login(Activity activity) {
        _channelAdapterM4399.login(activity, new ChannelAdapterMain.LoginCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(_activity, "登录成功,", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(_activity, "登录失败,", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void pay(String productId) {
        ChannelAdapterMain.ProductInfo productInfo = new ChannelAdapterMain.ProductInfo();
        productInfo.productId = productId;
        productInfo.amount = productId;
        productInfo.description = "去广告";
        productInfo.title = "去广告";
        _channelAdapterM4399.pay(_activity, productInfo, new ChannelAdapterMain.PayCallback() {
            @Override
            public void onSuccess(ChannelAdapterMain.ProductInfo productInfo) {
                try {
                    Toast.makeText(_activity, "去广告支付成功,", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Looper.prepare();
                    Toast.makeText(_activity, "去广告支付成功,", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }

            @Override
            public void onFailure(ChannelAdapterMain.ProductInfo productInfo, String error) {
                try {
                    Toast.makeText(_activity, "去广告支付失败,", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Looper.prepare();
                    Toast.makeText(_activity, "去广告支付失败,", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        });
    }

    @Override
    public void exitGame(final Activity activity) {
        _channelAdapterM4399.exitGame(activity);
    }

    @Override
    public void onResume(Activity activity) {

    }

    @Override
    public void onPause(Activity activity) {

    }

    @Override
    public void onStart(Activity activity) {

    }

    @Override
    public void onRestart(Activity activity) {

    }

    @Override
    public void onStop(Activity activity) {

    }

    @Override
    public void onDestroy(Activity activity) {
        _channelAdapterM4399.onDestroy();
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onNewIntent(Intent intent) {

    }
}
