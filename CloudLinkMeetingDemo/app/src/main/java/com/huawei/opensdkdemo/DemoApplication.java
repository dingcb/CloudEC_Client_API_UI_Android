package com.huawei.opensdkdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.util.Log;

import com.huawei.cloudlink.db.DBConfig;
import com.huawei.cloudlink.openapi.HWMSdk;
import com.huawei.cloudlink.openapi.OpenSDKConfig;
import com.huawei.cloudlink.openapi.api.CLMNotifyHandler;
import com.huawei.hwmbiz.eventbus.KickOutState;
import com.huawei.hwmconf.sdk.model.call.entity.CallInfo;

public class DemoApplication extends Application {

    final static String TAG = "DemoApplication";
    private Activity curActivity;
    @Override
    public void onCreate() {
        super.onCreate();

        OpenSDKConfig sdkConfig = new OpenSDKConfig(this)
                .setAppId("openSDKDemo")
                .setServerAddress(DBConfig.Default.getServerAddress())
                .setServerPort(DBConfig.Default.getServerPort())
                .setNotifyHandler(notifyHandler);
        HWMSdk.init(this, sdkConfig);
    }

    private CLMNotifyHandler notifyHandler = new CLMNotifyHandler() {

        /**
         * 呼叫结束后，会收到该通知
         * @param callInfo 呼叫信息
         */
        @Override
        public void onCallEnded(CallInfo callInfo) {
            Log.i(TAG, "呼叫结束了");
        }

        /**
         * 会议结束该后，会收到通知
         * @param result 会议结束后的错误码
         */
        @Override
        public void onConfEnded(int result) {
            Log.i(TAG, "会议结束了");
        }

        @Override
        public void onKickedOut(@Nullable KickOutState kickOutState) {
            // 账号被T，需要重新登录
            Log.i(TAG, "您被踢出了");
        }
    };

}
