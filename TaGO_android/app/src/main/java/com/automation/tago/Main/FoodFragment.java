package com.automation.tago.Main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.automation.tago.Models.FoodItem;
import com.automation.tago.Models.ShopItem;
import com.automation.tago.R;
import com.automation.tago.Utils.FoodListAdapter;
import com.automation.tago.Utils.GlobalMemories;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FoodFragment extends Fragment {

    //preliminaries in this fragment
    private static final String TAG = "FoodFragment";
    private Context mContext;

    //not view object initiation
    private ShopItem shopItem;

    //view objects initiation
    private TextView shop_name;
    private TextView shop_food_genre;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_food, container, false);
        mContext = getActivity();
        GlobalMemories.getmInstance().main_counter = 2;

        startingSetup(view);

        return view;
    }

    private void startingSetup(View view){
        shopItem = gettingShopDetails();
        shop_name = view.findViewById(R.id.shop_name);
        shop_food_genre = view.findViewById(R.id.shop_food_genre);
        listView = view.findViewById(R.id.listView);

        shop_name.setText(shopItem.getShop_name());
        shop_food_genre.setText(shopItem.getShop_food_genre());
        gettingFoodDetails();
    }

    private void gettingFoodDetails(){
        final List<FoodItem> list_food_items = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Food_list");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    if(singleSnapshot.getKey() != null) {
                        if (singleSnapshot.getKey().equals(shopItem.getShop_name())) {
                            for (DataSnapshot singleSnapshot2 : singleSnapshot.getChildren()) {
                                list_food_items.add(singleSnapshot2.getValue(FoodItem.class));
                            }
                        }
                    }
                }
                if(list_food_items.size() > 0){
                    setupListView(list_food_items);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private ShopItem gettingShopDetails(){
        Bundle bundle = this.getArguments();

        if(bundle != null){
            return bundle.getParcelable("shopItem");
        }else{
            return null;
        }
    }

    private void setupListView(List<FoodItem> list_food_items){
        FoodListAdapter adapter = new FoodListAdapter(mContext, R.layout.layout_listview_fooditem,
                list_food_items, getFragmentManager(), shopItem);
        listView.setAdapter(adapter);
    }
}
