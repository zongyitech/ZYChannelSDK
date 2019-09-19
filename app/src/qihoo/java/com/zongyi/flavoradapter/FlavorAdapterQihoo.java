package com.zongyi.flavoradapter;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.zongyi.channeladapter.ChannelAdapter;
import com.zongyi.channeladapter.ChannelAdapterQihoo;

public class FlavorAdapterQihoo extends FlavorAdapter{

    private Activity _activity;
    protected ChannelAdapterQihoo _channelAdapterQihoo;
    @Override
    public void onCreate(Activity activity) {
        _activity = activity;
        _channelAdapterQihoo = new ChannelAdapterQihoo();

    }

    @Override
    public void initSdk(final Activity activity) {
        _channelAdapterQihoo.initSdkInActivity(activity, new ChannelAdapter.LoginCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(activity, "登录成功", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(activity, "登录失败", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void login(final Activity activity) {
        _channelAdapterQihoo.login(activity, false, new ChannelAdapter.LoginCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(activity, "登录成功", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(activity, "登录失败", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void pay(String productId) {
        ChannelAdapter.ProductInfo productInfo = new ChannelAdapter.ProductInfo();
        productInfo.productId = productId;
        productInfo.amount = productId;
        productInfo.description = "斗地主超值礼包加金币";
        productInfo.title = "斗地主超值礼包";
        _channelAdapterQihoo.pay(_activity, productInfo, new ChannelAdapter.PayCallback() {
            @Override
            public void onSuccess(ChannelAdapter.ProductInfo productInfo) {
                Toast.makeText(_activity, "购买成功", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(ChannelAdapter.ProductInfo productInfo, String error) {
                Toast.makeText(_activity, "购买失败", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void exitGame(final Activity activity) {
        _channelAdapterQihoo.exitGame(activity,false);
    }

    @Override
    public void onResume(Activity activity) {
        _channelAdapterQihoo.onResume(activity);
    }

    @Override
    public void onPause(Activity activity) {
        _channelAdapterQihoo.onPause(activity);
    }

    @Override
    public void onStart(Activity activity) {
        _channelAdapterQihoo.onStart(activity);
    }

    @Override
    public void onRestart(Activity activity) {
        _channelAdapterQihoo.onRestart(activity);
    }

    @Override
    public void onStop(Activity activity) {
        _channelAdapterQihoo.onStop(activity);
    }

    @Override
    public void onDestroy(Activity activity) {
        _channelAdapterQihoo.onDestroy(activity);
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        _channelAdapterQihoo.onActivityResult(activity,requestCode,resultCode,data);
    }
    public void onNewIntent(Intent intent) {
        _channelAdapterQihoo.onNewIntent(_activity,intent);
    }
}
