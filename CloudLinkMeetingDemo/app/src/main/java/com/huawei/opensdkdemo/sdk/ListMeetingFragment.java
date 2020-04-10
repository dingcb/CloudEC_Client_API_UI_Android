package com.huawei.opensdkdemo.sdk;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.huawei.conflogic.HwmConfListInfo;
import com.huawei.hwmbiz.HWMBizSdk;
import com.huawei.hwmconf.presentation.view.component.CustomLayoutManager;
import com.huawei.hwmconf.sdk.common.GlobalHandler;
import com.huawei.hwmfoundation.HwmContext;
import com.huawei.opensdkdemo.DemoApplication;
import com.huawei.opensdkdemo.DemoUtil;
import com.huawei.opensdkdemo.IConfListUpdate;
import com.huawei.opensdkdemo.R;

import java.util.List;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

public class ListMeetingFragment extends BaseDialogFragment {
    public final static String TAG = "ListMeetingFragment";
    private RecyclerView mConfListRecyclerView;
    private ConfListItemAdapter mConfListAdapter;
    View rootView;

    private IConfListUpdate confListUpdate = new IConfListUpdate() {
        @Override
        public void onConfListUpdateNotify(List<HwmConfListInfo> list) {
            Log.i(TAG, "onConfListUpdateNotify: " + (list == null ? 0 : list.size()));
            updateMeetingList(list);
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = inflater.inflate(R.layout.sdk_conf_list, container, false);
        mConfListRecyclerView = rootView.findViewById(R.id.demo_conf_list_recyclerview);
        mConfListAdapter = new ConfListItemAdapter();
        if (mConfListRecyclerView != null) {
            mConfListRecyclerView.setAdapter(mConfListAdapter);
        }
        CustomLayoutManager customLayoutManager = new CustomLayoutManager(getContext());
        customLayoutManager.setSpeedRatio(0.5);
        mConfListRecyclerView.setLayoutManager(customLayoutManager);
        DemoApplication.registerConfListUpdateListener(confListUpdate);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                List<HwmConfListInfo> ckConfListInfos = HWMBizSdk.getBizOpenApi().getConfList();
                Log.i(TAG, "getConfList: " + (ckConfListInfos == null ? 0 : ckConfListInfos.size()));
                updateMeetingList(ckConfListInfos);
            }
        }, 50);
        return rootView;
    }


    public void updateMeetingList(List<HwmConfListInfo> ckConfListInfos) {
        List<ConfItemModel> confItemModels = DemoUtil.getInstance().transform(ckConfListInfos);
        if (mConfListAdapter != null) {
            HwmContext.getInstance().runOnMainThread(() -> mConfListAdapter.updateConfList(confItemModels));
        }
    }

    @Override
    public void onDestroy() {
        Log.i("TAG", "onDestroy");
        super.onDestroy();
        DemoApplication.removeConfListUpdateListener(confListUpdate);
    }
}
