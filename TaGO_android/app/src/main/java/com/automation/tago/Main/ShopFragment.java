package com.automation.tago.Main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.automation.tago.Models.ShopItem;
import com.automation.tago.R;
import com.automation.tago.Utils.GlobalMemories;
import com.automation.tago.Utils.ShopListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment {

    //preliminaries in this fragment
    private static final String TAG = "ShopFragment";
    private Context mContext;

    //view objects initiation
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_shop, container, false);
        Log.d(TAG, "onCreateView: inflating ShopFragment");
        GlobalMemories.getmInstance().main_counter = 1;
        mContext = getActivity();

        startingSetup(view);

        return view;
    }

    private void startingSetup(View view){
        listView = view.findViewById(R.id.listView);
        gettingShopDetails();
    }

    private void gettingShopDetails(){
        final List<ShopItem> list_shop_items = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Shop_list");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    if(singleSnapshot.getKey() != null) {
                        if (singleSnapshot.getKey().equals(gettingUniName())) {
                            for (DataSnapshot singleSnapshot2 : singleSnapshot.getChildren()) {
                                list_shop_items.add(singleSnapshot2.getValue(ShopItem.class));
                            }
                        }
                    }
                }
                if(list_shop_items.size() > 0){
                    setupListView(list_shop_items);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String gettingUniName(){
        Bundle bundle = getArguments();
        if(bundle != null){
            return bundle.getString("uni");
        }else{
            return  null;
        }
    }

    private void setupListView(List<ShopItem> list_shop_items){
        ShopListAdapter adapter = new ShopListAdapter(mContext,R.layout.layout_listview_shopitem,
                list_shop_items,getFragmentManager());
        listView.setAdapter(adapter);
    }
}
