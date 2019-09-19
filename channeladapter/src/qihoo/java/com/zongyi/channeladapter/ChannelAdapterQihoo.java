package com.zongyi.channeladapter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.qihoo.gamecenter.sdk.activity.ContainerActivity;
import com.qihoo.gamecenter.sdk.common.IDispatcherCallback;
import com.qihoo.gamecenter.sdk.matrix.Matrix;
import com.qihoo.gamecenter.sdk.protocols.CPCallBackMgr;
import com.qihoo.gamecenter.sdk.protocols.ProtocolConfigs;
import com.qihoo.gamecenter.sdk.protocols.ProtocolKeys;

import org.json.JSONException;
import org.json.JSONObject;

public class ChannelAdapterQihoo extends ChannelAdapter{

    private String nicknName;
    private String mAccessToken;
    private boolean isInitSdk;

    /**
     * AccessToken是否有效
     */
    protected static boolean isAccessTokenValid = true;
    /**
     * QT是否有效
     */
    protected static boolean isQTValid = true;

    @Override
    public void login(Activity activity, final LoginCallback callback) {

    }

    public void login(Activity activity, boolean isLandScape, final LoginCallback callback) {
        Intent intent = getLoginIntent(activity, isLandScape);
        Matrix.execute(activity, intent, new IDispatcherCallback() {
            @Override
            public void onFinished(String data) {
                if (isCancelLogin(data)) {
                    callback.onFailure("登录取消");
                    return;
                }
                // 解析access_token
                mAccessToken = parseAccessTokenFromLoginResult(data);
                if (!TextUtils.isEmpty(mAccessToken)) {
                    isAccessTokenValid = true;
                    isQTValid = true;
                    // 需要去应用的服务器获取用access_token获取一下带qid的用户信息
                    callback.onSuccess();
                }
            }
        });
    }

    @Override
    public void pay(final Activity activity, final ProductInfo productInfo, final PayCallback callback) {
        if(Matrix.isOnline()){
            if(!isAccessTokenValid) {
                return;
            }
            if(!isQTValid) {
                return;
            }
        }
        Intent intent = getPayIntent(activity, false, productInfo.amount, productInfo.title, productInfo.productId, ProtocolConfigs.FUNC_CODE_PAY);
        intent.putExtra(ProtocolKeys.FUNCTION_CODE,ProtocolConfigs.FUNC_CODE_PAY);
        // 启动接口
        Matrix.invokeActivity(activity, intent, new IDispatcherCallback() {
            @Override
            public void onFinished(String data) {
                if(TextUtils.isEmpty(data)) {
                    return;
                }

                boolean isCallbackParseOk = false;
                JSONObject jsonRes;
                try {
                    jsonRes = new JSONObject(data);
                    // error_code 状态码： 0 支付成功， -1 支付取消， 1 支付失败， -2 支付进行中,5 海马云环境支付转接处理(此处不需要做处理) ,4010201和4009911 登录状态已失效，引导用户重新登录
                    // error_msg 状态描述
                    int errorCode = jsonRes.optInt("error_code");
                    isCallbackParseOk = true;
                    switch (errorCode) {
                        case 0:
                        {
                            callback.onSuccess(productInfo);
                            break;
                        }
                        case 1:
                        {
                            callback.onFailure(productInfo, "支付失败");
                            break;
                        }
                        case -1:
                        {
                            callback.onFailure(productInfo, "支付取消");
                            break;
                        }
                        case -2: {
                            isAccessTokenValid = true;
                            isQTValid = true;
                            String errorMsg = jsonRes.optString("error_msg");
                            Log.d("qihoo",errorMsg);
                            break;
                        }
                        case 4010201:
                            //acess_token失效
                            isAccessTokenValid = false;
                            Log.d("qihoo","AccessToken已失效，请重新登录");
                            break;
                        case 4009911:
                            //QT失效
                            isQTValid = false;
                            Log.d("qihoo","QT已失效，请重新登录");
                            break;
                        //海马云环境支付转接处理(此处不需要做处理),不是错误码
                        case 5:
                            isAccessTokenValid = true;
                            isQTValid = true;
                            String errorMsg = jsonRes.optString("error_msg");
                            Log.d("qihoo",errorMsg);
                            break;
                        default:
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // 用于测试数据格式是否异常。
                if (!isCallbackParseOk) {
                    Log.d("qihoo","严重错误！！接口返回数据格式错误！！");
                }
            }
        });
    }

    @Override
    public void exitGame(Activity activity) {

    }


    public void exitGame(final Activity activity, boolean isLandScape) {
        doSdkQuit(activity,isLandScape);
    }

    @Override
    public String marketPackageName() {
        return null;
    }

    public void initSdkInActivity(final Activity activity, final LoginCallback callback) {

        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Matrix.setActivity(activity, new CPCallBackMgr.MatrixCallBack() {
                    @Override
                    public void execute(Context context, int functionCode, String functionParams) {
                        if (functionCode == ProtocolConfigs.FUNC_CODE_SWITCH_ACCOUNT) {
                            doSdkSwitchAccount(activity, getLandscape(context), callback);
                        }else if (functionCode == ProtocolConfigs.FUNC_CODE_INITSUCCESS) {
                            //这里返回成功之后才能调用SDK 其它接口
                            isInitSdk = true;
                        }
                    }
                }, true);
            }
        });
    }

    public void initSdkInAppication(Application application) {
        Matrix.initInApplication(application);
    }

    /**
     * 生成调用360SDK登录接口的Intent
     * @param isLandScape 是否横屏
     * @return intent
     */
    private Intent getLoginIntent(Activity activity, boolean isLandScape) {

        Intent intent = new Intent(activity, ContainerActivity.class);

        // 界面相关参数，360SDK界面是否以横屏显示。
        intent.putExtra(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, isLandScape);

        // 必需参数，使用360SDK的登录模块。
        intent.putExtra(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_LOGIN);

        return intent;
    }

    /***
     * 生成调用360SDK切换账号接口的Intent
     *
     * @param isLandScape 是否横屏
     * @return Intent
     */
    private Intent getSwitchAccountIntent(Activity activity, boolean isLandScape) {
        Intent intent = new Intent(activity, ContainerActivity.class);
        // 必须参数，360SDK界面是否以横屏显示。
        intent.putExtra(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, isLandScape);
        // 必需参数，使用360SDK的切换账号模块。
        intent.putExtra(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_SWITCH_ACCOUNT);
        return intent;
    }

    /**
     * 使用360SDK的切换账号接口
     *
     * @param isLandScape 是否横屏显示登录界面
     */
    private void doSdkSwitchAccount(final Activity activity, boolean isLandScape, final LoginCallback callback) {
        Intent intent = getSwitchAccountIntent(activity, isLandScape);
        Matrix.invokeActivity(activity, intent, new IDispatcherCallback() {
            @Override
            public void onFinished(String data) {
                // press back
                if (isCancelLogin(data)) {
                    callback.onFailure("取消登录");
                    return;
                }
                if(data!=null){
                    // 解析access_token
                    mAccessToken = parseAccessTokenFromLoginResult(data);
                    // 显示一下登录结果
                    if (!TextUtils.isEmpty(mAccessToken)) {
                        isAccessTokenValid = true;
                        isQTValid = true;
                        callback.onSuccess();
                    }
                    Toast.makeText(activity, data, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * 使用360SDK的退出接口
     *
     * @param isLandScape 是否横屏显示支付界面
     */
    private void doSdkQuit(final Activity activity, boolean isLandScape) {

        Bundle bundle = new Bundle();

        // 界面相关参数，360SDK界面是否以横屏显示。
        bundle.putBoolean(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, isLandScape);

        // 必需参数，使用360SDK的退出模块。
        bundle.putInt(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_QUIT);

        // 可选参数，登录界面的背景图片路径，必须是本地图片路径
        bundle.putString(ProtocolKeys.UI_BACKGROUND_PICTRUE, "");

        Intent intent = new Intent(activity, ContainerActivity.class);
        intent.putExtras(bundle);

        Matrix.invokeActivity(activity, intent, new IDispatcherCallback() {
            @Override
            public void onFinished(String data) {
                JSONObject json;
                try {
                    json = new JSONObject(data);
                    int which = json.optInt("which", -1);

                    switch (which) {
                        case 0: // 用户关闭退出界面
                            return;
                        default:// 退出游戏
                            activity.finish();
                            return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /***
     * 生成调用360SDK支付接口的Intent
     *
     * @param isLandScape
     * @return Intent
     */
    protected Intent getPayIntent(Activity activity, boolean isLandScape, String amount, String productName, String productId, int functionCode) {
        Bundle bundle = new Bundle();

        // 界面相关参数，360SDK界面是否以横屏显示。
        bundle.putBoolean(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, isLandScape);

        // *** 以下非界面相关参数 ***

        // 设置QihooPay中的参数。

        // 必需参数，所购买商品金额, 以分为单位。金额大于等于100分，360SDK运行定额支付流程； 金额数为0，360SDK运行不定额支付流程。
        bundle.putString(ProtocolKeys.AMOUNT,amount);

        // 必需参数，所购买商品名称，应用指定，建议中文，最大10个中文字。
        bundle.putString(ProtocolKeys.PRODUCT_NAME, productName);

        // 必需参数，购买商品的商品id，应用指定，最大16字符。
        bundle.putString(ProtocolKeys.PRODUCT_ID, productId);

        // 必需参数，应用方提供的支付结果通知uri，最大255字符。360服务器将把支付接口回调给该uri，具体协议请查看文档中，支付结果通知接口–应用服务器提供接口。
        bundle.putString(ProtocolKeys.NOTIFY_URI, "");

        // 必需参数，游戏或应用名称，最大16中文字。
        bundle.putString(ProtocolKeys.APP_NAME, "游戏充值");

        // 必需参数，应用内的用户名，如游戏角色名。 若应用内绑定360账号和应用账号，则可用360用户名，最大16中文字。（充值不分区服，
        // 充到统一的用户账户，各区服角色均可使用）。


        bundle.putString(ProtocolKeys.APP_USER_NAME, "");

        bundle.putString(ProtocolKeys.APP_ORDER_ID, "zongyi" + System.currentTimeMillis());
        // 必需参数，应用内的用户id。
        // 若应用内绑定360账号和应用账号，充值不分区服，充到统一的用户账户，各区服角色均可使用，则可用360用户ID最大32字符。
        bundle.putString(ProtocolKeys.APP_USER_ID, "userid");

        bundle.putInt(ProtocolKeys.FUNCTION_CODE,functionCode);

        Intent intent = new Intent(activity, ContainerActivity.class);
        intent.putExtras(bundle);

        return intent;
    }

    private boolean isCancelLogin(String data) {
        try {
            JSONObject joData = new JSONObject(data);
            int errno = joData.optInt("errno", -1);
            if (-1 == errno) {
                return true;
            }
        } catch (Exception ignored) {}
        return false;
    }

    private String parseAccessTokenFromLoginResult(String loginRes) {
        try {
            JSONObject joRes = new JSONObject(loginRes);
            JSONObject joData = joRes.getJSONObject("data");
            return joData.getString("access_token");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean getLandscape(Context context) {
        if (context == null) {
            return false;
        }
        return (context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE);
    }

    public String getmAccessToken() {
        if(!isInitSdk)
        {
            return "";
        }
        return mAccessToken;
    }

    public void onDestroy(Activity activity)
    {
        Matrix.destroy(activity);
    }


    public void onResume(Activity activity) {
        Matrix.onResume(activity);
    }

    public void onStart(Activity activity) {
        Matrix.onStart(activity);
    }

    public void onRestart(Activity activity) {
        Matrix.onRestart(activity);
    }

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        Matrix.onActivityResult(activity,requestCode, resultCode, data);
    }

    public void onPause(Activity activity) {
        Matrix.onPause(activity);
    }

    public void onStop(Activity activity) {
        Matrix.onStop(activity);
    }

    public void onNewIntent(Activity activity, Intent intent) {
        Matrix.onNewIntent(activity,intent);
    }
}
