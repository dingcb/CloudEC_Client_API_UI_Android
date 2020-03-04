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

public class DemoUtil {
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
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();

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
}
