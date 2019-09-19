package com.zongyi.flavoradapter;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.zongyi.channeladapter.ChannelAdapterMain;
import com.zongyi.channeladapter.ChannelAdapterM4399single;

public class FlavorAdapterM4399single extends FlavorAdapter{

    private ChannelAdapterM4399single _channelAdapterM4399single;
    private Activity _activity;

    @Override
    public void onCreate(Activity activity) {
        _activity = activity;
        _channelAdapterM4399single = new ChannelAdapterM4399single();
    }

    @Override
    public void initSdk(final Activity activity) {
        _channelAdapterM4399single.initSdkInActivity(activity, "122308", "涂色大师", new ChannelAdapterMain.PayCallback() {
            @Override
            public void onSuccess(ChannelAdapterMain.ProductInfo productInfo) {
                Toast.makeText(activity, "购买成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(ChannelAdapterMain.ProductInfo productInfo, String error) {
                Toast.makeText(activity, "购买失败", Toast.LENGTH_SHORT).show();
            }
        });//119947
    }

    @Override
    public void login(Activity activity) {

    }

    @Override
    public void pay(String productId) {
        ChannelAdapterMain.ProductInfo productInfo = new ChannelAdapterMain.ProductInfo();
        productInfo.productId = productId;
        productInfo.amount = "1";
        productInfo.description = "斗地主超值礼包加金币";
        productInfo.title = "斗地主超值礼包";
        _channelAdapterM4399single.pay(_activity,productInfo);
    }

    @Override
    public void exitGame(final Activity activity) {

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
        _channelAdapterM4399single.onDestroy();
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onNewIntent(Intent intent) {

    }
}
