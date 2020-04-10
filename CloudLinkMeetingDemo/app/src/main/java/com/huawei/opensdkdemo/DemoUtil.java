package com.huawei.opensdkdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.huawei.conflogic.HwmConfListInfo;
import com.huawei.hwmconf.presentation.constant.Constants;
import com.huawei.hwmconf.presentation.model.ConfItemBaseModel;
import com.huawei.hwmconf.presentation.model.ConfItemContentModel;
import com.huawei.hwmconf.presentation.model.ConfItemTitleModel;
import com.huawei.hwmconf.presentation.util.DateUtil;
import com.huawei.hwmconf.sdk.common.GlobalHandler;
import com.huawei.hwmfoundation.HwmContext;
import com.huawei.hwmlogger.HCLog;
import com.huawei.opensdkdemo.sdk.ConfItemModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DemoUtil {
    private static String TAG = "DemoUtil";
    private static  DemoUtil mInstance = null;
    private AlertDialog alertDialog;
    public static DemoUtil getInstance() {
        if (mInstance == null) {
            synchronized (DemoUtil.class) {
                if (mInstance == null) {
                    mInstance = new DemoUtil();
                }
            }
        }
        return mInstance;
    }
    private static boolean packageInstalled(Context context, String packageName){
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName,0);
        } catch (PackageManager.NameNotFoundException e) {
            return  false;
        }
        return packageInfo != null;
    }
    public static void openCloudLinkWithURL(Context context, String url){
        if (DemoUtil.packageInstalled(context,"com.huawei.CloudLink")){
            Intent intent = new Intent();
            intent.setData(Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }else {
            showToast(context,"应用未安装");
        }

    }
    public static  void showToast(Context context, String msg){
        HwmContext.getInstance().runOnMainThread(() -> Toast.makeText(context, msg, Toast.LENGTH_LONG).show());
    }
    public static RecyclerView.ItemDecoration getRecyclerViewDivider(Context context, @DrawableRes int drawableId) {
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(context.getResources().getDrawable(drawableId));
        return itemDecoration;
    }
    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    private ConfItemModel transform(HwmConfListInfo ckConfListInfo) {
        ConfItemModel confItemModel = new ConfItemModel();
        String startTime = DateUtil.transTimeZone(ckConfListInfo.getStartTime(), TimeZone.getTimeZone("GMT+00:00"), TimeZone.getDefault(), DateUtil.FMT_YMDHM);
        confItemModel.setStartTime(startTime);
        String endTime = DateUtil.transTimeZone(ckConfListInfo.getEndTime(), TimeZone.getTimeZone("GMT+00:00"), TimeZone.getDefault(), DateUtil.FMT_YMDHM);
        confItemModel.setEndTime(endTime);
        confItemModel.setConfId(ckConfListInfo.getConfId());
        confItemModel.setVmrConferenceId(ckConfListInfo.getVmrConferenceId());
        confItemModel.setConfSubject(ckConfListInfo.getConfSubject());
        confItemModel.setMediaType(ckConfListInfo.getMediaType());
        confItemModel.setScheduserName(ckConfListInfo.getScheduserName());
        confItemModel.setAccessNumber(ckConfListInfo.getAccessNumber());
        confItemModel.setChairmanPwd(ckConfListInfo.getChairmanPwd());
        confItemModel.setGeneralPwd(ckConfListInfo.getGeneralPwd());
        return confItemModel;
    }

    public List<ConfItemModel> transform(List<HwmConfListInfo> ckConfListInfos) {
        if (ckConfListInfos == null || ckConfListInfos.size() == 0) {
            return null;
        }
        List<HwmConfListInfo> temp = new ArrayList<>(ckConfListInfos);
        Collections.sort(temp, (ckConfListInfo1, ckConfListInfo2) -> {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            try {
                Date dt1 = format.parse(ckConfListInfo1.getStartTime());
                Date dt2 = format.parse(ckConfListInfo2.getStartTime());
                if (dt1.getTime() > dt2.getTime()) {
                    return 1;
                } else if (dt1.getTime() < dt2.getTime()) {
                    return -1;
                } else {
                    return 0;
                }
            } catch (Exception e) {
                HCLog.e(TAG, " transform error ");
            }
            return 0;
        });

        ckConfListInfos.clear();
        ckConfListInfos.addAll(temp);

        List<ConfItemModel> confItemModels = new ArrayList<>();
        if (!ckConfListInfos.isEmpty()) {
            for (HwmConfListInfo ckConfListInfo : ckConfListInfos) {
                confItemModels.add(transform(ckConfListInfo));
            }
        }
        return confItemModels;
    }
}
