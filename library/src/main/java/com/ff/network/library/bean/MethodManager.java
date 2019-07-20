package com.ff.network.library.bean;

import android.support.annotation.NonNull;

import com.ff.network.library.NetType;

import java.lang.reflect.Method;

/**
 * description: 保存符合要求的网络监听注解方法
 * author: FF
 * time: 2019-07-19 22:37
 */
public class MethodManager {

    // 参数类型：NetType netType
    private Class<?> type;

    // 网络类型：netType = NetType.AUTO
    private NetType netType;

    // 需要执行的方法：network()...
    private Method method;

    public MethodManager(Class<?> type, NetType netType, Method method) {
        this.type = type;
        this.netType = netType;
        this.method = method;
    }

    public Class<?> getType() {
        return type;
    }

    public NetType getNetType() {
        return netType;
    }

    public Method getMethod() {
        return method;
    }

    @NonNull
    @Override
    public String toString() {
        return "MethodManager{" +
                "type=" + type +
                ", netType=" + netType +
                ", method=" + method +
                '}';
    }
}
