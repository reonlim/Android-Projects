package com.automation.tago.Ticket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.automation.tago.Main.MainActivity;
import com.automation.tago.Models.TicketItem;
import com.automation.tago.R;
import com.automation.tago.Utils.BottomNavigationViewHelper;
import com.automation.tago.Utils.GlobalMemories;
import com.automation.tago.Utils.SectionsPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

public class TicketActivity extends AppCompatActivity implements
    TicketFragment.OnConfirmDeleteTicket{

    //preliminaries in this fragment
    private static final String TAG ="TicketActivity";
    private static final int ACTIVITY_NUM = 1;
    private Context mContext;

    //not view object initiation
    private String ticket_number;
    private int claim_;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference reference;
    private SectionsPagerAdapter adapter;
    private List<String> list_tickets;
    private List<Fragment> mFragmentList;

    //view objects initiation
    private ViewPager viewPager;
    private ImageView next;
    private ImageView back;
    private Button shop;
    private BottomNavigationViewEx bottomNavigationViewEx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        Log.d(TAG, "onCreate: starting on TicketActivity");
        setContentView(R.layout.activity_ticket);

        startingSetup();

    }

    private void startingSetup(){
        settingUpFirebase();
        ticket_number = "";
        list_tickets = new ArrayList<>();
        mFragmentList = new ArrayList<>();
        mContext = TicketActivity.this;
        next = findViewById(R.id.next);
        back = findViewById(R.id.back);
        shop = findViewById(R.id.shop_now);
        adapter = new SectionsPagerAdapter(getSupportFragmentManager(),mFragmentList);
        viewPager = findViewById(R.id.viewpager_container);
        viewPager.setSaveFromParentEnabled(false);
        viewPager.setAdapter(adapter);
        viewPager.setVisibility(View.GONE);
        next.setVisibility(View.GONE);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewPager.getCurrentItem() < adapter.getCount()) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                }
            }
        });
        back.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewPager.getCurrentItem() > 0) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                }
            }
        });
        shop.setVisibility(View.VISIBLE);
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,MainActivity.class);
                mContext.startActivity(intent);
                GlobalMemories.getmInstance().viewPager.setCurrentItem(0);
            }
        });
        setupListView();
        setupBottomNavigationView();
    }

    private void settingUpFirebase(){
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
    }

    private void setupListView(){
        final Query query = reference.child("Cart_list")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if(ticket_number.equals("") || !list_tickets.contains(dataSnapshot.getKey())) {
                        ArrayList<TicketItem> list_ticket_items = new ArrayList<>();
                        ticket_number = dataSnapshot.getKey();
                        list_tickets.add(ticket_number);
                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                            if(singleSnapshot.getKey() != null) {
                                if (!singleSnapshot.getKey().equals("date_time") &&
                                        !singleSnapshot.getKey().equals("claim")) {
                                    list_ticket_items.add(singleSnapshot.getValue(TicketItem.class));
                                } else if (singleSnapshot.getKey().equals("claim")) {
                                    claim_ = singleSnapshot.getValue(int.class);
                                    if (claim_ == 1) {
                                        GlobalMemories.adjustingDatabase(ticket_number);
                                    }
                                }
                            }
                        }
                        if(list_ticket_items.size() != 0 && claim_ != 1) {
                            TicketFragment fragment = new TicketFragment();
                            Bundle args = new Bundle();
                            args.putString("ticket_number",ticket_number);
                            args.putParcelableArrayList("testing 123",list_ticket_items);
                            args.putInt("claim_",claim_);
                            fragment.setArguments(args);
                            adapter.mFragmentList.add(fragment);
                            viewPager.setAdapter(adapter);
                            if(viewPager.getVisibility() != View.VISIBLE){
                                viewPager.setVisibility(View.VISIBLE);
                                next.setVisibility(View.VISIBLE);
                                back.setVisibility(View.VISIBLE);
                                shop.setVisibility(View.GONE);
                            }
                        }
                    }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Log.d(TAG, "onChildChanged: claim changed list:" + list_tickets + " " + dataSnapshot.getKey());
//                for (DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
//                    if (singleSnapshot.getKey().equals("claim")){
//                        claim_ = singleSnapshot.getValue(int.class);
//                        Log.d(TAG, "onChildChanged: claim changed claim_" + claim_);
//                    }
//                }
//                if(claim_ == 1) {
//                        GlobalMemories.getmInstance().claiming = 1;
////                    for (int i = 0; i < list_tickets.size(); i++) {
////                        if (list_tickets.get(i).equals(dataSnapshot.getKey())) {
////                            final int k = i;
////                            final int interval = 1000*30;
////                            Handler handler_this = new Handler();
////                            Runnable runnable = new Runnable(){
////                                public void run() {
////                                    Log.d(TAG, "run: aaa adapter" + adapter.mFragmentList);
////                                    adapter.destroyItem(viewPager,k,adapter.mFragmentList.get(k));
////                                    adapter.removeFragment(k);
////                                    viewPager.setAdapter(adapter);
////                                    Log.d(TAG, "run: aaa adapter" + adapter.mFragmentList);
////                                    Log.d(TAG, "run: claim changed adapter:" + adapter.getCount());
////                                    list_tickets.remove(k);
////                                    if(adapter.getCount() >= 1){
////                                        viewPager.setVisibility(View.VISIBLE);
////                                        shop.setVisibility(View.GONE);
////                                    }else{
////                                        viewPager.setVisibility(View.GONE);
////                                        shop.setVisibility(View.VISIBLE);
////                                    }
////                                }
////                            };
////                            handler_this.postAtTime(runnable, System.currentTimeMillis()+interval);
////                            handler_this.postDelayed(runnable, interval);
////                            claim_ = 0;
////                            break;
////                        }
////                    }
//                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setupBottomNavigationView(){
        bottomNavigationViewEx = findViewById(R.id.bottom_navigation_view);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(mContext,MainActivity.class);
        mContext.startActivity(intent);
        GlobalMemories.getmInstance().viewPager.setCurrentItem(0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(FirebaseAuth.getInstance().getCurrentUser() == null) {
                    finish();
                }
            }
        });
    }

    @Override
    public void onConfirmDeleteTicket(String ticket_number) {
        for (int i = 0; i < list_tickets.size(); i++) {
            if (list_tickets.get(i).equals(ticket_number)) {
                adapter.destroyItem(viewPager, i, adapter.mFragmentList.get(i));
                adapter.removeFragment(i);
                viewPager.setAdapter(adapter);
                list_tickets.remove(i);
                if (adapter.getCount() >= 1) {
                    viewPager.setVisibility(View.VISIBLE);
                    next.setVisibility(View.VISIBLE);
                    back.setVisibility(View.VISIBLE);
                    shop.setVisibility(View.GONE);
                } else {
                    viewPager.setVisibility(View.GONE);
                    next.setVisibility(View.GONE);
                    back.setVisibility(View.GONE);
                    shop.setVisibility(View.VISIBLE);
                }
                claim_ = 0;
                break;
            }
        }
    }
}
