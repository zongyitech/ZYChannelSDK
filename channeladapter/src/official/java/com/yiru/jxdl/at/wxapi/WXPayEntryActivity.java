package com.yiru.jxdl.at.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zongyi.channeladapter.ChannelAdapterMain;

/**
 * Created by CH
 * on 2019/9/16 0016 10:39
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI wxAPI;
    private static final String TAG = "WXEntryActivity";
    private static final String APP_ID = "wx7e98f5e829328366";


    public static ChannelAdapterMain.PayCallback payCallback;
    public static ChannelAdapterMain.ProductInfo productInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wxAPI = WXAPIFactory.createWXAPI(this, APP_ID);
        wxAPI.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        wxAPI.handleIntent(intent, this);
    }


    @Override
    public void onReq(BaseReq baseReq) {

    }


    @Override
    public void onResp(BaseResp baseResp) {
        Log.i("ansen", "微信支付回调 返回错误码:" + baseResp.errCode + " 错误名称:" + baseResp.errStr);
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {//微信支付
            Log.d("ansen", "------------");
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    Log.d("wx_pay", "支付完成");
                    //跳转到支付成功界面
                    payCallback.onSuccess(productInfo);
                    finish();
                    break;
                case BaseResp.ErrCode.ERR_COMM:
                    Log.d("wx_pay", "支付失败");
                    payCallback.onFailure(productInfo, baseResp.errStr);
                    finish();
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    Log.d("wx_pay", "用户取消支付");
                    finish();
                    Toast.makeText(this, "用户取消支付", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
        finish();

    }


}