package com.zongyi.channeladapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.zongyi.zychannelsdk_demo.R;


public class LoginDialog extends Dialog {

    private ILoginDialogDelegate _delegate;

    public LoginDialog(Context context, ILoginDialogDelegate delegate) {
        super(context, R.style.custom_dialog_style);
        _delegate = delegate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCanceledOnTouchOutside(false);
        setContentView(R.layout.login);
        Button closeButton = (Button) findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_delegate != null) {
                    _delegate.onCloseButtonClicked();
                }
                LoginDialog.this.dismiss();
            }
        });
        Button wechatLoginButton = (Button) findViewById(R.id.wechat_login_button);
        wechatLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_delegate != null) {
                    _delegate.onWechatButtonClicked();
                }
                LoginDialog.this.dismiss();
            }
        });
        Button qqLoginButton = (Button) findViewById(R.id.qq_login_button);
        qqLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_delegate != null) {
                    _delegate.onQQButtonClicked();
                }
                LoginDialog.this.dismiss();
            }
        });
    }
}
