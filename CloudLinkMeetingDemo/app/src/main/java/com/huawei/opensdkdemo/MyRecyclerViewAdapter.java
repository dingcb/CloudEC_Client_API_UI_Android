package com.huawei.opensdkdemo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import android.view.LayoutInflater;

import com.huawei.opensdkdemo.sdk.ApiPageModel;


public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    private List<ApiPageModel> list;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MyRecyclerViewAdapter(List<ApiPageModel> list) {
        this.list = list;
    }
    @NonNull
    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
        return new MyRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewAdapter.ViewHolder holder, int position) {
        ApiPageModel model = list.get(position);
        holder.mText.setText(model.name);
        int adapterPosition = holder.getAdapterPosition();
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new MyOnClickListener(position, list.get(adapterPosition)));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mText;

        ViewHolder(View itemView) {
            super(itemView);
            mText = itemView.findViewById(R.id.main_item_text);
        }
    }


    private class MyOnClickListener implements View.OnClickListener {
        private int position;
        private ApiPageModel data;

        public MyOnClickListener(int position, ApiPageModel data) {
            this.position = position;
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, position, data);
        }
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position, ApiPageModel data);
    }

}