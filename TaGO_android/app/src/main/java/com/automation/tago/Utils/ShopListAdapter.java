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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.automation.tago.Main.FoodFragment;
import com.automation.tago.Models.ShopItem;
import com.automation.tago.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class ShopListAdapter extends ArrayAdapter<ShopItem> {

    private static final String TAG = "ShopListAdapter";

    private LayoutInflater mInflater;
    private Context mContext;
    private int layoutResource;
    private FragmentManager fragmentManager;

    public ShopListAdapter(Context context, int resource, @NonNull List<ShopItem> objects, FragmentManager fragmentManager) {
        super(context, resource, objects);

        this.mInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.mContext = mContext;
        this.layoutResource = resource;
        this.fragmentManager = fragmentManager;
    }

    private static class ViewHolder{
        RelativeLayout rel_shop_item;
        ImageView shop_image;
        TextView shop_name, shop_food_genre;
        ShopItem shopItem;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(layoutResource, parent,false);
            holder = new ViewHolder();
            holder.rel_shop_item = convertView.findViewById(R.id.rel_shop_item);
            holder.shop_image = convertView.findViewById(R.id.shop_image);
            holder.shop_name = convertView.findViewById(R.id.shop_name);
            holder.shop_food_genre = convertView.findViewById(R.id.shop_food_genre);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.shopItem = getItem(position);

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(holder.shopItem.getShop_image(),holder.shop_image);

        holder.shop_name.setText(holder.shopItem.getShop_name());

        holder.shop_food_genre.setText(holder.shopItem.getShop_food_genre());

        holder.rel_shop_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FoodFragment fragment = new FoodFragment();
                Bundle args = new Bundle();
                args.putParcelable("shopItem",holder.shopItem);
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
