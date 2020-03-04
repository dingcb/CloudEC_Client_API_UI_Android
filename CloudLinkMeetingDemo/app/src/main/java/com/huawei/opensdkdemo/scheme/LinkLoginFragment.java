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
import android.widget.TextView;

import com.huawei.opensdkdemo.DemoUtil;
import com.huawei.opensdkdemo.R;

public class LinkLoginFragment extends DialogFragment {
    public final static String TAG = "LinkLoginFragment";
    View rootView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = inflater.inflate(R.layout.link_login, container, false);

        Button loginBtn = rootView.findViewById(R.id.link_login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                login();
            }
        });
        return rootView;
    }
    private void login(){
        TextView addressView = rootView.findViewById(R.id.linklogin_server_address);
        String serverAddress = addressView.getText().toString();
        if(serverAddress.equals("")) {
            serverAddress = addressView.getHint().toString();
        }
        TextView portView = rootView.findViewById(R.id.linklogin_server_port);
        String portString = portView.getText().toString();
        if(portString.equals("")) {
            portString = portView.getHint().toString();
        }
        int serverPort = Integer.parseInt(portString);
        TextView companyDomainView = rootView.findViewById(R.id.linklogin_company_domain);
        String domain = companyDomainView.getText().toString();
        if(domain.equals("")) {
            domain = companyDomainView.getHint().toString();
        }
        TextView oauthCodeView = rootView.findViewById(R.id.linklogin_code);
        String code = oauthCodeView.getText().toString();
        if(code.equals("")) {
            code = oauthCodeView.getHint().toString();
        }
        if (!serverAddress.isEmpty() && !portString.isEmpty() && !domain.isEmpty() && !code.isEmpty()) {
            try {
                Uri.Builder builder = new Uri.Builder()
                        .scheme("cloudlink")
                        .authority("welinksoftclient")
                        .path("h5page")
                        .appendQueryParameter("page","ssoLogin")
                        .appendQueryParameter("server_url",serverAddress)
                        .appendQueryParameter("port",portString)
                        .appendQueryParameter("domain",domain)
                        .appendQueryParameter("code",code);
                DemoUtil.openCloudLinkWithURL(getContext(), builder.toString());
            } catch (UnsupportedOperationException e) {
                Log.e(TAG,e.getMessage());
            }
        }
    }
}
