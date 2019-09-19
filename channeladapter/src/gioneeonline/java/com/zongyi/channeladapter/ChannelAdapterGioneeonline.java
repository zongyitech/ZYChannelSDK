package com.zongyi.channeladapter;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.gionee.gamesdk.floatwindow.AccountInfo;
import com.gionee.gamesdk.floatwindow.GamePlatform;
import com.gionee.gamesdk.floatwindow.QuitGameCallback;
import com.gionee.gsp.GnEFloatingBoxPositionModel;


public class ChannelAdapterGioneeonline extends ChannelAdapter{

    private String PlayerId;
    private String amigoToken;
    private String UserId;
    @Override
    public void login(final Activity activity, final LoginCallback callback) {
        GamePlatform.loginAccount(activity, true, new GamePlatform.LoginListener() {

            @Override
            public void onSuccess(AccountInfo accountInfo) {
                // 登录成功，处理自己的业务。

                // 获取playerId
                PlayerId = accountInfo.mPlayerId;


                // 获取amigoToken
                amigoToken = accountInfo.mToken;

                // 获取用户ID
                UserId = accountInfo.mUserId;

                com.gionee.gameservice.utils.LogUtils.logd("AccountInfo", accountInfo.toString());
                callback.onSuccess();
            }

            @Override
            public void onError(Object e) {
                callback.onFailure("登录失败:" + e);
            }

            @Override
            public void onCancel() {
                Toast.makeText(activity, "取消登录", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void pay(final Activity activity, final ProductInfo productInfo, final PayCallback callback) {

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

    public void initSdkInApplication(Application application, String API_KEY ) {
        GamePlatform.init(application, API_KEY);
    }

    public void requestFloatWindowsPermission(Activity activity)
    {
        // 设置悬浮窗的默认位置(如果不设置，则默认左上角)
        GamePlatform.setFloatingBoxOriginPosition(GnEFloatingBoxPositionModel.RIGHT_TOP);
        GamePlatform.requestFloatWindowsPermission(activity);
    }

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        /**
         * 该处是为了提示权限赋予成功
         */
        GamePlatform.onActivityResult(activity, requestCode, resultCode, data);
    }

    public String getAmigoToken() {
        return amigoToken;
    }

    public String getUserId() {
        return UserId;
    }

    public String getPlayerId() {
        return PlayerId;
    }
}
