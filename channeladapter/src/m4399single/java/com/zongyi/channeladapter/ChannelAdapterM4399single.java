package com.zongyi.channeladapter;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.util.Log;

import cn.m4399.operate.OperateCenterConfig;
import cn.m4399.operate.SingleOperateCenter;
import cn.m4399.recharge.RechargeOrder;


public class ChannelAdapterM4399single extends ChannelAdapterMain{

    private SingleOperateCenter mOpeCenter;
    private ProductInfo product;
    @Override
    public void login(Activity activity, final LoginCallback callback) {

    }

    @Override
    public void pay(final Activity activity, final ProductInfo productInfo, final PayCallback callback) {

    }

    public void pay(final Activity activity, final ProductInfo productInfo) {
        mOpeCenter.recharge(activity, productInfo.amount, productInfo.title);
        product = productInfo;
    }


    @Override
    public void exitGame(final Activity activity) {

    }

    @Override
    public String marketPackageName() {
        return null;
    }

    public void initSdkInActivity(Activity activity, String GAME_KEY, String GAME_NAME , final PayCallback callback) {
        mOpeCenter = SingleOperateCenter.getInstance();
        new OperateCenterConfig.Builder(activity)
                .setDebugEnabled(false)  //发布游戏时，要设为false
                .setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) //设置SDK界面方向，应与游戏设置一直
                .setSupportExcess(false) //设置是否支持超出金额充值
                .setGameKey(GAME_KEY) 	//换成实际游戏的game key
                .setGameName(GAME_NAME)	//换成实际游戏的名字，原则上与游戏名字匹配
                .build();

        SingleOperateCenter.SingleRechargeListener singleRechargeListener = new SingleOperateCenter.SingleRechargeListener() {

            /*
             * 充值过程结束时SDK回调此方法
             *
             * 充值过程结束并不代表订单生命周期全部完成，SDK还需要查询订单状态，游戏
             * 要根据订单状态决定是否发放物品等
             *
             * @param msg 表示充值结果的友好的文本说明
             *
             */
            @Override
            public void onRechargeFinished(boolean success, String msg) {
                Log.d("m4399", "Pay: [" + success + ", " + msg + "]");
                if(!success)
                {
                    callback.onFailure(product,"支付失败");
                }
            }

            /*
             * 充值过程成功完成后，SDK会查询订单状态，根据订单状态状态正常则通知游戏发放物品
             *
             * @param shouldDeliver
             *  是否要发放物品
             * @param o
             *  封装了最后提交的订单信息的对象，主要包含以下成员，各成员都有getter方法
             *  payChannel：   充值渠道
             *  orderId：      	充值订单号
             *  je：			充值金额
             *  goods：        	购买的物品
             *
             * @return
             *  物品发放过程是否成功
             */

            @Override
            public boolean notifyDeliverGoods(boolean shouldDeliver, RechargeOrder o) {
                if (shouldDeliver) {
                    Log.d("m4399", "单机充值发放物品, [" + o + "]");
                    callback.onSuccess(product);
                    return true;
                } else {
                    Log.d("m4399", "单机充值查询到的订单状态不正常，建议不要发放物品");
                    callback.onFailure(product,"单机充值查询到的订单状态不正常");
                    return false;
                }
            }
        };
        mOpeCenter.init(activity, singleRechargeListener);
    }

    public void onDestroy()
    {
        if (mOpeCenter != null) {
            mOpeCenter.destroy();
            mOpeCenter = null;
        }
    }
}
