package com.automation.tago.Main;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.automation.tago.Administration.LoginActivity;
import com.automation.tago.R;
import com.automation.tago.Utils.BottomNavigationViewHelper;
import com.automation.tago.Utils.CartListAdapter;
import com.automation.tago.Utils.GlobalMemories;
import com.automation.tago.Utils.SectionsPagerAdapter;
import com.automation.tago.Utils.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //preliminaries in this fragment
    private static final String TAG ="MainActivity";
    private static final int ACTIVITY_NUM = 0;
    private Context mContext;

    //not view object initiation
    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    //view objects initiation
    public ImageView close;
    private TabLayout tabLayout;
    private SectionsPagerAdapter adapter;
    private BottomNavigationViewEx bottomNavigationViewEx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: starting on MainActivity");
        mContext = MainActivity.this;
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        startingSetup();

    }

    private void startingSetup(){
        close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        GlobalMemories.getmInstance().cart_item_number = findViewById(R.id.cart_item_number);
        GlobalMemories.getmInstance().cart_item_number.setText(String.valueOf(0));
        GlobalMemories.getmInstance().adapterGlobal = new CartListAdapter(mContext,
                R.layout.layout_listview_cartitem,GlobalMemories.getmInstance().list_cart_items);

        initImageLoader();
        setupViewPager();
        setupBottomNavigationView();
        generalSetting();
    }

    private void initImageLoader(){
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    private void setupViewPager(){
        adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new RootFragment());
        adapter.addFragment(new CartFragment());
        GlobalMemories.getmInstance().viewPager = findViewById(R.id.viewpager_container);
        GlobalMemories.getmInstance().viewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.main_layout_tabs);
        tabLayout.setupWithViewPager(GlobalMemories.getmInstance().viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_food);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_cart);
    }

    private void setupBottomNavigationView(){
        bottomNavigationViewEx = findViewById(R.id.bottom_navigation_view);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext,this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    private void generalSetting(){
        Query query = reference.child("General_settings");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                generalSettingsIntermediate(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                generalSettingsIntermediate(dataSnapshot);
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

        GlobalMemories.getmInstance().isListening = 1;
        if(mAuth.getCurrentUser() != null) {
            final Query query1 = reference.child("users")
                    .child(mAuth.getCurrentUser().getUid())
                    .child("ticket");
            query1.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
                    if(dataSnapshot.getKey() != null) {
                        if(dataSnapshot.getKey().equals("date")){
                            GlobalMemories.getmInstance().current_date = dataSnapshot.getValue(String.class);
                        }else if(dataSnapshot.getKey().equals("ticket_quantity")){
                            if(GlobalMemories.getmInstance().current_date.equals(date.format(new Date()))) {
                                GlobalMemories.getmInstance().current_number_of_ticket = dataSnapshot.getValue(int.class);
                                GlobalMemories.getmInstance().isListening = 0;
                            }else{
                                GlobalMemories.getmInstance().current_number_of_ticket = 0;
                                GlobalMemories.getmInstance().current_date = date.format(new Date());
                                reference.child("users")
                                        .child(mAuth.getCurrentUser().getUid())
                                        .child("ticket")
                                        .child("date")
                                        .setValue(date.format(new Date()));
                                reference.child("users")
                                        .child(mAuth.getCurrentUser().getUid())
                                        .child("ticket")
                                        .child("ticket_quantity")
                                        .setValue(0);
                                GlobalMemories.getmInstance().isListening = 0;
                                query1.removeEventListener(this);
                            }
                        }
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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
    }

    private void generalSettingsIntermediate(DataSnapshot dataSnapshot){
        if(dataSnapshot.getKey() != null) {
            if (dataSnapshot.getKey().equals("authorization")) {
                GlobalMemories.getmInstance().authorization = dataSnapshot.getValue(int.class);
            }else if (dataSnapshot.getKey().equals("item_cap")){
                GlobalMemories.getmInstance().item_cap = dataSnapshot.getValue(int.class);
            }else if(dataSnapshot.getKey().equals("ticket_cap")){
                GlobalMemories.getmInstance().ticket_cap = dataSnapshot.getValue(int.class);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(mContext,LoginActivity.class);
            mContext.startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalMemories.getmInstance().isListening = 1;
        if(mAuth.getCurrentUser() != null) {
            final Query query1 = reference.child("users")
                    .child(mAuth.getCurrentUser().getUid())
                    .child("ticket");
            query1.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
                    if(dataSnapshot.getKey() != null) {
                        if(dataSnapshot.getKey().equals("date")){
                            GlobalMemories.getmInstance().current_date = dataSnapshot.getValue(String.class);
                        }else if(dataSnapshot.getKey().equals("ticket_quantity")){
                            if(GlobalMemories.getmInstance().current_date.equals(date.format(new Date()))) {
                                GlobalMemories.getmInstance().current_number_of_ticket = dataSnapshot.getValue(int.class);
                                GlobalMemories.getmInstance().isListening = 0;
                            }else{
                                GlobalMemories.getmInstance().current_number_of_ticket = 0;
                                GlobalMemories.getmInstance().current_date = date.format(new Date());
                                reference.child("users")
                                        .child(mAuth.getCurrentUser().getUid())
                                        .child("ticket")
                                        .child("date")
                                        .setValue(date.format(new Date()));
                                reference.child("users")
                                        .child(mAuth.getCurrentUser().getUid())
                                        .child("ticket")
                                        .child("ticket_quantity")
                                        .setValue(0);
                                GlobalMemories.getmInstance().isListening = 0;
                                query1.removeEventListener(this);
                            }
                        }
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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
    }

    @Override
    public void onBackPressed() {
        if(GlobalMemories.getmInstance().viewPager.getCurrentItem() == 0) {
            if (GlobalMemories.getmInstance().main_counter == 0) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            } else if (GlobalMemories.getmInstance().main_counter != 0 &&
                    GlobalMemories.getmInstance().main_counter < 10) {
                GlobalMemories.getmInstance().main_counter = GlobalMemories.getmInstance().main_counter - 1;
                super.onBackPressed();
            }
        }else{
            GlobalMemories.getmInstance().viewPager.setCurrentItem(0);
        }
    }
}
