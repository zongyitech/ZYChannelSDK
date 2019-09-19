package com.zongyi.channeladapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zongyi.zychannelsdk_demo.R;

public class PayDialog extends Dialog {

    private IPayDialogDelegate _delegate;

    public PayDialog(Context context, IPayDialogDelegate delegate) {
        super(context, R.style.custom_dialog_style);
        _delegate = delegate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCanceledOnTouchOutside(false);
        setContentView(R.layout.pay);
        Button closeButton = (Button) findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_delegate != null) {
                    _delegate.onCloseButtonClicked();
                }
                PayDialog.this.dismiss();
            }
        });
        Button wxpayButton = (Button) findViewById(R.id.wxpay_button);
        wxpayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_delegate != null) {
                    _delegate.onWxPayButtonClicked();
                }
                PayDialog.this.dismiss();
            }
        });
        Button alipayButton = (Button) findViewById(R.id.alipay_button);
        alipayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_delegate != null) {
                    _delegate.onAliPayButtonClicked();
                }
                PayDialog.this.dismiss();
            }
        });
    }
}
