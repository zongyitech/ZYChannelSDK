package com.zongyi.flavoradapter;

import android.app.Application;
import com.zongyi.channeladapter.ChannelAdapterGioneeonline;

/**
 * Created by zhongpingye on 2018/11/26.
 */

public class FlavorAdapterGioneeonlineApplication extends Application {
    private ChannelAdapterGioneeonline _channelAdapterGioneeonline;
    @Override
    public void onCreate() {
        super.onCreate();
        _channelAdapterGioneeonline = new ChannelAdapterGioneeonline();
        _channelAdapterGioneeonline.initSdkInApplication(this, "3FFEFD7E1BA449E98A8A0E2DB79621AA");
    }
}
