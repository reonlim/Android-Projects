package com.automation.tago.Profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.IntentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.automation.tago.Administration.LoginActivity;
import com.automation.tago.Main.MainActivity;
import com.automation.tago.R;
import com.automation.tago.Utils.GlobalMemories;
import com.google.firebase.auth.FirebaseAuth;

public class SignOutFragment extends Fragment {

    //preliminaries in this fragment
    private static final String TAG = "SignOutFragment";
    private Context mContext;

    //not view object initiation
    private FirebaseAuth mAuth;

    //view objects initiation
    private ImageView close;
    private Button signout_button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signout, container, false);
        GlobalMemories.getmInstance().profile_counter = 1;
        mContext = getActivity();
        mAuth = FirebaseAuth.getInstance();

        startingSetup(view);

        return view;
    }

    private void startingSetup(View view){
        close = view.findViewById(R.id.close);
        signout_button = view.findViewById(R.id.signout_button);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)mContext).onBackPressed();
            }
        });
        signout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(mContext,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.startActivity(intent);
                ((Activity)mContext).finishAffinity();
            }
        });
    }
}
