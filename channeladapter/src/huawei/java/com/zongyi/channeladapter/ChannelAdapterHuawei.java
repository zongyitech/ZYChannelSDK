package com.zongyi.channeladapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.common.handler.CheckUpdateHandler;
import com.huawei.android.hms.agent.common.handler.ConnectHandler;
import com.huawei.android.hms.agent.game.GameLoginSignUtil;
import com.huawei.android.hms.agent.game.handler.ICheckLoginSignHandler;
import com.huawei.android.hms.agent.game.handler.LoginHandler;
import com.huawei.android.hms.agent.pay.PaySignUtil;
import com.huawei.android.hms.agent.pay.handler.GetOrderHandler;
import com.huawei.android.hms.agent.pay.handler.PayHandler;
import com.huawei.hms.support.api.entity.game.GameUserData;
import com.huawei.hms.support.api.entity.pay.OrderRequest;
import com.huawei.hms.support.api.entity.pay.PayReq;
import com.huawei.hms.support.api.entity.pay.PayStatusCodes;
import com.huawei.hms.support.api.pay.OrderResult;
import com.huawei.hms.support.api.pay.PayResultInfo;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import static android.content.ContentValues.TAG;

public class ChannelAdapterHuawei extends ChannelAdapterMain {

    public String UNCHECK_PAYREQUESTID_FILE ;
    public String game_priv_key ;
    public String game_public_key ;
    public String pay_priv_key ;
    public String pay_pub_key ;
    public String appId ;
    public String cpId;
    private String playerId;
    private String displayName;
    private String orderID;

    @Override
    public void login(final Activity activity, final LoginCallback callback) {
        Log.d(TAG,"game login: begin");
        HMSAgent.Game.login(new LoginHandler() {
            @Override
            public void onResult(int retCode, GameUserData userData) {
                if (retCode == HMSAgent.AgentResultCode.HMSAGENT_SUCCESS && userData != null) {
                    playerId = userData.getPlayerId();
                    displayName = userData.getDisplayName();
                    callback.onSuccess();
                    Log.d(TAG,"game login: onResult: retCode=" + retCode + "  user=" + userData.getDisplayName() + "|" + userData.getPlayerId() + "|" + userData.getIsAuth() + "|" + userData.getPlayerLevel());
                    // 当登录成功时，此方法会回调2次， | This method recalls 2 times when the login is successful.
                    // 第1次：只回调了playerid；特点：速度快；在要求快速登录，并且对安全要求不高时可以用此playerid登录 | 1th time: Only callback playerID; features: fast speed; You can log in with this playerID when you require fast logon and are not high on security requirements
                    // 第2次：回调了所有信息，userData.getIsAuth()为1；此时需要对登录结果进行验签 | 2nd time: Callback All information, Userdata.getisauth () is 1;

                    if (userData.getIsAuth() == 1) {
                        // 如果需要对登录结果进行验签，请发送请求到开发者服务器进行（安全起见，私钥要放在服务端保存）。| If you need to check your login results, send a request to the developer server (for security, the private key is stored on the service side).
                        // 下面工具方法仅为了展示验签请求的逻辑，实际实现应该放在开发者服务端。| The following tool method is intended only to show the logic of the verification request, and the actual implementation should be on the developer server.
                        GameLoginSignUtil.checkLoginSign(appId, cpId, game_priv_key, game_public_key, userData, new ICheckLoginSignHandler() {
                            @Override
                            public void onCheckResult(String code, String resultDesc, boolean isCheckSuccess) {
                                Log.d(TAG,"game login check sign: onResult: retCode=" + code + "  resultDesc=" + resultDesc + "  isCheckSuccess=" + isCheckSuccess);
                            }
                        });
                    }
                } else {
                    callback.onFailure("登录失败");
                    Log.d(TAG,"game login: onResult: retCode=" + retCode);
                }
            }

            @Override
            public void onChange() {
                // 此处帐号登录发生变化，需要重新登录 | Account login changed here, login required
                Log.d(TAG,"game login: login changed!");
                login(activity, new LoginCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(String error) {

                    }
                });
            }
        }, 1);
    }

    @Override
    public void pay(Activity activity, final ProductInfo productInfo, final PayCallback callback) {

        saveData(activity,"Buyid", productInfo.productId);
        Log.d(TAG,"pay: begin");
        //EditText etAmount = (EditText) findViewById(R.id.et_amount);
        // CP 支付参数
        float amount = Float.parseFloat(productInfo.amount);//支付金额，
        if(amount == 0.0)
        {
            amount = (float) 0.01;
        }
        PayReq payReq = createPayReq(amount, productInfo.title, productInfo.description);
        HMSAgent.Pay.pay(payReq, new PayHandler() {
            @Override
            public void onResult(int retCode, PayResultInfo payInfo) {
                if (retCode == HMSAgent.AgentResultCode.HMSAGENT_SUCCESS && payInfo != null) {
                    boolean checkRst = PaySignUtil.checkSign(payInfo, pay_pub_key);
                    Log.d(TAG,"pay: onResult: pay success and checksign=" + checkRst);
                    //
                    if (checkRst) {
                        callback.onSuccess(productInfo);
                        // 支付成功并且验签成功，发放商品 | Payment successful and successful verification, distribution of goods
                    } else {
                        callback.onFailure(productInfo,"支付失败 pay: onResult: pay =" + retCode);
                        Log.d(TAG,"pay: onResult: pay =" + retCode);
                        // 签名失败，需要查询订单状态：对于没有服务器的单机应用，调用查询订单接口查询；其他应用到开发者服务器查询订单状态。| Signature failed, need to query order status: For stand-alone applications without servers, call Query order interface query, other application to the Developer Server query order status.
                    }
                } else if (retCode == HMSAgent.AgentResultCode.ON_ACTIVITY_RESULT_ERROR
                        || retCode == PayStatusCodes.PAY_STATE_TIME_OUT
                        || retCode == PayStatusCodes.PAY_STATE_NET_ERROR) {
                    callback.onFailure(productInfo,"支付失败 pay: onResult: pay =" + retCode);
                    Log.d(TAG,"pay: onResult: pay =" + retCode);
                    // 需要查询订单状态：对于没有服务器的单机应用，调用查询订单接口查询；其他应用到开发者服务器查询订单状态。 | Pay failed, need to query order status: For stand-alone applications without servers, call Query order interface query, other application to the Developer Server query order status.
                } else {
                    callback.onFailure(productInfo,"支付失败 pay: onResult: pay fail=" + retCode);
                    Log.d(TAG,"pay: onResult: pay fail=" + retCode);
                    // 其他错误码意义参照支付api参考 | Other error code meaning reference payment API reference
                }
            }
        });
        Log.d(TAG,"pay: onResult: pay "+ payReq.getRequestId());
        // 将requestid缓存，供查询订单 | RequestID Cache for Query order
         addRequestIdToCache(activity, payReq.getRequestId());
    }

    @Override
    public void exitGame(Activity activity) {

    }

    @SuppressLint("DefaultLocale")
    private PayReq createPayReq(float totalAmount, String productName, String productDesc) {
        PayReq payReq = new PayReq();

        /*
         * 生成requestId | Generate RequestID
         */
        @SuppressLint("SimpleDateFormat")
        DateFormat format = new java.text.SimpleDateFormat("yyyyMMddhhmmssSSS");
        int random= new SecureRandom().nextInt() % 100000;
        random = random < 0 ? -random : random;
        String requestId = format.format(new Date());
        requestId = String.format("%s%05d", requestId, random);
        orderID = requestId;
        /*
         * 生成总金额 | Generate Total Amount
         */
        String amount = String.format("%.2f", totalAmount);

        //商品名称 | Product Name
        payReq.productName = productName;
        //商品描述 | Product Description
        payReq.productDesc = productDesc;
        // 商户ID，来源于开发者联盟，也叫“支付id” | Merchant ID, from the Developer Alliance, also known as "Payment ID"
        payReq.merchantId = cpId;
        // 应用ID，来源于开发者联盟 | Application ID, from the Developer Alliance
        payReq.applicationID = appId;
        // 支付金额 | Amount paid
        payReq.amount = amount;
        // 支付订单号 | Payment order Number
        payReq.requestId = requestId;
        // 国家码 | Country code
        payReq.country = "CN";
        //币种 | Currency
        payReq.currency = "CNY";
        // 渠道号 | Channel number
        payReq.sdkChannel = 1;
        // 回调接口版本号 | Callback Interface Version number
        payReq.urlVer = "2";

        // 商户名称，必填，不参与签名。会显示在支付结果页面 | Merchant name, must be filled out, do not participate in the signature. will appear on the Pay results page
        payReq.merchantName = "纵艺科技";
        //分类，必填，不参与签名。该字段会影响风控策略 | Categories, required, do not participate in the signature. This field affects wind control policy
        // X4：主题,X5：应用商店,	X6：游戏,X7：天际通,X8：云空间,X9：电子书,X10：华为学习,X11：音乐,X12 视频, | X4: Theme, X5: App Store, X6: Games, X7: Sky Pass, X8: Cloud Space, X9: ebook, X10: Huawei Learning, X11: Music, X12 video,
        // X31 话费充值,X32 机票/酒店,X33 电影票,X34 团购,X35 手机预购,X36 公共缴费,X39 流量充值 | X31, X32 air tickets/hotels, X33 movie tickets, X34 Group purchase, X35 mobile phone advance, X36 public fees, X39 flow Recharge
        payReq.serviceCatalog = "X6";
        //商户保留信息，选填不参与签名，支付成功后会华为支付平台会原样 回调CP服务端 | The merchant retains the information, chooses not to participate in the signature, the payment will be successful, the Huawei payment platform will be back to the CP service
        payReq.extReserved = "";

        //对单机应用可以直接调用此方法对请求信息签名，非单机应用一定要在服务器端储存签名私钥，并在服务器端进行签名操作。| For stand-alone applications, this method can be called directly to the request information signature, not stand-alone application must store the signature private key on the server side, and sign operation on the server side.
        // 在服务端进行签名的cp可以将getStringForSign返回的待签名字符串传给服务端进行签名 | The CP, signed on the server side, can pass the pending signature string returned by Getstringforsign to the service side for signature
        payReq.sign = PaySignUtil.rsaSign(PaySignUtil.getStringForSign(payReq), pay_priv_key);

        return payReq;
    }

    private void addRequestIdToCache(Activity activity ,String requestId) {
        SharedPreferences sp = activity.getSharedPreferences(UNCHECK_PAYREQUESTID_FILE, 0);
        sp.edit().putBoolean(requestId, false).apply();
    }
    private void removeCacheRequestId(Activity activity ,String reqId) {
        SharedPreferences sp = activity.getSharedPreferences(UNCHECK_PAYREQUESTID_FILE, 0);
        sp.edit().remove(reqId).apply();
    }

    @Override
    public String marketPackageName() {
        return null;
    }


    public void onPause(Activity activity)
    {
        HMSAgent.Game.hideFloatWindow(activity);
    }

    public void onResume(Activity activity)
    {
        HMSAgent.Game.showFloatWindow(activity);
    }

    public void init(Application application) {
        HMSAgent.init(application);
    }

    public void initHMSAgent(final Activity activity)
    {
        //请务必在游戏启动后自动调用Connect，而不是在用户进行登录、支付等操作时才调用，否则后续审核过程中将被驳回
        HMSAgent.connect(activity, new ConnectHandler() {
            @Override
            public void onConnect(int rst) {
                Log.d(TAG,"HMS connect end：" + rst);
            }
        });

        HMSAgent.checkUpdate(activity, new CheckUpdateHandler() {
            @Override
            public void onResult(int rst) {
                Log.d(TAG,"check app update rst:：" + rst);
            }
        });
        HMSAgent.Game.showFloatWindow(activity);
    }

    public void checkLostPay(Activity activity, final CheckPayCallback callback) {
        // 取出所有未确认订单 | Remove all unacknowledged orders
        SharedPreferences sp = activity.getSharedPreferences(UNCHECK_PAYREQUESTID_FILE, 0);
        Map<String, ?> allUnCheckedPays = sp.getAll();
        Set<? extends Map.Entry<String, ?>> setAllUnChecked = allUnCheckedPays.entrySet();
        Log.d(TAG,"checkPay: getKey requId="+setAllUnChecked);
        boolean hasUnConfirmedPayRecorder = false;

        // 循环查询 | Circular Query
        for (Map.Entry<String, ?> ele : setAllUnChecked) {
            if (ele == null) {
                continue;
            }

            Object valueObj = ele.getValue();
            if (valueObj != null && valueObj instanceof Boolean) {
                Boolean valueBoolean = (Boolean) valueObj;
                if (!valueBoolean) {
                    String reqId = ele.getKey();
                    getPayDetail(activity, reqId, callback);
                    Log.d(TAG,"checkPay: getKey requId="+reqId);
                    hasUnConfirmedPayRecorder = true;
                }
            }
        }

        if (!hasUnConfirmedPayRecorder) {
            Log.d(TAG,"checkPay: no pay to check");
        }
    }

    private void getPayDetail(final Activity activity, final String reqId,final CheckPayCallback callback) {
        OrderRequest or = new OrderRequest();

        Log.d(TAG,"checkPay: begin=" + reqId);
        or.setRequestId(reqId);
        or.setTime(String.valueOf(System.currentTimeMillis()));
        or.setKeyType("1");
        or.setMerchantId(cpId);

        //对查询订单请求信息进行签名,建议CP在服务器端储存签名私钥，并在服务器端进行签名操作。| To sign the query order request information, it is recommended that CP store the signature private key on the server side and sign the operation on the server side.
        //在服务端进行签名的cp可以将getStringForSign返回的待签名字符串传给服务端进行签名 | The CP, signed on the server side, can pass the pending signature string returned by Getstringforsign to the service side for signature
        or.sign = PaySignUtil.rsaSign(PaySignUtil.getStringForSign(or), pay_priv_key);
        HMSAgent.Pay.getOrderDetail(or, new GetOrderHandler() {
            @Override
            public void onResult(int retCode, OrderResult checkPayResult) {
                Log.d(TAG,"checkPay: requId="+reqId+"  retCode=" + retCode);
                if (checkPayResult != null && checkPayResult.getReturnCode() == retCode) {
                    // 处理支付业务返回码 | Processing Payment Business return code
                    if (retCode == HMSAgent.AgentResultCode.HMSAGENT_SUCCESS) {
                        boolean checkRst = PaySignUtil.checkSign(checkPayResult, pay_pub_key);
                        if (checkRst) {
                            Log.d(TAG,"checkPay: Pay getBillingGold, begin");
                            //amount;
                            String _amount=loadData(activity,"Buyid","1");
                            Log.d(TAG,"checkPay: Pay successfully, "+_amount);
                            callback.onSuccess();
                            // 支付成功，发放对应商品 | Pay success, distribute the corresponding goods
                            Log.d(TAG,"checkPay: Pay successfully, distribution of goods");
                        } else {
                            callback.onFailure("支付失败，没有漏单");
                            // 验签失败，当支付失败处理 | Verification fails when payment fails to deal with
                            Log.d(TAG,"checkPay: Failed to verify signature, pay failed");
                        }

                        // 不需要再查询 | No more queries
                        removeCacheRequestId(activity,checkPayResult.getRequestId());
                    } else if (retCode == PayStatusCodes.ORDER_STATUS_HANDLING
                            || retCode == PayStatusCodes.ORDER_STATUS_UNTREATED
                            || retCode == PayStatusCodes.PAY_STATE_TIME_OUT) {
                        // 未处理完，需要重新查询。如30分钟后再次查询。超过24小时当支付失败处理 | Not finished processing, you need to requery. such as 30 minutes after the query again. More than 24 hours when payment fails to handle
                        Log.d(TAG,"checkPay: Pay failed. errorCode="+retCode+"  errMsg=" + checkPayResult.getReturnDesc());
                    } else if (retCode == PayStatusCodes.PAY_STATE_NET_ERROR) {
                        // 网络失败，需要重新查询 | Network failure, need to Requery
                        Log.d(TAG,"checkPay: A network problem caused the payment to fail. errorCode="+retCode+"  errMsg=" + checkPayResult.getReturnDesc());
                    } else {
                        // 支付失败，不需要再查询 | Payment failed, no more queries required
                        Log.d(TAG,"checkPay: Pay failed. errorCode="+retCode+"  errMsg=" + checkPayResult.getReturnDesc());
                        removeCacheRequestId(activity, reqId);
                    }
                } else {
                    // 没有结果回来，需要重新查询。如30分钟后再次查询。超过24小时当支付失败处理 | No results back, you need to requery. such as 30 minutes after the query again. More than 24 hours when payment fails to handle
                    Log.d(TAG,"checkPay: Pay failed. errorCode="+retCode);
                }
            }
        });
    }

    private String loadData(Activity activity, String keyCode , String valueCode ) {
        SharedPreferences read = activity.getSharedPreferences("GameData",
                Context.MODE_PRIVATE);
        String value = read.getString(keyCode, valueCode);
        Log.d("读取数据 " ,"data is " + valueCode );
        return value;
    }
    private void saveData(Activity activity, String keyCode , String valueCode) {
        SharedPreferences.Editor editor = activity.getSharedPreferences("GameData",
                Context.MODE_PRIVATE).edit();
        editor.putString(keyCode, valueCode);
        editor.apply();
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getOrderID() {
        return orderID;
    }
}
