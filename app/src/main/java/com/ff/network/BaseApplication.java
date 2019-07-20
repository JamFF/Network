package com.ff.network;

import android.app.Application;

import com.ff.network.library.NetworkManager;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NetworkManager.INSTANCE.init(this);
    }
}
