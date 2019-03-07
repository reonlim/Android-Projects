package com.automation.tago.Profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.automation.tago.R;
import com.automation.tago.Utils.GlobalMemories;
import com.automation.tago.Utils.SectionStatePagerAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    //preliminaries in this fragment
    private static final String TAG = "ProfileFragment";
    private Context mContext;

    //not view object initiation
    private List<Fragment> fragmentList;

    //view objects initiation
    private TextView user_name;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        GlobalMemories.getmInstance().profile_counter = 0;
        mContext = getActivity();

        startingSetup(view);

        return view;
    }

    private void startingSetup(View view){
        fragmentList = new ArrayList<>();
        user_name = view.findViewById(R.id.user_name);
        listView = view.findViewById(R.id.listView);
        user_name.setText("Student");
        setupFragments();
        setupSettingsList();
    }

    private void setupFragments(){
        fragmentList.add(new UserDetailsFragment());
        fragmentList.add(new PaymentFragment());
        fragmentList.add(new SignOutFragment());
    }

    private void setupSettingsList(){
        final ArrayList<String> options = new ArrayList<>();
        options.add("User Details");
        options.add("Payment");
        options.add("SignOut");

        ArrayAdapter adapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, options);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setViewPager(position, options);
            }
        });
    }

    private void setViewPager(int position, ArrayList<String> options){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout,fragmentList.get(position));
        transaction.addToBackStack(options.get(position));
        transaction.commit();
    }
}
