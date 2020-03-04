package com.huawei.opensdkdemo.scheme;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.huawei.opensdkdemo.DemoUtil;
import com.huawei.opensdkdemo.R;

public class LinkJoinFragment extends DialogFragment {
    public final static String TAG = "LinkJoinFragment";
    View rootView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = inflater.inflate(R.layout.link_join_conf, container, false);

        Button joinBtn = rootView.findViewById(R.id.btn_6);
        joinBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                linkJoinMeeting(v);
            }
        });
        return rootView;
    }

    public void linkJoinMeeting(View view) {
        TextView addressView = rootView.findViewById(R.id.anonymous_server_address);
        String serverAddress =  addressView.getText().toString();
        if (serverAddress.equals("")){
            serverAddress =  addressView.getHint().toString();
        }
        TextView portView = rootView.findViewById(R.id.anonymous_server_port);
        String portString = portView.getText().toString();
        if (portString.equals("")){
            portString = portView.getHint().toString();
        }
        TextView confIdView = rootView.findViewById(R.id.anonymous_conf_id);
        String confId = confIdView.getText().toString();
        Switch micSwitch = rootView.findViewById(R.id.switch_open_mic);
        boolean openMic = micSwitch.isChecked();
        Switch cameraSwitch = rootView.findViewById(R.id.switch_open_camera);
        boolean openCamera = cameraSwitch.isChecked();
        TextView confEnterCodeView = rootView.findViewById(R.id.anonymous_enter_code);
        String enterCode = confEnterCodeView.getText().toString();
        TextView nameView = rootView.findViewById(R.id.anonymous_name);
        String name = nameView.getText().toString();
        if (name.equals("")){
            name = nameView.getHint().toString();
        }
        if (!confId.isEmpty() && !serverAddress.isEmpty() && !portString.isEmpty()){
            try {
                Uri.Builder builder = new Uri.Builder()
                        .scheme("cloudlink")
                        .authority("welinksoftclient")
                        .path("h5page")
                        .appendQueryParameter("page","joinConfByLink")
                        .appendQueryParameter("server_url",serverAddress)
                        .appendQueryParameter("port",portString)
                        .appendQueryParameter("conf_id",confId)
                        .appendQueryParameter("enter_code",enterCode)
                        .appendQueryParameter("name",name)
                        .appendQueryParameter("open_mic",String.valueOf(openMic))
                        .appendQueryParameter("open_camera",String.valueOf(openCamera));
                DemoUtil.openCloudLinkWithURL(getActivity(), builder.toString());
            } catch (UnsupportedOperationException e) {
                Log.e(TAG, e.getMessage());
            }

        }
    }
}
