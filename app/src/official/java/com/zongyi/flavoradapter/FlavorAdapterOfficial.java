package com.zongyi.flavoradapter;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.zongyi.channeladapter.ChannelAdapterMain;
import com.zongyi.channeladapter.ChannelAdapterOfficial;

public class FlavorAdapterOfficial extends FlavorAdapter {

    private ChannelAdapterOfficial _channelAdapter = new ChannelAdapterOfficial();
    private Activity _activity;

    @Override
    public void onCreate(Activity activity) {
        _activity = activity;
    }

    @Override
    public void initSdk(Activity activity) {
//        _channelAdapter.initSDK(activity, "2c3fac3370891", "76b1db25dd049e235e55fd5ccbb5444f", "wx9706d82551515d27", "124156fc80c85df7cf27ad9506cc9f61", null, null, null, null);
    }

    @Override
    public void login(final Activity activity) {
        _channelAdapter.login(activity, new ChannelAdapterMain.LoginCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(activity, "登录成功!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(activity, "登录失败 error = " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void pay(String productId) {
        ChannelAdapterMain.ProductInfo productInfo = new ChannelAdapterMain.ProductInfo();
        productInfo.productId = productId;
        productInfo.amount = "0.01";
        productInfo.title = "xxxx";
        productInfo.description = "xxxxxxxx";
        _channelAdapter.pay(_activity, productInfo, new ChannelAdapterMain.PayCallback() {
            @Override
            public void onSuccess(ChannelAdapterMain.ProductInfo productInfo) {
                Toast.makeText(_activity, "支付成功!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(ChannelAdapterMain.ProductInfo productInfo, String error) {
                Toast.makeText(_activity, "支付失败 error = " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void exitGame(Activity activity) {

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
//        _channelAdapter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onNewIntent(Intent intent) {

    }
}
