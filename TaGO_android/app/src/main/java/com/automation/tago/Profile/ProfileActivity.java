package com.automation.tago.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.automation.tago.Main.MainActivity;
import com.automation.tago.R;
import com.automation.tago.Utils.BottomNavigationViewHelper;
import com.automation.tago.Utils.GlobalMemories;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class ProfileActivity extends AppCompatActivity {

    //preliminaries in this fragment
    private static final String TAG ="ProfileActivity";
    private static final int ACTIVITY_NUM = 2;
    private Context mContext;

    //view objects initiation
    private BottomNavigationViewEx bottomNavigationViewEx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: starting on ProfileActivity");
        mContext = ProfileActivity.this;

        startingSetup();
    }

    private void startingSetup(){
        ProfileFragment fragment = new ProfileFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout,fragment);
        transaction.commit();

        setupBottomNavigationView();
    }

    private void setupBottomNavigationView(){
        bottomNavigationViewEx = findViewById(R.id.bottom_navigation_view);
        Log.d(TAG, "setupBottomNavigationView: class:" + bottomNavigationViewEx.getClass());
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    @Override
    public void onBackPressed() {
        if(GlobalMemories.getmInstance().profile_counter == 0){
            Intent intent = new Intent(mContext,MainActivity.class);
            mContext.startActivity(intent);
            GlobalMemories.getmInstance().viewPager.setCurrentItem(0);
        }else{
            super.onBackPressed();
        }
    }
}
