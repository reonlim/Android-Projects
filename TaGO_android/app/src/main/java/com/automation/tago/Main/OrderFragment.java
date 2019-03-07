package com.automation.tago.Main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.automation.tago.Models.CartItem;
import com.automation.tago.Models.FoodItem;
import com.automation.tago.Models.ShopItem;
import com.automation.tago.R;
import com.automation.tago.Utils.GlobalMemories;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class OrderFragment extends Fragment {

    //preliminaries in this fragment
    private static final String TAG = "OrderFragment";
    private Context mContext;

    //not view object initiation
    private FoodItem foodItem;
    private ShopItem shopItem;
    private CartItem cartItem;

    //view objects initiation
    private TextView shop_name;
    private ImageView food_image;
    private TextView food_name;
    private TextView food_description;
    private TextView food_price;
    private ImageView minus;
    private ImageView add_plus;
    private ImageView cart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        GlobalMemories.getmInstance().main_counter = 3;
        mContext = getActivity();

        startingSetup(view);

        return view;
    }

    private void startingSetup(View view){
        Bundle bundle = gettingDetails();
        if(bundle != null) {
            foodItem = bundle.getParcelable("foodItem");
            shopItem = bundle.getParcelable("shopItem");
        }
        shop_name = view.findViewById(R.id.shop_name);
        food_image = view.findViewById(R.id.food_image);
        food_name = view.findViewById(R.id.food_name);
        food_description = view.findViewById(R.id.food_description);
        food_price = view.findViewById(R.id.food_price);
        minus = view.findViewById(R.id.minus);
        add_plus = view.findViewById(R.id.add_plus);
        cart = view.findViewById(R.id.cart);
        GlobalMemories.getmInstance().food_name = foodItem.getFood_name();
        GlobalMemories.getmInstance().quantity = view.findViewById(R.id.quantity);

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(foodItem.getFood_image(),food_image);

        shop_name.setText(shopItem.getShop_name());
        food_name.setText(foodItem.getFood_name());
        food_description.setText(foodItem.getFood_description());
        food_price.setText(String.valueOf(foodItem.getFood_price()));

        List<CartItem> list_cart_items = GlobalMemories.getmInstance().list_cart_items;
        int position = checkingFoodInlist(list_cart_items);
        if (position == GlobalMemories.getmInstance().list_cart_items.size()){
            GlobalMemories.getmInstance().quantity.setText(String.valueOf(0));
        }else{
            GlobalMemories.getmInstance().quantity.setText(
                    String.valueOf(list_cart_items.get(position).getFood_quantity()));
        }

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalMemories.getmInstance().viewPager.setCurrentItem(1);
            }
        });

        cartItem = new CartItem(shopItem.getShop_name(),foodItem.getFood_image(),
                foodItem.getFood_name(), 0,foodItem.getFood_price());

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<CartItem> list_cart_items = GlobalMemories.getmInstance().list_cart_items;
                int position = checkingFoodInlist(list_cart_items);
                if (position != list_cart_items.size()) {
                    int quantity_ = list_cart_items.get(position).getFood_quantity();
                    if (quantity_ == 1) {
                        list_cart_items.remove(position);
                        GlobalMemories.getmInstance().quantity.setText(String.valueOf(0));
                    } else if (quantity_ > 1) {
                        GlobalMemories.getmInstance().list_cart_items.get(position)
                                .setFood_quantity(quantity_ - 1);
                        GlobalMemories.getmInstance().quantity.setText(
                                String.valueOf(quantity_ - 1));
                    }
                    GlobalMemories.getmInstance().adapterGlobal.notifyDataSetChanged();
                    GlobalMemories.settingUpCartItemNumber(GlobalMemories.getmInstance().cart_item_number);
                }
            }
        });

        add_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int item_cap = GlobalMemories.getmInstance().item_cap;
                if (item_cap != 0 &&
                        Integer.valueOf(GlobalMemories.getmInstance().cart_item_number.getText().toString()) <
                        item_cap) {
                    List<CartItem> list_cart_items = GlobalMemories.getmInstance().list_cart_items;
                    int position = checkingFoodInlist(list_cart_items);
                    if (position == list_cart_items.size()) {
                        cartItem.setFood_quantity(1);
                        GlobalMemories.getmInstance().list_cart_items.add(cartItem);
                        GlobalMemories.getmInstance().quantity.setText(String.valueOf(1));
                    } else {
                        int quantity_ = list_cart_items.get(position).getFood_quantity();
                        GlobalMemories.getmInstance().list_cart_items.get(position).setFood_quantity(
                                quantity_ + 1
                        );
                        GlobalMemories.getmInstance().quantity.setText(String.valueOf(quantity_ + 1));
                    }
                    GlobalMemories.getmInstance().adapterGlobal.notifyDataSetChanged();
                    GlobalMemories.settingUpCartItemNumber(GlobalMemories.getmInstance().cart_item_number);
                }
                else if(Integer.valueOf(GlobalMemories.getmInstance().cart_item_number.getText().toString()) == item_cap){
                    Toast.makeText(mContext, "There can only be " + item_cap + " items per ticket", Toast.LENGTH_SHORT).show();
                }else if(GlobalMemories.getmInstance().item_cap == 0){
                    Toast.makeText(mContext, "Please wait for the ticket system to load.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private int checkingFoodInlist(List<CartItem> list_cart_items){
        int k = -1;
        for(int i = 0; i < list_cart_items.size(); i++){
            if(list_cart_items.get(i).getFood_name().equals(foodItem.getFood_name())){
                k = i;
                break;
            }
        }
        if(k == -1){
            k = list_cart_items.size();
        }
        return k;
    }

    private Bundle gettingDetails(){
        Bundle bundle = getArguments();
        return bundle;
    }

}
