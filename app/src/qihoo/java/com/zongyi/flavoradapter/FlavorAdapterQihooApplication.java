package com.zongyi.flavoradapter;

import android.app.Application;
import com.zongyi.channeladapter.ChannelAdapterQihoo;

/**
 * Created by zhongpingye on 2018/11/26.
 */

public class FlavorAdapterQihooApplication extends Application {
    protected ChannelAdapterQihoo _channelAdapterQihoo;
    @Override
    public void onCreate() {
        super.onCreate();
        _channelAdapterQihoo = new ChannelAdapterQihoo();
        _channelAdapterQihoo.initSdkInAppication(this);
    }

}
