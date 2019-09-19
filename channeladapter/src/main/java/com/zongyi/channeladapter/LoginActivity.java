package com.zongyi.channeladapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zongyi.zychannelsdk_demo.R;

/**
 * Created by CH
 * on 2019/9/11 0011 17:35
 */
public class LoginActivity extends Activity {
    Button wxLoginBtn;
    IWXAPI api;
    // 微信登录
    private static final String APP_ID = "wx7e98f5e829328366";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        api = WXAPIFactory.createWXAPI(this, APP_ID, true);
        api.registerApp(APP_ID);
        wxLoginBtn = (Button) findViewById(R.id.wx_login);
        wxLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!api.isWXAppInstalled()) {
                    Toast.makeText(LoginActivity.this, "请安装微信", Toast.LENGTH_SHORT).show();
                } else {
                    SendAuth.Req req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";
                    req.state = "wechat_sdk_demo_test";
                    api.sendReq(req);
                }
            }
        });

    }
}
