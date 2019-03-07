package com.automation.tago.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.automation.tago.Main.MainActivity;
import com.automation.tago.Models.CartItem;
import com.automation.tago.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class CartListAdapter extends ArrayAdapter<CartItem> {

    private static final String TAG = "CartListAdapter";

    private LayoutInflater mInflater;
    private Context mContext;
    private int layoutResource;

    public CartListAdapter(@NonNull Context context, @NonNull int resource, @NonNull List<CartItem> objects) {
        super(context, resource, objects);

        this.mInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.mContext = context;
        this.layoutResource = resource;
    }

    private static class ViewHolder{
        TextView shop_name,food_name,food_quantity,food_unit_price,food_total_price;
        ImageView food_image,food_add,food_minus;
        CartItem cartItem;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ViewHolder holder;

        if(convertView == null){
            convertView = mInflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder();
            holder.shop_name = convertView.findViewById(R.id.shop_name);
            holder.food_name = convertView.findViewById(R.id.food_name);
            holder.food_quantity = convertView.findViewById(R.id.food_quantity);
            holder.food_unit_price = convertView.findViewById(R.id.food_unit_price);
            holder.food_total_price = convertView.findViewById(R.id.food_total_price);
            holder.food_image = convertView.findViewById(R.id.food_image);
            holder.food_add = convertView.findViewById(R.id.add_);
            holder.food_minus = convertView.findViewById(R.id.minus_);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.cartItem = getItem(position);

        holder.shop_name.setText(holder.cartItem.getShop_name());
        holder.food_name.setText(holder.cartItem.getFood_name());
        holder.food_quantity.setText(String.valueOf(holder.cartItem.getFood_quantity()));
        holder.food_unit_price.setText(String.valueOf(holder.cartItem.getFood_price()));
        holder.food_total_price.setText(String.format("%.2f", holder.cartItem.getFood_price()*
                holder.cartItem.getFood_quantity()));

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(holder.cartItem.getFood_image(),holder.food_image);

        holder.food_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int item_cap = GlobalMemories.getmInstance().item_cap;
                if (item_cap != 0 &&
                        Integer.valueOf(GlobalMemories.getmInstance().cart_item_number.getText().toString()) <
                        item_cap) {
                    GlobalMemories.getmInstance().list_cart_items.get(position).setFood_quantity(
                            GlobalMemories.getmInstance().list_cart_items.get(position).getFood_quantity()
                                    + 1
                    );
                    CartListAdapter.this.notifyDataSetChanged();
                    GlobalMemories.getmInstance().quantity.setText(String.valueOf(
                            GlobalMemories.getmInstance().list_cart_items.get(position).getFood_quantity()
                    ));
                    GlobalMemories.settingUpCartItemNumber(GlobalMemories.getmInstance().cart_item_number);
                }else if(Integer.valueOf(GlobalMemories.getmInstance().cart_item_number.getText().toString()) == item_cap){
                    Toast.makeText(mContext, "There can only be " + item_cap + " items per ticket", Toast.LENGTH_SHORT).show();
                }else if(GlobalMemories.getmInstance().item_cap == 0){
                    Toast.makeText(mContext, "Please wait for the ticket system to load.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.food_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GlobalMemories.getmInstance().list_cart_items.get(position).getFood_quantity() != 1) {
                    GlobalMemories.getmInstance().list_cart_items.get(position).setFood_quantity(
                            GlobalMemories.getmInstance().list_cart_items.get(position).getFood_quantity()
                                    - 1
                    );
                    if(holder.cartItem.getFood_name().equals(GlobalMemories.getmInstance().food_name)){
                        GlobalMemories.getmInstance().quantity.setText(String.valueOf(
                                GlobalMemories.getmInstance().list_cart_items.get(position).getFood_quantity()
                        ));
                    }
                }else{
                    GlobalMemories.getmInstance().list_cart_items.remove(position);
                    if(holder.cartItem.getFood_name().equals(GlobalMemories.getmInstance().food_name)){
                        GlobalMemories.getmInstance().quantity.setText(String.valueOf(0));
                    }
                }
                CartListAdapter.this.notifyDataSetChanged();
                GlobalMemories.settingUpCartItemNumber(GlobalMemories.getmInstance().cart_item_number);
            }
        });

        return convertView;
    }
}
