package com.huawei.opensdkdemo.sdk;

import android.support.v4.app.DialogFragment;

import com.huawei.opensdkdemo.DemoActivity;

public class BaseDialogFragment extends DialogFragment {
    public void showLoading(){
        if (getActivity() != null){
            ((DemoActivity)getActivity()).showLoading();
        }
    }
    public void dismissLoading(){
        if (getActivity() != null){
            ((DemoActivity)getActivity()).dismissLoading();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // 如果因为页面跳转dialog没有消失，这里重新dismiss一次
        dismissLoading();
    }

    @Override
    public void onStart() {
        super.onStart();
        dismissLoading();
    }
}
