package com.zongyi.flavoradapter;

import android.app.Activity;
import android.content.Intent;

public abstract class FlavorAdapter {
    public abstract void onCreate(Activity activity);
    public abstract void initSdk(Activity activity);
    public abstract void login(Activity activity);
    public abstract void pay(String productId);
    public abstract void exitGame(Activity activity);
    public abstract void onResume(Activity activity);
    public abstract void onPause(Activity activity);
    public abstract void onStart(Activity activity);
    public abstract void onRestart(Activity activity);
    public abstract void onStop(Activity activity);
    public abstract void onDestroy(Activity activity);
    public abstract void onActivityResult(Activity activity,int requestCode, int resultCode, Intent data);
    public abstract void onNewIntent(Intent intent);

    public static FlavorAdapter createFlavorAdapterByFlavorName(String flavorName){
        if (flavorName == null || flavorName.length() <= 0){
            return null;
        }
        return FlavorAdapter.createFlavorAdapterByClassName("com.zongyi.flavoradapter.FlavorAdapter" + flavorName.substring(0, 1).toUpperCase() + flavorName.substring(1));
    }

    private static FlavorAdapter createFlavorAdapterByClassName(String className){
        try {
            return (FlavorAdapter)Class.forName(className).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}

