package com.ff.network.library.utils;

public interface Constants {

    /**
     * Log日志Tag日志前缀名
     */
    String LOG_TAG = "FF";

    /**
     * 系统网络改变广播
     */
    String ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    /**
     * 跳转设置回调请求标识码
     */
    int SETTING_REQUEST_CODE = 666;

    /**
     * 是否使用广播方式监听网络变化
     */
    boolean IS_RECEIVER = false;
}

