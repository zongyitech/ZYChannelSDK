package com.zongyi.channeladapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;
import com.tencent.ysdk.api.YSDKApi;
import com.tencent.ysdk.framework.common.BaseRet;
import com.tencent.ysdk.framework.common.eFlag;
import com.tencent.ysdk.framework.common.ePlatform;
import com.tencent.ysdk.module.bugly.BuglyListener;
import com.tencent.ysdk.module.pay.PayItem;
import com.tencent.ysdk.module.pay.PayListener;
import com.tencent.ysdk.module.pay.PayRet;
import com.tencent.ysdk.module.user.PersonInfo;
import com.tencent.ysdk.module.user.UserListener;
import com.tencent.ysdk.module.user.UserLoginRet;
import com.tencent.ysdk.module.user.UserRelationRet;
import com.tencent.ysdk.module.user.WakeupRet;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChannelAdapterTencent extends ChannelAdapterMain{

    private static final String LOG_TAG = "YSDK";
    private String nickName = "";
    private String openId = "";
    private String userId = "";
    public  String MIDAS_APPKEY;

    @Override
    public void login(Activity activity, LoginCallback callback) {

    }

    @Override
    public void pay(Activity activity, final ProductInfo productInfo, final PayCallback callback) {

        int amount = Integer.parseInt(productInfo.amount);
        if (amount ==0)
        {
            amount = 1;
        }
        PayItem item = new PayItem();
        item.id = productInfo.productId;
        item.name = productInfo.title;
        item.desc = productInfo.description;
        item.price = amount;
        item.num = Integer.parseInt("1");
        Bitmap bmp = BitmapFactory.decodeResource(activity.getResources(), activity.getResources().getIdentifier("sample_yuanbao", "drawable", activity.getPackageName()));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appResData = baos.toByteArray();
        String ysdkExt = "ysdkExt";
        String midasExt = "midasExt";
        com.tencent.ysdk.api.YSDKApi.buyGoods(false,"1", item, MIDAS_APPKEY, appResData, midasExt, ysdkExt, new PayListener() {
            @Override
            public void OnPayNotify(PayRet payRet) {
                Log.d(LOG_TAG,payRet.toString());
                if(PayRet.RET_SUCC == payRet.ret){
                    //支付流程成功
                    switch (payRet.payState){
                        //支付成功
                        case PayRet.PAYSTATE_PAYSUCC:
                            callback.onSuccess(productInfo);
                            break;
                        //取消支付
                        case PayRet.PAYSTATE_PAYCANCEL:
                            callback.onFailure(productInfo,"用户取消支付："+payRet.toString());
                            break;
                        //支付结果未知
                        case PayRet.PAYSTATE_PAYUNKOWN:
                            callback.onFailure(productInfo,"用户支付结果未知，建议查询余额："+payRet.toString());
                            break;
                        //支付失败
                        case PayRet.PAYSTATE_PAYERROR:
                            callback.onFailure(productInfo,"支付异常"+payRet.toString());
                            break;
                    }
                }else{
                    switch (payRet.flag){
                        case eFlag.Login_TokenInvalid:
                            callback.onFailure(productInfo,"登录态过期，请重新登录："+payRet.toString());
                            letUserLogout();
                            loginAccount("qq");
                            break;
                        case eFlag.Pay_User_Cancle:
                            //用户取消支付
                            callback.onFailure(productInfo,"用户取消支付："+payRet.toString());
                            break;
                        case eFlag.Pay_Param_Error:
                            callback.onFailure(productInfo,"支付失败，参数错误"+payRet.toString());
                            break;
                        case eFlag.Error:
                        default:
                            callback.onFailure(productInfo,"支付异常"+payRet.toString());
                            break;
                    }
                }
            }
        });

    }

    @Override
    public void exitGame(Activity activity) {

    }

    @Override
    public String marketPackageName() {
        return null;
    }

    public void loginAccount(String platform)
    {
        switch (platform) {
            case "qq":
                YSDKApi.login(ePlatform.QQ);
                break;
            case "wx":
                YSDKApi.login(ePlatform.WX);
                break;
            case "guest":
                YSDKApi.login(ePlatform.Guest);
                break;
            default:
                YSDKApi.login(ePlatform.Guest);
                break;
        }
    }

    public void initsdk(Activity activity)
    {
        YSDKApi.onCreate(activity);
    }

    public void setListener(final Activity activity, final LoginCallback callback)
    {
        YSDKApi.setUserListener(new UserListener() {
            @Override
            public void OnLoginNotify(UserLoginRet userLoginRet) {
                switch (userLoginRet.flag) {
                    case eFlag.Succ:
                        letUserLogin(callback);
                        break;
                    // 游戏逻辑，对登录失败情况分别进行处理
                    case eFlag.QQ_UserCancel:
                        showToastTips(activity,"用户取消授权，请重试");
                        letUserLogout();
                        break;
                    case eFlag.QQ_LoginFail:
                        showToastTips(activity,"QQ登录失败，请重试");
                        letUserLogout();
                        break;
                    case eFlag.QQ_NetworkErr:
                        showToastTips(activity,"QQ登录异常，请重试");
                        letUserLogout();
                        break;
                    case eFlag.QQ_NotInstall:
                        showToastTips(activity,"手机未安装手Q，请安装后重试");
                        letUserLogout();
                        break;
                    case eFlag.QQ_NotSupportApi:
                        showToastTips(activity,"手机手Q版本太低，请升级后重试");
                        letUserLogout();
                        break;
                    case eFlag.WX_NotInstall:
                        showToastTips(activity,"手机未安装微信，请安装后重试");
                        letUserLogout();
                        break;
                    case eFlag.WX_NotSupportApi:
                        showToastTips(activity,"手机微信版本太低，请升级后重试");
                        letUserLogout();
                        break;
                    case eFlag.WX_UserCancel:
                        showToastTips(activity,"用户取消授权，请重试");
                        letUserLogout();
                        break;
                    case eFlag.WX_UserDeny:
                        showToastTips(activity,"用户拒绝了授权，请重试");
                        letUserLogout();
                        break;
                    case eFlag.WX_LoginFail:
                        showToastTips(activity,"微信登录失败，请重试");
                        letUserLogout();
                        break;
                    case eFlag.Login_TokenInvalid:
                        showToastTips(activity,"您尚未登录或者之前的登录已过期，请重试");
                        letUserLogout();
                        break;
                    case eFlag.Login_NotRegisterRealName:
                        // 显示登录界面
                        showToastTips(activity,"您的账号没有进行实名认证，请实名认证后重试");
                        letUserLogout();
                        break;
                    default:
                        // 显示登录界面
                        letUserLogout();
                        break;
                }
            }

            @Override
            public void OnWakeupNotify(WakeupRet wakeupRet) {
                switch (wakeupRet.flag) {
                    case eFlag.Wakeup_YSDKLogining:
                        // 用拉起的账号登录，登录结果在OnLoginNotify()中回调
                        break;
                    case eFlag.Wakeup_NeedUserSelectAccount:
                        // 异账号时，游戏需要弹出提示框让用户选择需要登录的账号
                        Log.d(LOG_TAG, "diff account");
                        showDiffLogin(activity);
                        break;
                    case eFlag.Wakeup_NeedUserLogin:
                        // 没有有效的票据，登出游戏让用户重新登录
                        Log.d(LOG_TAG, "need login");
                        letUserLogout();
                        break;
                    default:
                        Log.d(LOG_TAG, "logout");
                        letUserLogout();
                        break;
                }
            }

            @Override
            public void OnRelationNotify(UserRelationRet userRelationRet) {
                if (userRelationRet.persons != null && userRelationRet.persons.size()>0) {
                    PersonInfo personInfo = (PersonInfo)userRelationRet.persons.firstElement();
                    nickName = personInfo.nickName;
                    openId = personInfo.openId;
                    userId = personInfo.userId;
                }
            }
        });

        YSDKApi.setBuglyListener(new BuglyListener(){

            @Override
            public String OnCrashExtMessageNotify() {
                // 此处游戏补充crash时上报的额外信息
                Log.d(LOG_TAG, "OnCrashExtMessageNotify called");
                Date nowTime = new Date();
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                return "new Upload extra crashing message for bugly on " + time.format(nowTime);
            }

            @Override
            public byte[] OnCrashExtDataNotify() {
                return null;
            }
        });

    }

    private void showToastTips(Activity activity, String tips) {
        Toast.makeText(activity,tips,Toast.LENGTH_LONG).show();
    }

    public void letUserLogout() {
        YSDKApi.logout();
    }

    // 平台授权成功,让用户进入游戏. 由游戏自己实现登录的逻辑
    private void letUserLogin(final LoginCallback callback) {
        UserLoginRet ret = new UserLoginRet();
        YSDKApi.getLoginRecord(ret);
        Log.d(LOG_TAG,"flag: " + ret.flag);
        Log.d(LOG_TAG,"platform: " + ret.platform);
        if (ret.ret != BaseRet.RET_SUCC) {
            Log.d(LOG_TAG,"UserLogin error!!!");
            callback.onFailure("UserLogin error!!!");
            letUserLogout();
            return;
        }
        nickName = ret.nick_name;
        openId = ret.open_id;
        userId = ret.getAccessToken();

        if (ret.platform == ePlatform.PLATFORM_ID_QQ) {
            com.tencent.ysdk.api.YSDKApi.queryUserInfo(ePlatform.QQ);
            callback.onSuccess();
        } else if (ret.platform == ePlatform.PLATFORM_ID_WX) {
            com.tencent.ysdk.api.YSDKApi.queryUserInfo(ePlatform.WX);
            callback.onSuccess();
        }else if (ret.platform == ePlatform.PLATFORM_ID_GUEST) {
            com.tencent.ysdk.api.YSDKApi.queryUserInfo(ePlatform.Guest);
            callback.onSuccess();
        }
    }

    private void showDiffLogin(final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("异账号提示");
                builder.setMessage("你当前拉起的账号与你本地的账号不一致，请选择使用哪个账号登陆：");
                builder.setPositiveButton("本地账号",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                showToastTips(activity,"选择使用本地账号");
                                letUserLogout();

                            }
                        });
                builder.setNeutralButton("拉起账号",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                showToastTips(activity,"选择使用拉起账号");
                                letUserLogout();
                            }
                        });
                builder.show();
            }
        });
    }

    public String getNickName() {
        return nickName;
    }

    public String getOpenId() {
        return openId;
    }

    public String getUserId() {
        return userId;
    }

    public void onPause(Activity activity){
        YSDKApi.onPause(activity);
    }

    public void onResume(Activity activity)
    {
        YSDKApi.onResume(activity);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        YSDKApi.onActivityResult(requestCode, resultCode, data);
    }

    public void onRestart(Activity activity) {
        YSDKApi.onRestart(activity);
    }

    public void onStop(Activity activity) {
        YSDKApi.onStop(activity);
    }

    public void onDestroy(Activity activity) {
        YSDKApi.onDestroy(activity);
    }

    public void onNewIntent(Activity activity, Intent intent) {
        YSDKApi.handleIntent(intent);
    }
}
