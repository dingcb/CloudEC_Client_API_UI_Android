package com.huawei.opensdkdemo;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.huawei.opensdkdemo.scheme.LinkJoinFragment;
import com.huawei.opensdkdemo.scheme.LinkLoginFragment;
import com.huawei.opensdkdemo.sdk.ApiPageModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SchemeListFragment extends BaseListFragment {
    private List<String> titles = Arrays.asList("启动","登录","入会");
    private List<DialogFragment> fragments = Arrays.asList(
            null,
            new LinkLoginFragment(),
            new LinkJoinFragment());
    @Override
    public List<ApiPageModel> getItems(){
        List<ApiPageModel> items = new ArrayList<>();
        for (int i = 0; i < titles.size(); i++){
            ApiPageModel model = new ApiPageModel();
            model.name = titles.get(i);
            model.page = fragments.get(i);
            items.add(model);
        }
        return items;
    }
    @Override
    public void handleItemClick(int position, ApiPageModel data) {
        if (position == 0){
            start();
        }else {
            if (data.page != null){
                DialogFragment f =  (DialogFragment) data.page;
                if (!f.isAdded()){
                    f.show(getActivity().getSupportFragmentManager(), null);
                }
            }else {
                Log.e("SchemeListFragment", "not found page");

            }
        }
    }
    private void start(){
        String startUrl = "cloudlink://welinksoftclient/h5page?page=launch";
        DemoUtil.openCloudLinkWithURL(getContext(),startUrl);
    }
}
