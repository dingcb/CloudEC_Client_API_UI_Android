package com.huawei.opensdkdemo;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

import java.util.ArrayList;
import java.util.List;

public class DemoActivity extends FragmentActivity{
    private final String TAG = "DemoActivity";
    private AlertDialog alertDialog;
    TabLayout tabLayout;
    ViewPager viewPager;
    List<Fragment> fragments = new ArrayList<>();
    List<String> titles = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.main_tab);
        viewPager = findViewById(R.id.main_vp);
        Fragment sdkFragment = new SDKListFragment();
        Fragment schemeFragment = new SchemeListFragment();
        fragments.add(sdkFragment);
        fragments.add(schemeFragment);
        titles.add("SDK集成Demo");
        titles.add("链接调用Demo");
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragments.get(i);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }
    public void showLoading(){
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
            alertDialog.setCanceledOnTouchOutside(false);

        }
        if (!isFinishing()) {
            alertDialog.show();
            alertDialog.setContentView(R.layout.loading_alert);
        }
    }
    public void dismissLoading(){
        if (null != alertDialog && alertDialog.isShowing()) {
            if (!isFinishing()) {
                alertDialog.dismiss();
            }
        }
    }
}
