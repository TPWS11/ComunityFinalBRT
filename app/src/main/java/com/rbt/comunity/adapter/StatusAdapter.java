package com.rbt.comunity.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rbt.comunity.R;
import com.rbt.comunity.model.ModelStatus;

import java.util.List;

public class StatusAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<ModelStatus> statusList;

    public StatusAdapter(Context context, List<ModelStatus> statusList) {
        this.context = context;
        this.statusList = statusList;
    }

    @Override
    public int getCount() {
        return statusList.size();
    }

    @Override
    public Object getItem(int position) {
        return statusList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }
        if (convertView == null && inflater != null) {
            convertView = inflater.inflate(R.layout.user_search_item, null);
        }
        if (convertView != null) {
            TextView name = convertView.findViewById(R.id.tv_user_name_search);
            TextView status = convertView.findViewById(R.id.tv_bio_search);

            ModelStatus status1 = statusList.get(position);
            name.setText(status1.getName());
            status.setText(status1.getStatus());
        }
        return convertView;

    }
}