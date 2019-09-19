package com.zongyi.flavoradapter;

import android.app.Application;

import com.zongyi.channeladapter.ChannelAdapterGionee;

/**
 * Created by zhongpingye on 2018/11/26.
 */

public class FlavorAdapterGioneeApplication extends Application {
    private ChannelAdapterGionee _channelAdapterGionee;
    @Override
    public void onCreate() {
        super.onCreate();
        _channelAdapterGionee = new ChannelAdapterGionee();
        _channelAdapterGionee.initSdkInApplication(this,"537D50F047C446A19FBE19C4CC2FC280" ,"MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKlzhcNggYyKQf1NJlluje0WCj3W1M29UHguWRqbK1QhO9tawDGqnyG02xUbF3+k8ZvLVS3xfhMdqmGDLJeuDBwdTHoZviMZLlJZDWpQhVljBo8pi33YPFru8q2fGdtRUSvB4OqAQUkafghpedZGHPpjvgcO/HVKZKGzj1kETMdhAgMBAAECgYEAo4HLQtluaDRItKirJoSdAnbP7xfLQ7M3YKAobpnn10gyP9lztvkSkC25cMYxiIOad2BekvSBWOS5MVqnvry9BRQ9uAZohQOe8IafQHjsvMrGHOB3x3VGBmI8AAbWWRGOEuadU5PvPAhdBhp4fvLh8Khdaanct39pvPVxUkqWXgECQQD2fyMIm9URzVUaJYUDVD/fJ+k8ShgOJK8hpDUp1gAunrj5TP+HsoHxJpJxDdG3hZSMTCSzAUUP+qiGLR/nu5ERAkEAr/v3V7t9sRZv4FUtLCt3OYyZxZBzfguLSSnl5tsjN9kr7dxdF7esSopq672VUKRrLxifDo/ZVDp2UYqDCDXRUQJAVeL4nwBFHl3QIDsgfId3ddbfMo2SkiMADpW7nGcfSx8szlig1HRDSHYrIk8xWmOl3biONPzlRmsNGytP6Zt2MQJAbJLGUeFBtazDDyNdwD98ygudn070ghI7N8NarKgMPVuGB7UJ1iUibcaiGpkXcu5NlnvZPdGv4pBDCSGsn7ccUQJARhQKE0yh2LYCOP+852Vy0Mt1g/F6uC4MYrB8m6pGqiQtURum5R9j9reuhTgoSKKPROcUbr+gdIG1OA6QnYrLGQ==");
    }

}
