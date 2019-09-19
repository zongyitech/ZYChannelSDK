package com.zongyi.flavoradapter;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.zongyi.channeladapter.ChannelAdapterMain;
import com.zongyi.channeladapter.ChannelAdapterVivo;


public class FlavorAdapterVivo extends FlavorAdapter{

    private ChannelAdapterVivo _channelAdapterVivo;
    private Activity _activity;

    @Override
    public void onCreate(Activity activity) {
        _activity = activity;
        _channelAdapterVivo = new ChannelAdapterVivo();
        _channelAdapterVivo.cpId = "91faf777a0a40ccf6abe";
        _channelAdapterVivo.appKey = "6473bdd2d6405d716067c4c53df92c19";
        _channelAdapterVivo.appId = "100279708";
    }

    @Override
    public void initSdk(Activity activity) {
        _channelAdapterVivo.initSdk(activity.getApplication(), _channelAdapterVivo.appId, true);;
    }

    @Override
    public void login(Activity activity) {
        _channelAdapterVivo.login(activity, new ChannelAdapterMain.LoginCallback() {
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
        _channelAdapterVivo.pay(_activity, productInfo, new ChannelAdapterMain.PayCallback() {
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
        _channelAdapterVivo.exitGame(activity);
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
