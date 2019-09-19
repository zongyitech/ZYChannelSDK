package com.zongyi.flavoradapter;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.zongyi.channeladapter.ChannelAdapter;
import com.zongyi.channeladapter.ChannelAdapterGioneeonline;

public class FlavorAdapterGioneeonline extends FlavorAdapter{

    private ChannelAdapterGioneeonline _channelAdapterGioneeonline;
    private Activity _activity;

    @Override
    public void onCreate(Activity activity) {
        _activity = activity;
        _channelAdapterGioneeonline = new ChannelAdapterGioneeonline();
        _channelAdapterGioneeonline.requestFloatWindowsPermission(activity);
    }

    @Override
    public void initSdk(Activity activity) {

    }

    @Override
    public void login(final Activity activity) {
        _channelAdapterGioneeonline.login(activity, new ChannelAdapter.LoginCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(activity, "登录成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(activity, "登录失败", Toast.LENGTH_SHORT).show();
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

    }

    @Override
    public void exitGame(final Activity activity) {
        _channelAdapterGioneeonline.exitGame(activity);
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
        _channelAdapterGioneeonline.onActivityResult(activity,requestCode,resultCode,data);
    }

    @Override
    public void onNewIntent(Intent intent) {

    }
}
