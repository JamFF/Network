package com.ff.network.library;

/**
 * description: 网络类型
 * author: FF
 * time: 2019-07-19 21:31
 */
public enum NetType {

    // 有网络，包括Wi-Fi/GPRS
    AUTO,

    // Wi-Fi网络
    WIFI,

    // 主要是PC/笔记本电脑/PDA上网
    CMNET,

    // 手机上网
    CMWAP,

    // 没有任何网络
    NONE
}
