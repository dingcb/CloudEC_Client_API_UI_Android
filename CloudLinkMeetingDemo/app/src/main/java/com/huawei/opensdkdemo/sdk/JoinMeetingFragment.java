package com.huawei.opensdkdemo.sdk;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.huawei.cloudlink.openapi.HWMSdk;
import com.huawei.cloudlink.openapi.api.param.JoinConfParam;
import com.huawei.hwmconf.presentation.error.ErrorMessageFactory;
import com.huawei.hwmconf.presentation.interactor.JoinConfInteractor;
import com.huawei.hwmconf.presentation.interactor.JoinConfInteractorImpl;
import com.huawei.hwmconf.sdk.util.Utils;
import com.huawei.hwmfoundation.callback.HwmCallback;
import com.huawei.opensdkdemo.DemoUtil;
import com.huawei.opensdkdemo.R;

public class JoinMeetingFragment extends BaseDialogFragment {
    public final static String TAG = "JoinMeetingFragment";
    View rootView;
    private JoinConfInteractor mJoinConfInteractor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mJoinConfInteractor = new JoinConfInteractorImpl();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = inflater.inflate(R.layout.sdk_join_conf, container, false);

        Button joinBtn = rootView.findViewById(R.id.join_btn);
        joinBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                joinConf();
            }
        });
        return rootView;
    }

    private void joinConf(){
        TextView idView = rootView.findViewById(R.id.meeting_id);
        TextView name = rootView.findViewById(R.id.meeting_nickname);
        TextView passwordView = rootView.findViewById(R.id.meeting_pass);
        String mId = idView.getText().toString();
        String nickName = name.getText().toString();
        String password = passwordView.getText().toString();
        if (TextUtils.isEmpty(mId)){
            DemoUtil.showToast(getContext(),"会议ID不能为空");
            return;
        }
        showLoading();

        JoinConfParam joinConfParam = new JoinConfParam()
                .setConfId(mId)
                .setPassword(password)
                .setNickname(nickName)
                .setCameraOn(true)
                .setMicOn(true);
        HWMSdk.getOpenApi(getActivity().getApplication()).joinConf(joinConfParam, new HwmCallback<Void>() {
            @Override
            public void onSuccess(Void ret) {
                dismissLoading();
                dismiss();
            }

            @Override
            public void onFailed(int retCode, String desc) {
                dismissLoading();
                dismiss();
                String err = ErrorMessageFactory.create(Utils.getApp(), retCode);
                if (TextUtils.isEmpty(err)) {
                    err = Utils.getApp().getString(com.huawei.hwmmobileconfui.R.string.conf_join_fail_tip);
                }
                DemoUtil.showToast(getContext(), "加入会议失败: " + err);
            }
        });
    }
}
