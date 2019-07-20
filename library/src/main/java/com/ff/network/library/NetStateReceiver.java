package com.ff.network.library;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.ArrayMap;
import android.util.Log;

import com.ff.network.library.annotation.Network;
import com.ff.network.library.bean.MethodManager;
import com.ff.network.library.utils.Constants;
import com.ff.network.library.utils.NetworkUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * description: 网络状态改变的监听
 * author: FF
 * time: 2019-07-19 21:22
 */
public class NetStateReceiver extends BroadcastReceiver {

    private NetType mNetType;

    // key:MainActivity ...  value:A注解方法 B注解方法 ...
    private ArrayMap<Object, List<MethodManager>> networkList;

    public NetStateReceiver() {
        mNetType = NetType.NONE;
        networkList = new ArrayMap<>();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            Log.e(Constants.LOG_TAG, "onReceive: null");
            return;
        }

        if (intent.getAction().equals(Constants.ANDROID_NET_CHANGE_ACTION)) {
            if (NetworkUtils.isNetworkAvailable()) {
                Log.d(Constants.LOG_TAG, "onReceive: 网络连接成功");
            } else {
                Log.d(Constants.LOG_TAG, "onReceive: 没有网络连接");
            }

            postNetType();
        }
    }

    // 同时分发
    private void postNetType() {
        NetType netType = NetworkUtils.getNetType();
        if (mNetType == netType) {
            return;
        }
        mNetType = netType;
        Set<Object> set = networkList.keySet();
        // 比如获取MainActivity对象
        for (final Object getter : set) {
            // 所有注解的方法
            List<MethodManager> methodList = networkList.get(getter);
            if (methodList == null) {
                continue;
            }
            // 循环每个方法
            for (final MethodManager method : methodList) {
                // 参数类型必须是NetType
                if (!method.getType().isAssignableFrom(mNetType.getClass())) {
                    continue;
                }
                // 注解上设置的参数
                switch (method.getNetType()) {
                    case AUTO:
                        invoke(method, getter, mNetType);
                        break;

                    case WIFI:
                        if (mNetType == NetType.WIFI || mNetType == NetType.NONE) {
                            invoke(method, getter, mNetType);
                        }
                        break;

                    case CMWAP:
                        if (mNetType == NetType.CMWAP || mNetType == NetType.NONE) {
                            invoke(method, getter, mNetType);
                        }
                        break;

                    case CMNET:
                        if (mNetType == NetType.CMNET || mNetType == NetType.NONE) {
                            invoke(method, getter, mNetType);
                        }
                        break;

                    default:
                        break;
                }
            }
        }
    }

    private void invoke(MethodManager method, Object getter, NetType netType) {
        Method execute = method.getMethod();
        try {
            execute.invoke(getter, netType);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void registerObserver(Object register) {
        // 获取MainActivity中所有的网络监听注解方法
        List<MethodManager> methodList = networkList.get(register);
        if (methodList == null) { // 不为空表示以前注册过
            // 通过反射获取方法
            methodList = findAnnotationMethod(register);
            networkList.put(register, methodList);
        }
    }

    private List<MethodManager> findAnnotationMethod(Object register) {
        List<MethodManager> methodList = new ArrayList<>();

        Class<?> clazz = register.getClass();
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            // 获取方法的注解
            Network network = method.getAnnotation(Network.class);
            if (network == null) {
                continue;
            }

            // 方法返回值校验
            Type returnType = method.getGenericReturnType();
            if (!"void".equals(returnType.toString())) {
                throw new RuntimeException(method.getName() + "方法返回必须是void");
            }

            // 参数校验
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 1) {
                throw new RuntimeException(method.getName() + "方法有且只有一个参数");
            }

            method.setAccessible(true);
            // 过滤的上面，得到符合要求的方法，才开始添加到集合methodList
            MethodManager manager = new MethodManager(parameterTypes[0], network.netType(), method);
            methodList.add(manager);
        }

        return methodList;
    }

    public void unRegisterObserver(Object register) {
        if (!networkList.isEmpty()) {
            networkList.remove(register);
        }
        Log.e(Constants.LOG_TAG, register.getClass().getName() + "注销成功");
    }

    public void unRegisterAllObserver() {
        if (!networkList.isEmpty()) {
            networkList.clear();
        }
        NetworkManager.INSTANCE.getApplication().unregisterReceiver(this);
        networkList = null;
        Log.e(Constants.LOG_TAG, "注销所有监听成功");
    }
}
