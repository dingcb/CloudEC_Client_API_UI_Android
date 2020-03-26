package com.huawei.opensdkdemo;

import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.huawei.cloudlink.openapi.HWMSdk;
import com.huawei.cloudlink.openapi.api.param.CLMResult;
import com.huawei.hwmfoundation.callback.HwmCallback;
import com.huawei.opensdkdemo.sdk.ApiPageModel;
import com.huawei.opensdkdemo.sdk.BaseDialogFragment;
import com.huawei.opensdkdemo.sdk.CallFragment;
import com.huawei.opensdkdemo.sdk.CreateMeetingFragment;
import com.huawei.opensdkdemo.sdk.JoinMeetingFragment;
import com.huawei.opensdkdemo.sdk.NormalLoginFragment;
import com.huawei.opensdkdemo.sdk.SSOLoginFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import loginlogic.LoginCompletedResult;

public class SDKListFragment extends BaseListFragment {
    public final static String TAG = "SDKListFragment";
    private List<String> titles = Arrays.asList("登录","SSO登录","创建会议","加入会议","呼叫","登出");
    private List<BaseDialogFragment> fragments = Arrays.asList(
            new NormalLoginFragment(),
            new SSOLoginFragment(),
            new CreateMeetingFragment(),
            new JoinMeetingFragment(),
            new CallFragment(),
            null);
    @Override
    public List<ApiPageModel> getItems() {
        List<ApiPageModel> models = new ArrayList<>();
        for (int i = 0; i < titles.size(); i ++){
            ApiPageModel item = new ApiPageModel();
            item.name = titles.get(i);
            item.page = fragments.get(i);
            models.add(item);
        }
        return models;
    }

    @Override
    public void handleItemClick(int position, ApiPageModel data) {
        if(position == titles.size() - 1) {
            logout();
        }
        else {
            if (data.page != null){
               DialogFragment f =  data.page;
               if (!f.isAdded()){
                   f.show(getActivity().getSupportFragmentManager(), null);
               }
            }else {
                Log.e("SDKListFragment", "not found page");

            }
        }
    }

    private void logout() {
        if (getActivity() != null){
            ((DemoActivity)getActivity()).showLoading();
        }
        HWMSdk.getOpenApi(getActivity().getApplication()).logout(new HwmCallback<LoginCompletedResult>() {
            @Override
            public void onSuccess(LoginCompletedResult result) {
                if (getActivity() != null){
                    ((DemoActivity)getActivity()).dismissLoading();
                }
                DemoUtil.showToast(getContext(), "登出成功");
                ((DemoActivity)getActivity()).dismissLoading();
            }

            @Override
            public void onFailed(int retCode, String desc) {
                DemoUtil.showToast(getContext(), "等出失败 : " + retCode + "; " + desc);
                ((DemoActivity)getActivity()).dismissLoading();
            }
        });
    }

}
