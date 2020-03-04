package com.huawei.opensdkdemo.sdk;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.huawei.cloudlink.openapi.HWMSdk;
import com.huawei.cloudlink.openapi.api.param.CLMParticipant;
import com.huawei.cloudlink.openapi.api.param.ConfType;
import com.huawei.cloudlink.openapi.api.param.CreateConfParam;
import com.huawei.hwmconf.presentation.error.ErrorMessageFactory;
import com.huawei.hwmconf.sdk.model.conf.entity.ConfInfo;
import com.huawei.hwmconf.sdk.util.Utils;
import com.huawei.hwmfoundation.callback.HwmCallback;
import com.huawei.opensdkdemo.DemoUtil;
import com.huawei.opensdkdemo.R;

import java.util.ArrayList;
import java.util.List;

public class CreateMeetingFragment extends BaseDialogFragment {
    public final static String TAG = "CreateMeetingFragment";
    View rootView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = inflater.inflate(R.layout.sdk_create_conf, container, false);

        Button createBtn = rootView.findViewById(R.id.create_conf);
        createBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                createConf();
            }
        });
        Switch attendChooseSwitch = rootView.findViewById(R.id.switch_with_attend);
        attendChooseSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseAttend();
            }
        });
        View attendView = rootView.findViewById(R.id.attend_view);
        attendView.setVisibility(attendChooseSwitch.isChecked()?View.VISIBLE:View.GONE);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Switch attendSwitch = rootView.findViewById(R.id.switch_with_attend);
        View attendView = rootView.findViewById(R.id.attend_view);
        attendView.setVisibility(attendSwitch.isChecked()?View.VISIBLE:View.GONE);
    }

    private void createConf(){
        TextView subjectView = rootView.findViewById(R.id.subject);
        String subject = subjectView.getText().toString();
        if (TextUtils.isEmpty(subject)){
            subject =  subjectView.getHint().toString();
        }
        RadioButton audioBtn = rootView.findViewById(R.id.radio_audio);
//        ConfType type = audioBtn.isChecked()?1:19;
        ConfType confType = audioBtn.isChecked()?ConfType.CONF_AUDIO:ConfType.CONF_VIDEO_AND_DATA;
        Switch needPasswordSwitch = rootView.findViewById(R.id.switch_need_password);
        boolean needPassword = needPasswordSwitch.isChecked();
        Switch needWithMemberSwitch = rootView.findViewById(R.id.switch_with_attend);
        boolean needWithMember = needWithMemberSwitch.isChecked();
        if (needWithMember){
            TextView nameView = rootView.findViewById(R.id.attend_name);
            String name = nameView.getText().toString();
            if (TextUtils.isEmpty(name)){
                name =  nameView.getHint().toString();
            }
            TextView numberView = rootView.findViewById(R.id.attend_number);
            String number = numberView.getText().toString();
            if (TextUtils.isEmpty(number)){
                number =  numberView.getHint().toString();
            }
            CLMParticipant member = new CLMParticipant(number,name);
            List<CLMParticipant> members = new ArrayList<>(1);
            members.add(member);
            showLoading();

            CreateConfParam createConfParam = new CreateConfParam()
                    .setSubject(subject)
                    .setConfType(confType)
                    .setNeedPassword(needPassword);
//                    .setMembers(members);
            HWMSdk.getOpenApi(getActivity()).createConf(createConfParam, this.completeHandler);
        }else {
            showLoading();
            CreateConfParam createConfParam = new CreateConfParam()
                    .setSubject(subject)
                    .setConfType(confType)
                    .setNeedPassword(needPassword);
            HWMSdk.getOpenApi(getActivity()).createConf(createConfParam, this.completeHandler);
        }
    }

    private HwmCallback<ConfInfo> completeHandler = new HwmCallback<ConfInfo>() {
        @Override
        public void onSuccess(ConfInfo confInfo) {
            dismissLoading();
            DemoUtil.showToast(getContext(), "创建会议成功: 会议id：" + confInfo.getConfId() +
                    ";会议密码：" + confInfo.getConfPwd());
        }

        @Override
        public void onFailed(int retCode, String desc) {
            dismissLoading();
            String err = ErrorMessageFactory.create(Utils.getApp(), retCode);
            if (TextUtils.isEmpty(err)) {
                err = Utils.getApp().getString(com.huawei.hwmmobileconfui.R.string.conf_create_error);
            }
            DemoUtil.showToast(getContext(), "创建会议失败: " + err);
        }
    };
    public void showChooseAttend(){
        Switch attendSwitch = rootView.findViewById(R.id.switch_with_attend);
        View attendView = rootView.findViewById(R.id.attend_view);
        attendView.setVisibility(attendSwitch.isChecked()?View.VISIBLE:View.GONE);
    }
}
