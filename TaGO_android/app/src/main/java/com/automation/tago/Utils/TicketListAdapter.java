package com.automation.tago.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.automation.tago.Models.TicketItem;
import com.automation.tago.R;

import java.util.ArrayList;
import java.util.List;

public class TicketListAdapter extends ArrayAdapter<TicketItem> {

    private static final String TAG = "TicketListAdapter";

    private LayoutInflater mInflater;
    private Context mContext;
    private int layoutResource;

    public TicketListAdapter(@NonNull Context context, @NonNull int resource, @NonNull ArrayList<TicketItem> objects) {
        super(context, resource, objects);

        this.mInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.mContext = context;
        this.layoutResource = resource;
    }

    private static class ViewHolder{
        TextView food_name,food_quantity;
        TicketItem ticketItem;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ViewHolder holder;

        if(convertView == null){
            convertView = mInflater.inflate(layoutResource, parent,false);
            holder = new ViewHolder();

            holder.food_name = (TextView)convertView.findViewById(R.id.food_name);
            holder.food_quantity = (TextView)convertView.findViewById(R.id.food_quantity);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.ticketItem = getItem(position);
        holder.food_name.setText(holder.ticketItem.getFood_name());
        holder.food_quantity.setText(String.valueOf(holder.ticketItem.getFood_quantity()));

        return convertView;
    }
}
