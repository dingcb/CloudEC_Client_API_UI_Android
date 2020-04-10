package com.huawei.opensdkdemo.sdk;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huawei.opensdkdemo.R;

import java.util.ArrayList;
import java.util.List;

public class ConfListItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ConfItemModel> items;

    public ConfListItemAdapter() {
        this.items = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.conf_item, viewGroup, false);
        return new ContentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ConfItemModel item = items.get(i);
        int differDays = item.getDiffDays();
        String chairManTips = String.format("chairman： %1$s", item.getScheduserName());
        ((ContentViewHolder) viewHolder).startTime.setText(item.getHMStartTime());
        ((ContentViewHolder) viewHolder).startTimeSuper.setVisibility(View.GONE);
        if (differDays != 0) {
            ((ContentViewHolder) viewHolder).endTime.setText(item.getHMEndTime());
            ((ContentViewHolder) viewHolder).endTimeSuper.setVisibility(View.VISIBLE);
            ((ContentViewHolder) viewHolder).endTimeSuper.setText(Html.fromHtml("<sup>" + "+" + differDays + " </sup>"));
        } else {
            ((ContentViewHolder) viewHolder).endTimeSuper.setVisibility(View.GONE);
            ((ContentViewHolder) viewHolder).endTime.setText(item.getHMEndTime());
        }
        ((ContentViewHolder) viewHolder).confChairman.setText(chairManTips);
        ((ContentViewHolder) viewHolder).confSubject.setText(item.getConfSubject());
        ((ContentViewHolder) viewHolder).mediaType.setImageResource(item.getMediaTypeImg());

    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    public void updateConfList(List<ConfItemModel> list) {
        if (items != null) {
            items.clear();
        }
        if (list != null && list.size() > 0) {
            if (items != null) {
                items.addAll(list);
            }
        }
        notifyDataSetChanged();
    }

    class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView startTime;
        TextView startTimeSuper;
        TextView endTime;
        TextView endTimeSuper;
        TextView confSubject;
        TextView confChairman;
        ImageView mediaType;

        ContentViewHolder(View itemView) {
            super(itemView);
            startTime = itemView.findViewById(R.id.demo_conf_item_start_time_text);
            startTimeSuper = itemView.findViewById(R.id.demo_conf_item_start_time_text_super);
            endTime = itemView.findViewById(R.id.demo_conf_item_end_time_text);
            endTimeSuper = itemView.findViewById(R.id.demo_conf_item_end_time_text_super);
            confSubject = itemView.findViewById(R.id.demo_conf_item_conf_subject_text);
            confChairman = itemView.findViewById(R.id.demo_conf_item_chairman_text);
            mediaType = itemView.findViewById(R.id.demo_conf_item_media_type_img);
        }
    }

}
