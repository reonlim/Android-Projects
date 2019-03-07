package com.automation.tago.Dialogs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.automation.tago.R;

public class ReauthenticateFragment extends DialogFragment {

    private static final String TAG = "ReauthenticateFragment";
    private Context mContext;

    public interface OnConfirmPasswordListener{
        void onConfirmPassword(String password);
    }
    OnConfirmPasswordListener mOnConfirmPasswordListener;

    private EditText password;
    private Button confirm_password;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reauthenticate, container, false);
        Log.d(TAG, "onCreateView: starting the dialog");
        mContext = getActivity();

        startingSetup(view);

        return view;
    }

    private void startingSetup(View view){
        password = view.findViewById(R.id.current_password);
        confirm_password = view.findViewById(R.id.confirm_password_button);

        confirm_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!password.getText().toString().equals("")){
                    mOnConfirmPasswordListener.onConfirmPassword(password.getText().toString());
                    getDialog().dismiss();
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnConfirmPasswordListener = (OnConfirmPasswordListener) getTargetFragment();
            Log.d(TAG, "onAttach: successful in attaching the dialog to the fragment");
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
        }
    }
}
