package com.zongyi.flavoradapter;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.zongyi.channeladapter.ChannelAdapter;
import com.zongyi.channeladapter.ChannelAdapterGionee;

public class FlavorAdapterGionee extends FlavorAdapter{

    private ChannelAdapterGionee _channelAdapterGionee;
    private Activity _activity;

    @Override
    public void onCreate(Activity activity) {
        _activity = activity;
        _channelAdapterGionee = new ChannelAdapterGionee();
   //     _channelAdapterGionee.requestFloatWindowsPermission(activity);
        _channelAdapterGionee.initStartFloatWindows(activity);
    }

    @Override
    public void initSdk(Activity activity) {

    }

    @Override
    public void login(Activity activity) {

    }

    @Override
    public void pay(String productId) {
        ChannelAdapter.ProductInfo productInfo = new ChannelAdapter.ProductInfo();
        productInfo.productId = productId;
        productInfo.amount = productId;
        productInfo.description = "斗地主超值礼包加金币";
        productInfo.title = "斗地主超值礼包";
        _channelAdapterGionee.pay(_activity, productInfo, new ChannelAdapter.PayCallback() {
            @Override
            public void onSuccess(ChannelAdapter.ProductInfo productInfo) {
                Toast.makeText(_activity, "支付成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(ChannelAdapter.ProductInfo productInfo, String error) {
                Toast.makeText(_activity, "支付失败：" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void exitGame(final Activity activity) {
        _channelAdapterGionee.exitGame(activity);
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
        _channelAdapterGionee.onActivityResult(activity,requestCode,resultCode,data);
    }

    @Override
    public void onNewIntent(Intent intent) {

    }
}
