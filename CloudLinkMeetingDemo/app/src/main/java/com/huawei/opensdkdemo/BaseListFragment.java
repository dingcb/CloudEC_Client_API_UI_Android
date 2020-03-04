package com.huawei.opensdkdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huawei.opensdkdemo.sdk.ApiPageModel;

import java.util.List;

public class BaseListFragment extends Fragment {
    RecyclerView mRecyclerView;
    MyRecyclerViewAdapter mAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_list, container, false);
        mRecyclerView = view.findViewById(R.id.main_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new MyRecyclerViewAdapter(getItems());
        mRecyclerView.setAdapter(mAdapter);
        //设置分割线
        mRecyclerView.addItemDecoration(DemoUtil.getRecyclerViewDivider(getContext(), R.drawable.main_recycler_decoration));
        mAdapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ApiPageModel data) {
                handleItemClick(position, data);
            }
        });
        return view;
    }
    public void handleItemClick(int position, ApiPageModel data){

    }
    public List <ApiPageModel> getItems(){
        return null;
    }

}
