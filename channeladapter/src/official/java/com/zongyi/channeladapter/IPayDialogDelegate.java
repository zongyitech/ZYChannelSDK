package com.zongyi.channeladapter;

public interface IPayDialogDelegate {
    void onWxPayButtonClicked();

    void onAliPayButtonClicked();

    void onCloseButtonClicked();
}
