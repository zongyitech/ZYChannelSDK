package com.zongyi.channeladapter;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.gionee.game.offlinesdk.floatwindow.AppInfo;
import com.gionee.game.offlinesdk.floatwindow.GamePlatform;
import com.gionee.game.offlinesdk.floatwindow.QuitGameCallback;
import com.gionee.game.offlinesdk.floatwindow.pay.GamePlayByTradeData;
import com.gionee.game.offlinesdk.floatwindow.pay.OrderInfo;
import com.gionee.game.offlinesdk.floatwindow.pay.PayGameCallback;

public class ChannelAdapterGionee extends ChannelAdapter{

    @Override
    public void login(Activity activity, LoginCallback callback) {

    }

    @Override
    public void pay(final Activity activity, final ProductInfo productInfo, final PayCallback callback) {
        OrderInfo orderInfo = createOrderInfo(productInfo.title,productInfo.amount);
        GamePlayByTradeData.getInstance().pay(activity, orderInfo, new PayGameCallback() {

            @Override
            public void onSuccess() {
                // 测试用，支付成功情况，请游戏更具实际情况处理
                callback.onSuccess(productInfo);
            }

            @Override
            public void onFail(String errCode, String errDescription) {
                // 测试用，支付失败情况，请游戏更具实际情况处理
                callback.onFailure(productInfo,errDescription);
            }
        });
    }

    @Override
    public void exitGame(final Activity activity) {
        GamePlatform.quitGame(activity, new QuitGameCallback() {
            @Override
            public void onQuit() {
                Toast.makeText(activity, "结束游戏", Toast.LENGTH_SHORT).show();
                activity.finish();
            }

            @Override
            public void onCancel() {
                Toast.makeText(activity, "取消退出", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public String marketPackageName() {
        return null;
    }

    public void initSdkInApplication(Application application, String API_KEY , String PRIVATE_KEY) {
        AppInfo appInfo = new AppInfo();
        appInfo.setApiKey(API_KEY);
        appInfo.setPrivateKey(PRIVATE_KEY);
        GamePlatform.init(application, appInfo);
    }

    public void requestFloatWindowsPermission(Activity activity)
    {
        /**
         * 6.0以上系统需要手动申请悬浮窗权限
         */
        if (Build.VERSION.SDK_INT >= 23){
            GamePlatform.requestFloatWindowsPermission(activity);
        }
    }

    public void initStartFloatWindows(Activity activity)
    {
        /**
         * 6.0以上系统需要手动申请悬浮窗权限
         */
        if (Build.VERSION.SDK_INT >= 23){
            GamePlatform.requestFloatWindowsPermission(activity);
        }
        GamePlatform.startFloatWindowsService(activity);
    }

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        /**
         * 该处是为了提示权限赋予成功
         */
        GamePlatform.onActivityResult(activity, requestCode, resultCode, data);
    }

    private OrderInfo createOrderInfo(String subject, String price) {
        String orderNum = createOrderNum();

        // 设置订单信息
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCpOrderNum(orderNum);
        orderInfo.setSubject(subject);
        orderInfo.setProductName(subject);
        orderInfo.setTotalFee(price);
        orderInfo.setPayMethod(GamePlayByTradeData.PAY_METHOD_UNSPECIFIED);
        orderInfo.setDealPrice(price);
        return orderInfo;
    }

    private String createOrderNum() {
        return "zongyi" + System.currentTimeMillis();
    }
}
