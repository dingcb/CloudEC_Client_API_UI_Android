package com.huawei.opensdkdemo;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.huawei.cloudlink.db.DBConfig;
import com.huawei.cloudlink.openapi.HWMSdk;
import com.huawei.cloudlink.openapi.OpenSDKConfig;
import com.huawei.cloudlink.openapi.api.CLMNotifyHandler;
import com.huawei.conflogic.HwmConfListInfo;
import com.huawei.hwmbiz.BizNotificationHandler;
import com.huawei.hwmconf.sdk.model.call.entity.CallInfo;
import com.huawei.hwmconf.sdk.model.conf.entity.ConfInfo;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DemoApplication extends Application {

    final static String TAG = "DemoApplication";
    private Activity curActivity;

    private static CopyOnWriteArrayList<IConfListUpdate> confListUpdates = new CopyOnWriteArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();

        OpenSDKConfig sdkConfig = new OpenSDKConfig(this)
                .setAppId("openSDKDemo")
                .setNeedIm("true".equals(BuildConfig.needIm))
                .setNeedScreenShare("true".equals(BuildConfig.needScreenShare))
                .setServerAddress(DBConfig.Default.getServerAddress())
                .setServerPort(DBConfig.Default.getServerPort())
                .setBizNotificationHandler(bizNotificationHandler)
                .setNotifyHandler(notifyHandler);
        HWMSdk.init(this, sdkConfig);
    }

    public static void registerConfListUpdateListener(IConfListUpdate confListUpdate){
        confListUpdates.add(confListUpdate);
    }

    public static void removeConfListUpdateListener(IConfListUpdate confListUpdate){
        confListUpdates.remove(confListUpdate);
    }

    private BizNotificationHandler bizNotificationHandler = new BizNotificationHandler() {
        @Override
        public void onConfListUpdateNotify(List<HwmConfListInfo> list) {
            for (IConfListUpdate confListUpdate : confListUpdates) {
                confListUpdate.onConfListUpdateNotify(list);
            }
        }
    };

    private CLMNotifyHandler notifyHandler = new CLMNotifyHandler() {

        /**
         * 呼叫结束后，会收到该通知
         * @param callInfo 呼叫信息
         */
        @Override
        public void onCallEnded(CallInfo callInfo) {
            Log.i(TAG, "呼叫结束了");
        }

        @Override
        public void onConfDetailNotify(ConfInfo confInfo) {
            Log.i(TAG, "confInfo: confId" + confInfo.getConfId() + ", Subject:" + confInfo.getConfSubject());
        }

        /**
         * 会议结束该后，会收到通知
         * @param result 会议结束后的错误码
         */
        @Override
        public void onConfEnded(int result) {
            Log.i(TAG, "会议结束了");
        }
    };

}
