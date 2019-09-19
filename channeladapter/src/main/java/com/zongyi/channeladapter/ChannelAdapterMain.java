package com.zongyi.channeladapter;


import android.app.Activity;

public abstract class ChannelAdapterMain {


    public interface LoginCallback {
        void onSuccess();

        void onFailure(String error);
    }

    public static class ProductInfo {
        public String amount;
        public String productId;
        public String title;
        public String description;
    }

    public interface PayCallback {
        void onSuccess(ProductInfo productInfo);

        void onFailure(ProductInfo productInfo, String error);
    }

    public interface CheckPayCallback {
        void onSuccess();

        void onFailure(String error);
    }

    public abstract void login(Activity activity, LoginCallback callback);

    public abstract void pay(Activity activity, ProductInfo productInfo, PayCallback callback);

    public abstract void exitGame(Activity activity);

    public abstract String marketPackageName();

    public static ChannelAdapterMain createChannelAdapterByFlavorName(String flavorName) {
        if (flavorName == null || flavorName.length() <= 0) {
            return null;
        }
        return ChannelAdapterMain.createChannelAdapterByClassName("com.zongyi.channeladapter.ChannelAdapterMain" + flavorName.substring(0, 1).toUpperCase() + flavorName.substring(1));
    }

    private static ChannelAdapterMain createChannelAdapterByClassName(String className) {
        try {
            return (ChannelAdapterMain) Class.forName(className).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
