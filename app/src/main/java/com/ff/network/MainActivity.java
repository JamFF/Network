package com.ff.network;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ff.network.library.NetType;
import com.ff.network.library.NetworkManager;
import com.ff.network.library.annotation.Network;
import com.ff.network.library.utils.Constants;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NetworkManager.INSTANCE.registerObserver(this);
    }

    @Network(netType = NetType.WIFI)
    private void network(NetType netType) {
        switch (netType) {
            case WIFI:
                Log.i(Constants.LOG_TAG, "MainActivity: WIFI");
                break;
            case CMNET:
            case CMWAP:
                Log.i(Constants.LOG_TAG, "MainActivity: 有网络");
                break;

            case NONE:
                // 没有网络，提示用户跳转到设置
                Log.i(Constants.LOG_TAG, "MainActivity: 没网络");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 反注册、解绑
        NetworkManager.INSTANCE.unRegisterObserver(this);
        NetworkManager.INSTANCE.unRegisterAllObserver();
    }
}
