package com.automation.tago.Utils;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.automation.tago.Main.OrderFragment;
import com.automation.tago.Models.FoodItem;
import com.automation.tago.Models.ShopItem;
import com.automation.tago.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class FoodListAdapter extends ArrayAdapter<FoodItem> {

    private static final String TAG = "FoodListAdapter";

    private LayoutInflater mInflater;
    private Context mContext;
    private int layoutResource;
    private FragmentManager fragmentManager;
    private ShopItem shopItem;

    public FoodListAdapter(@NonNull Context context, @NonNull int resource, @NonNull List<FoodItem> objects, FragmentManager fragmentManager, ShopItem shopItem) {
        super(context, resource, objects);

        this.mInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.mContext = context;
        this.layoutResource = resource;
        this.fragmentManager = fragmentManager;
        this.shopItem = shopItem;
    }

    private static class ViewHolder{
        RelativeLayout rel_food_item;
        TextView food_name, food_description, food_price;
        SquareImageView food_image;
        FoodItem foodItem;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ViewHolder holder;

        if(convertView == null){
            convertView = mInflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder();
            holder.rel_food_item = convertView.findViewById(R.id.rel_food_item);
            holder.food_name = convertView.findViewById(R.id.food_name);
            holder.food_description = convertView.findViewById(R.id.food_description);
            holder.food_price = convertView.findViewById(R.id.food_price);
            holder.food_image = convertView.findViewById(R.id.food_image);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.foodItem = getItem(position);

        holder.food_name.setText(holder.foodItem.getFood_name());
        holder.food_description.setText(holder.foodItem.getFood_description());
        holder.food_price.setText(String.valueOf(holder.foodItem.getFood_price()));
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(holder.foodItem.getFood_image(),holder.food_image);

        holder.rel_food_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderFragment fragment = new OrderFragment();
                Bundle args = new Bundle();
                args.putParcelable("foodItem",holder.foodItem);
                args.putParcelable("shopItem",shopItem);
                fragment.setArguments(args);

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame_layout,fragment);
                transaction.addToBackStack("FoodFragment");
                transaction.commit();
            }
        });

        return convertView;
    }
}
