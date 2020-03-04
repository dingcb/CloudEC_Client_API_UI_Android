package com.huawei.opensdkdemo.sdk;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.huawei.opensdkdemo.R;

public class SSOLoginFragment extends BaseDialogFragment {
    public final static String TAG = "SSOLoginFragment";
    View rootView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = inflater.inflate(R.layout.sdk_sso_login, container, false);

        Button joinBtn = rootView.findViewById(R.id.sso_login_btn);
        joinBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                login(v);
            }
        });
        return rootView;
    }
    private void login(View v){
        Toast.makeText(getActivity(), "规划中", Toast.LENGTH_LONG).show();
    }
}
