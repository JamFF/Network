package com.ff.network.library;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;

import com.ff.network.library.utils.Constants;

public enum NetworkManager {

    INSTANCE;

    private Application mApplication;

    private NetStateReceiver mReceiver;

    private NetworkCallbackImpl mCallback;

    private ConnectivityManager mManager;

    NetworkManager() {
        if (Constants.IS_RECEIVER) {
            mReceiver = new NetStateReceiver();
        } else {
            mCallback = new NetworkCallbackImpl();
        }
    }

    @SuppressLint("MissingPermission")
    public void init(Application application) {
        this.mApplication = application;

        if (Constants.IS_RECEIVER) {
            // 方式一：注册广播
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constants.ANDROID_NET_CHANGE_ACTION);
            application.registerReceiver(mReceiver, filter);
        } else {
            // 方式二：监听，21以上
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            NetworkRequest request = builder.build();
            mManager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (mManager != null) {
                mManager.registerNetworkCallback(request, mCallback);
            }
        }
    }

    public Application getApplication() {
        if (mApplication == null) {
            throw new RuntimeException("NetworkManager.getDefault().init()未初始化");
        }
        return mApplication;
    }


    public void registerObserver(Object register) {
        if (Constants.IS_RECEIVER && mReceiver != null) {
            mReceiver.registerObserver(register);
        } else if (!Constants.IS_RECEIVER && mCallback != null) {
            mCallback.registerObserver(register);
        }
    }

    public void unRegisterObserver(Object register) {
        if (Constants.IS_RECEIVER && mReceiver != null) {
            mReceiver.unRegisterObserver(register);
        } else if (!Constants.IS_RECEIVER && mCallback != null) {
            mCallback.unRegisterObserver(register);
        }
    }

    public void unRegisterAllObserver() {
        if (Constants.IS_RECEIVER && mReceiver != null) {
            mReceiver.unRegisterAllObserver();
        } else if (!Constants.IS_RECEIVER && mManager != null && mCallback != null) {
            mCallback.unRegisterAllObserver();
            mManager.unregisterNetworkCallback(mCallback);
        }
    }
}
