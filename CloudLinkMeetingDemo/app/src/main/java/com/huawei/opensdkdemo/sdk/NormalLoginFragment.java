package com.huawei.opensdkdemo.sdk;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.huawei.cloudlink.openapi.HWMSdk;
import com.huawei.hwmbiz.HWMBizSdk;
import com.huawei.hwmbiz.eventbus.LoginResult;
import com.huawei.hwmbiz.exception.Error;
import com.huawei.hwmcommonui.ui.popup.dialog.base.BaseDialogBuilder;
import com.huawei.hwmfoundation.callback.HwmCallback;
import com.huawei.opensdkdemo.DemoUtil;
import com.huawei.opensdkdemo.R;

public class NormalLoginFragment extends BaseDialogFragment {
    public final static String TAG = "NormalLoginFragment";
    View rootView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = inflater.inflate(R.layout.sdk_normal_login, container, false);

        Button joinBtn = rootView.findViewById(R.id.btn_2);
        joinBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                login(v);
            }
        });
        return rootView;
    }
    
    private void login(View v){
        TextView accountView =  rootView.findViewById(R.id.account);
        String account = accountView.getText().toString();
        if (TextUtils.isEmpty(account)){
            account = accountView.getHint().toString();
        }

        TextView passwordView =  rootView.findViewById(R.id.password);
        String password =  passwordView.getText().toString();
        if (password.equals("")){
            password =  passwordView.getHint().toString();
        }
        showLoading();
        HWMSdk.getOpenApi(getActivity()).login(account, password, new HwmCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                dismissLoading();
                if (loginResult != null ) {
                    DemoUtil.showToast(getContext(), "登录成功" + loginResult.getUserUuid());
                } else {
                    DemoUtil.showToast(getContext(), "您已经登录");
                }
            }

            @Override
            public void onFailed(int retCode, String desc) {
                dismissLoading();
                Error error = HWMBizSdk.getLoginApi().convertErrorCodeToUI(retCode);
                String errorTip = getLoginErrTips(error);
                new BaseDialogBuilder(getActivity())
                        .setMessage(getLoginErrTips(error))
                        .setMessagePosition(Gravity.CENTER)
                        .addAction("确认", (dialog, button, index) -> dialog.dismiss())
                        .show();
            }
        });
    }

    private String getLoginErrTips(Error error){
        switch (error){
            case Login_ERR_GENERAL:
                return getString(R.string.login_err_general);
            case Login_ERR_PARAM_ERROR:
                return getString(R.string.login_err_param);
            case Login_REQUEST_TIMEOUT:
                return getString(R.string.login_err_request_timeout);
            case Login_ERR_NETWORK_ERROR:
                return getString(R.string.login_err_network_err);
            case Login_ERR_CERTIFICATE_VERIFY_FAILED:
                return getString(R.string.login_err_certificate_verify_failed);
            case Login_ERR_ACCOUNT_OR_PASSWORD_ERROR:
                return getString(R.string.login_err_account_pwd_err);
            case Login_ERR_ACCOUNT_LOCKED:
                return getString(R.string.login_err_account_locked);
            case Login_ERR_IP_OR_DEVICE_FORBIDDEN:
                return getString(R.string.login_err_ip_or_device_forbidden);
            case Login_ERR_CORP_OR_ACCOUNT_DIACTIVE:
                return getString(R.string.login_err_corp_or_account_inactive);
            case Login_ERR_SERVER_UPGRADING:
                return getString(R.string.login_err_server_upgrade);
            case Login_ERR_NEED_MODIFY_PASSWORD:
                return  getString(R.string.login_err_fist_login_change_password);
        }
        return getString(R.string.login_err_general);
    }

}
