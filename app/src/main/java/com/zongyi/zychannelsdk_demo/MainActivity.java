package com.zongyi.zychannelsdk_demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.zongyi.flavoradapter.FlavorAdapter;

public class MainActivity extends Activity {

    private FlavorAdapter _flavorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _flavorAdapter = FlavorAdapter.createFlavorAdapterByFlavorName(BuildConfig.FLAVOR);
        if (_flavorAdapter != null) {
            _flavorAdapter.onCreate(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        _flavorAdapter.onResume(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        _flavorAdapter.onStart(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        _flavorAdapter.onRestart(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        _flavorAdapter.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        _flavorAdapter.onStop(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        _flavorAdapter.onNewIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 该处是为了提示权限赋予成功
         */
        _flavorAdapter.onActivityResult(this, requestCode, resultCode, data);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//当返回按键被按下
            return false;
        }
        return false;
    }

    public void onInitSdkButtonClicked(View view) {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _flavorAdapter.initSdk(MainActivity.this);
            }
        });
    }

    public void onInitLoginButtonClicked(View view) {

    }

    public void onLoginButtonClicked(View view) {
        _flavorAdapter.login(this);
    }

    public void onPayButtonClicked(View view) {
        _flavorAdapter.pay("0");
    }

    public void onExitGameButtonClicked(View view) {
        _flavorAdapter.exitGame(this);
    }
}