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
import android.widget.GridView;

import com.automation.tago.Models.Uni;
import com.automation.tago.R;
import com.automation.tago.Utils.GlobalMemories;
import com.automation.tago.Utils.UniListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL;

public class UniFragment extends Fragment {

    //preliminaries in this fragment
    private static final String TAG = "UniFragment";
    private static int NUM_GRID_COL = 2;
    private Context mContext;

    //view objects initiation
    private GridView gridView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_uni, container, false);
        GlobalMemories.getmInstance().main_counter = 0;
        mContext = getContext();

        startingSetup(view);

        return view;
    }

    private void startingSetup(View view){
        gridView = view.findViewById(R.id.gridView);
        gettingUniDetails();
    }

    private void gettingUniDetails(){
        final List<Uni> unis = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Uni");
        Log.d(TAG, "gettingUniDetails: query:" + query);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: data type:" + dataSnapshot);
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    unis.add(singleSnapshot.getValue(Uni.class));
                }

                setupGridView(unis);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setupGridView(List<Uni> unis){
        UniListAdapter adapter = new UniListAdapter(mContext,R.layout.layout_grid_imageview,unis,
                getFragmentManager());
        gridView.setAdapter(adapter);
    }
}
