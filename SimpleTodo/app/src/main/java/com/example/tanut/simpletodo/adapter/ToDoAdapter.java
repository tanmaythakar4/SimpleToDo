package com.example.tanut.simpletodo.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.tanut.simpletodo.MainActivity;
import com.example.tanut.simpletodo.R;
import com.example.tanut.simpletodo.backend.Item;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static android.R.attr.resource;

/**
 * Created by tanut on 8/4/2017.
 */

public class ToDoAdapter extends ArrayAdapter<Item> {

    Context _activity;

    public ToDoAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
        this._activity = context;
    }

    @Override
    public void notifyDataSetChanged() {
        this.setNotifyOnChange(false);
    this.sort(new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return o2.priority-o1.priority;
            }
        });;
        super.notifyDataSetChanged();
        this.setNotifyOnChange(true);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Item item = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.tvItem = (TextView) convertView.findViewById(R.id.tvItem);
            viewHolder.tvPriority = (TextView) convertView.findViewById(R.id.tvPriority);
            viewHolder.cbSelected = (CheckBox) convertView.findViewById(R.id.cb_selection);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.tvItem.setText(item.item);
        //viewHolder.tvItem.setPaintFlags(viewHolder.tvItem.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

        viewHolder.cbSelected.setChecked(item.status == 1);
        viewHolder.cbSelected.setTag(position);
        viewHolder.cbSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((MainActivity)_activity).checkboxSelected(isChecked,(int)buttonView.getTag());

            }
        });

        List<String> priorites = new ArrayList<String>();
        priorites.add("LOW");
        priorites.add("MEDIUM");
        priorites.add("HIGH");
        viewHolder.tvPriority.setText(priorites.get(item.priority));

        return convertView;
    }

    private static class ViewHolder {
        TextView tvItem;
        TextView tvPriority;
        CheckBox cbSelected;

    }

}
