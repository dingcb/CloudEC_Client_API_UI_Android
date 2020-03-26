package com.huawei.opensdkdemo.sdk;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.huawei.cloudlink.openapi.HWMSdk;
import com.huawei.cloudlink.openapi.api.param.CLMResult;
import com.huawei.cloudlink.openapi.api.param.CallParam;
import com.huawei.hwmfoundation.callback.HwmCallback;
import com.huawei.opensdkdemo.DemoUtil;
import com.huawei.opensdkdemo.R;

public class CallFragment extends BaseDialogFragment {
    public final static String TAG = "CallFragment";
    View rootView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = inflater.inflate(R.layout.sdk_call, container, false);
        Button callBtn = rootView.findViewById(R.id.start_call);
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCall();
            }
        });
        return rootView;
    }
    private void startCall(){
        TextView numberView = rootView.findViewById(R.id.call_number);
        String number = numberView.getText().toString();
        TextView accountView = rootView.findViewById(R.id.call_account);
        String account = accountView.getText().toString();
        TextView accountUuidView = rootView.findViewById(R.id.call_account_uuid);
        String accountUuid = accountUuidView.getText().toString();
        RadioButton audioBtn = rootView.findViewById(R.id.radio_audio);
        boolean isVideo = !audioBtn.isChecked();
        if (TextUtils.isEmpty(number) && TextUtils.isEmpty(account)){
            DemoUtil.showToast(getContext(), "account and number require");
        }
        showLoading();
        CallParam callParam = new CallParam()
                .setNumber(number)
                .setAccount(account)
                .setCalleeUuid(accountUuid)
                .setVideo(isVideo);
        HWMSdk.getOpenApi(getActivity().getApplication()).startCall(callParam, new HwmCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                dismissLoading();
                Log.i(TAG,"call success");
            }

            @Override
            public void onFailed(int retCode, String desc) {
                DemoUtil.showToast(getContext(), "呼叫失败："+ retCode + ", desc: " + desc);
            }
        });

    }
}
