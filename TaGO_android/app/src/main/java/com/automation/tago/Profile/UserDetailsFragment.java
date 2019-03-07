package com.automation.tago.Profile;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.automation.tago.Models.User;
import com.automation.tago.R;
import com.automation.tago.Utils.GlobalMemories;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserDetailsFragment extends Fragment {

    //preliminaries in this fragment
    private static final String TAG = "UserDetailsFragment";
    private Context mContext;

    //not view object initiation
    private FirebaseAuth mAuth;

    //view objects initiation
    private ImageView close;
    private RelativeLayout rel_user_username;
    private RelativeLayout rel_user_email;
    private RelativeLayout rel_user_password;
    private TextView user_username;
    private TextView user_email;
    private TextView user_password;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_details, container, false);
        GlobalMemories.getmInstance().profile_counter = 1;
        mContext = getActivity();
        mAuth = FirebaseAuth.getInstance();

        startingSetup(view);

        return view;
    }

    private void startingSetup(View view){
        close = view.findViewById(R.id.close);
        rel_user_username = view.findViewById(R.id.rel_user_username);
        rel_user_email = view.findViewById(R.id.rel_user_email);
        rel_user_password = view.findViewById(R.id.rel_user_password);
        user_username = view.findViewById(R.id.user_username);
        user_email = view.findViewById(R.id.user_email);
        user_password = view.findViewById(R.id.user_password);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)mContext).onBackPressed();
            }
        });
        setupTextView();
    }

    private void setupTextView(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("users")
                .orderByChild("user_id")
                .equalTo(mAuth.getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: data type 5:" + dataSnapshot);
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: data type 6:" + singleSnapshot);
                    User user = singleSnapshot.getValue(User.class);
                    if(user != null) {
                        user_username.setText(user.getUsername());
                        user_email.setText(user.getEmail());
                        user_password.setText(user.getPassword());
                    }
                }
                fragmentTransitionSetup(rel_user_username, user_username,0);
                fragmentTransitionSetup(rel_user_email, user_email,1);
                fragmentTransitionSetup(rel_user_password, user_password,2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fragmentTransitionSetup(final RelativeLayout relativeLayout, final TextView textView, final int i){

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeDetailsFragment fragment = new ChangeDetailsFragment();
                Bundle args = new Bundle();
                args.putString("user_details",String.valueOf(textView.getText()));
                args.putInt("options",i);
                fragment.setArguments(args);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout,fragment);
                transaction.addToBackStack("ChangeDetailsFragment");
                transaction.commit();
            }
        });

    }
}
