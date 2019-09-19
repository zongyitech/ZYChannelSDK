package com.zongyi.flavoradapter;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.zongyi.channeladapter.ChannelAdapterMain;
import com.zongyi.channeladapter.ChannelAdapterOppoSingle;

public class FlavorAdapterOppoSingle extends FlavorAdapter{

    private ChannelAdapterOppoSingle _channelAdapterOppoSingle;
    private Activity _activity;

    @Override
    public void onCreate(Activity activity) {
        _activity = activity;
        _channelAdapterOppoSingle = new ChannelAdapterOppoSingle();
    }

    @Override
    public void initSdk(Activity activity) {
        _channelAdapterOppoSingle.initSdk("e2eCa732422245E8891F6555e999878B",activity);
        _channelAdapterOppoSingle.jumpGameRecommend(null,null);
    }

    @Override
    public void login(Activity activity) {
        _channelAdapterOppoSingle.login(activity, new ChannelAdapterMain.LoginCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(_activity, "登录成功，单机sdk登录后支付调不起来", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(_activity, "登录失败，单机sdk登录后支付调不起来", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void pay(String productId) {
        ChannelAdapterMain.ProductInfo productInfo = new ChannelAdapterMain.ProductInfo();
        productInfo.productId = productId;
        productInfo.amount = productId;
        productInfo.description = "斗地主超值礼包加金币";
        productInfo.title = "斗地主超值礼包";
        _channelAdapterOppoSingle.pay(_activity, productInfo, new ChannelAdapterMain.PayCallback() {
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
        _channelAdapterOppoSingle.exitGame(activity);
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

    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onNewIntent(Intent intent) {

    }
}
