package com.huawei.opensdkdemo.sdk;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.huawei.cloudlink.openapi.HWMSdk;
import com.huawei.cloudlink.openapi.api.param.JoinConfParam;
import com.huawei.hwmcommonui.ui.popup.dialog.base.ButtonParams;
import com.huawei.hwmcommonui.ui.popup.dialog.edit.EditDialog;
import com.huawei.hwmcommonui.ui.popup.dialog.edit.EditDialogBuilder;
import com.huawei.hwmconf.presentation.error.ErrorMessageFactory;
import com.huawei.hwmconf.presentation.interactor.JoinConfInteractor;
import com.huawei.hwmconf.presentation.interactor.JoinConfInteractorImpl;
import com.huawei.hwmconf.sdk.SimpleConfListener;
import com.huawei.hwmconf.sdk.model.conf.entity.JoinConfResult;
import com.huawei.hwmconf.sdk.util.Utils;
import com.huawei.hwmfoundation.callback.HwmCallback;
import com.huawei.hwmlogger.HCLog;
import com.huawei.opensdkdemo.DemoUtil;
import com.huawei.opensdkdemo.R;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class JoinMeetingFragment extends BaseDialogFragment {
    public final static String TAG = "JoinMeetingFragment";
    View rootView;
    private JoinConfInteractor mJoinConfInteractor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mJoinConfInteractor = new JoinConfInteractorImpl();
        mJoinConfInteractor.getConfApi().addListener(mSimpleConfListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mJoinConfInteractor.getConfApi().removeListener(mSimpleConfListener);
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
        String mId = idView.getText().toString();
        String nickName = name.getText().toString();
        if (TextUtils.isEmpty(mId)){
            DemoUtil.showToast(getContext(),"会议ID不能为空");
            return;
        }
        showLoading();

        JoinConfParam joinConfParam = new JoinConfParam()
                .setConfId(mId)
                .setNickname(nickName)
                .setCameraOn(true)
                .setMicOn(true);
        HWMSdk.getOpenApi(getActivity()).joinConf(joinConfParam, new HwmCallback<Void>() {
            @Override
            public void onSuccess(Void ret) {
                dismissLoading();
            }

            @Override
            public void onFailed(int retCode, String desc) {
                dismissLoading();
                String err = ErrorMessageFactory.create(Utils.getApp(), retCode);
                if (TextUtils.isEmpty(err)) {
                    err = Utils.getApp().getString(com.huawei.hwmmobileconfui.R.string.conf_join_fail_tip);
                }
                DemoUtil.showToast(getContext(), "加入会议失败: " + err);
            }
        });
    }

    private SimpleConfListener mSimpleConfListener = new SimpleConfListener() {
        @Override
        public void onJoinConfNeedPwdNotify(JoinConfResult joinConfResult) {
            Observable.just(joinConfResult)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(joinConfResult1 -> handleJoinConfNeedPwdNotify(joinConfResult1));

        }
    };

    public void showPwdEditDialog(String title, String hint, ButtonParams.OnDialogButtonClick cancelListener, ButtonParams.OnDialogButtonClick sureListener) {
        new EditDialogBuilder(getActivity())
                .setTitle(title)
                .setHint(hint)
                .setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD)
                .addAction(getString(com.huawei.hwmmobileconfui.R.string.conf_dialog_cancle_btn_str), cancelListener)
                .addAction(getString(com.huawei.hwmmobileconfui.R.string.conf_dialog_confirm_btn_str), sureListener)
                .show();
    }

    private void handleJoinConfNeedPwdNotify(JoinConfResult joinConfResult) {
        HCLog.i(TAG, " handleJoinConfNeedPwdNotify ");
        String title = Utils.getApp().getString(com.huawei.hwmmobileconfui.R.string.conf_join_input_pwd_title);
        String hint = Utils.getApp().getString(com.huawei.hwmmobileconfui.R.string.conf_join_input_pwd_hint);
        dismissLoading();
        showPwdEditDialog(
                title,
                hint,
                (dialog, button, i) -> {
                    dialog.dismiss();
                },
                (dialog, button, i) -> {

                    JoinConfParam joinConfParam = new JoinConfParam()
                            .setConfId(joinConfResult.getConfId())
//                            .setAccessCode(joinConfResult.getAccessCode())
                            .setPassword(((EditDialog) dialog).getInput())
                            .setNickname(((EditText)rootView.findViewById(R.id.meeting_nickname))
                                    .getText().toString())
                            .setCameraOn(true)
                            .setMicOn(true);
                    HWMSdk.getOpenApi(getActivity()).joinConf(joinConfParam, new HwmCallback<Void>() {
                        @Override
                        public void onSuccess(Void ret) {
                            dismissLoading();
                        }

                        @Override
                        public void onFailed(int retCode, String desc) {
                            dismissLoading();
                            String err = ErrorMessageFactory.create(Utils.getApp(), retCode);
                            if (TextUtils.isEmpty(err)) {
                                err = Utils.getApp().getString(com.huawei.hwmmobileconfui.R.string.conf_join_fail_tip);
                            }
                            DemoUtil.showToast(getContext(), "加入会议失败: " + err);
                        }
                    });
                    dialog.dismiss();
                });

    }

}
