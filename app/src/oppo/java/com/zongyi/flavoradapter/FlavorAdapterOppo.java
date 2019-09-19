package com.zongyi.flavoradapter;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.zongyi.channeladapter.ChannelAdapterMain;
import com.zongyi.channeladapter.ChannelAdapterOppo;


public class FlavorAdapterOppo extends FlavorAdapter{

    private ChannelAdapterOppo _channelAdapterOppo;
    private Activity _activity;

    @Override
    public void onCreate(Activity activity) {
        _activity = activity;
        _channelAdapterOppo = new ChannelAdapterOppo();
    }

    @Override
    public void initSdk(Activity activity) {
        _channelAdapterOppo.initSdk("100", "111", activity);
    }

    @Override
    public void login(Activity activity) {
        _channelAdapterOppo.login(activity, new ChannelAdapterMain.LoginCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(String error) {

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
        _channelAdapterOppo.pay(_activity, productInfo, new ChannelAdapterMain.PayCallback() {
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
    public void exitGame(final Activity activity) {
        _channelAdapterOppo.exitGame(activity);
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
