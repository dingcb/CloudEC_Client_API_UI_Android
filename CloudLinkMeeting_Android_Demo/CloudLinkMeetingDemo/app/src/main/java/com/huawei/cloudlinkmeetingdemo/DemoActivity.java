package com.huawei.cloudlinkmeetingdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.cloudlink.openapi.api.CLMCompleteHandler;
import com.huawei.cloudlink.openapi.api.CLMResult;
import com.huawei.cloudlink.openapi.api.CloudLinkSDK;
import com.huawei.cloudlink.openapi.api.OpenApiConst;

public class DemoActivity extends Activity {
    private AlertDialog alertDialog;
    private final String TAG = "DemoActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
    }

    public void init(View view){
        this.showLoadingDialog();
        CloudLinkSDK.getOpenApi().clmInit(getApplication(), this, new CLMCompleteHandler() {
            @Override
            public void onCompleted(CLMResult result) {
                DemoActivity.this.dismissLoadingDialog();
                Toast.makeText(DemoActivity.this,"初始化成功",Toast.LENGTH_LONG).show();

            }
        });
    }
    public void login(View view){
        TextView portView = findViewById(R.id.server_port);
        String portString = portView.getText().toString();
        if (portString.equals("")){
            portString = portView.getHint().toString();
        }
        int serverPort = Integer.parseInt(portString);

        TextView addressView = findViewById(R.id.server_address);
        String serverAddress =  addressView.getText().toString();
        if (serverAddress.equals("")){
            serverAddress =  addressView.getHint().toString();
        }

        TextView accountView = findViewById(R.id.account);
        String account = accountView.getText().toString();
        if (account.equals("")){
            account = accountView.getHint().toString();
        }

        TextView passwordView = findViewById(R.id.password);
        String password =  passwordView.getText().toString();
        if (password.equals("")){
            password =  passwordView.getHint().toString();
        }
        this.showLoadingDialog();
        CloudLinkSDK.getOpenApi().clmLogin(serverPort, serverAddress, account, password,
                new CLMCompleteHandler() {
                    @Override
                    public void onCompleted(CLMResult result) {
                        DemoActivity.this.dismissLoadingDialog();
                        if (result.getCode() == 0){
                            Toast.makeText(DemoActivity.this,"登录成功",Toast.LENGTH_LONG).show();
                        }else {
                            Log.e(TAG,"login fail" + result.getMessage());
                            Toast.makeText(DemoActivity.this,"登录失败"+ result.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    public void createConf(View view){
        TextView subjectView = findViewById(R.id.subject);
        String subject = subjectView.getText().toString();
        if (subject.equals("")){
            subject =  subjectView.getHint().toString();
        }
        RadioButton audioBtn = findViewById(R.id.radio_audio);
        int type = audioBtn.isChecked()?1:19;
        Switch needPasswordSwitch = this.findViewById(R.id.switch_need_password);
        boolean needPassword = needPasswordSwitch.isChecked();
        CloudLinkSDK.getOpenApi().clmCreateMeeting(subject, type, needPassword, this.completeHandler);
    }
    public void joinConfById(View view){
        TextView idView = findViewById(R.id.meeting_id);
        TextView passView = findViewById(R.id.meeting_pass);
        String mId = idView.getText().toString();
        String passCode = passView.getText().toString();
        if (mId.equals("")){
            Toast.makeText(this,"会议ID不能为空",Toast.LENGTH_LONG).show();
            return;
        }
        CloudLinkSDK.getOpenApi().clmJoinMeetingById(mId, passCode, this.completeHandler);
    }
    private CLMCompleteHandler completeHandler = new CLMCompleteHandler() {
        @Override
        public void onCompleted(CLMResult result) {
            if (result.getCmd().equals(OpenApiConst.OPEN_EVENT_CREATE_MEETING)){
                Log.i(TAG,"create meeting result" + result.getMessage());
                if (result.getCode() != 0){
                    Log.e(TAG,"create meeting fail" + result.getMessage());
                }
            }else if(result.getCmd().equals(OpenApiConst.OPEN_EVENT_JOIN_MEETING_BY_ID)){
                Log.i(TAG,"join meeting result" + result.getMessage());
                if (result.getCode() != 0){
                    Log.e(TAG,"join meeting fail" + result.getMessage());
                }
            }
        }
    };
    public void showLoadingDialog() {
        if (alertDialog == null){
            alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
            alertDialog.setCancelable(false);
            alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_BACK;
                }
            });
        }
        alertDialog.show();
        alertDialog.setContentView(R.layout.loading_alert);
        alertDialog.setCanceledOnTouchOutside(false);
    }

    public void dismissLoadingDialog() {
        if (null != alertDialog && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }
}
