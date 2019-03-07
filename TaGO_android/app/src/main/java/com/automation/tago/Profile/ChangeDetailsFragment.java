package com.automation.tago.Profile;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.automation.tago.Dialogs.ReauthenticateFragment;
import com.automation.tago.R;
import com.automation.tago.Utils.GlobalMemories;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ChangeDetailsFragment extends Fragment implements
    ReauthenticateFragment.OnConfirmPasswordListener{

    @Override
    public void onConfirmPassword(String password) {

        AuthCredential credential = EmailAuthProvider
                .getCredential(mAuth.getCurrentUser().getEmail(),password);

        mAuth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if(gettingOptions() == 1) {
                        mAuth.getCurrentUser().updateEmail(edit_details.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    updateDetails(gettingOptions(), edit_details.getText().toString());
                                } else {
                                    Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else if (gettingOptions() == 2){
                        mAuth.getCurrentUser().updatePassword(edit_details.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    updateDetails(gettingOptions(), edit_details.getText().toString());
                                } else {
                                    Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    //preliminaries in this fragment
    private static final String TAG = "ChangeDetailsFragment";
    private Context mContext;

    //not view object initiation
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private List<String> list_options;
    private String text_before;

    //view objects initiation
    private ImageView close;
    private EditText edit_details;
    private Button update_button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_details, container, false);
        GlobalMemories.getmInstance().profile_counter = 2;
        mContext = getActivity();

        startingSetup(view);

        return view;
    }

    private void startingSetup(View view){
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        list_options = new ArrayList<>();
        text_before = gettingDetails();
        close = view.findViewById(R.id.close);
        edit_details = view.findViewById(R.id.edit_details);
        update_button = view.findViewById(R.id.update_button);

        list_options.add("username");
        list_options.add("email");
        list_options.add("password");

        edit_details.setText(text_before);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)mContext).onBackPressed();
            }
        });
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOptions(gettingOptions(),edit_details.getText().toString());
            }
        });
    }

    private String gettingDetails(){
        Bundle bundle = getArguments();

        if(bundle != null){
            return  bundle.getString("user_details");
        }else{
            return  null;
        }
    }

    private int gettingOptions(){
        Bundle bundle = getArguments();

        if(bundle != null){
            return  bundle.getInt("options");
        }else{
            return 3;
        }
    }

    private void updateDetails(int options, String detail){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("users")
                .child(user.getUid())
                .child(list_options.get(options))
                .setValue(detail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    ((Activity) mContext).onBackPressed();
                }else{
                    Toast.makeText(mContext, "Failed in updating the following detail", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkOptions(final int options, final String detail){
        if(!edit_details.getText().toString().equals(text_before)) {
            if (options != 0) {
                ReauthenticateFragment dialog = new ReauthenticateFragment();
                dialog.show(getFragmentManager(), "ReauthenticateFragment");
                dialog.setTargetFragment(ChangeDetailsFragment.this, 1);
                hideKeyboard(((Activity)mContext));
            } else {
                updateDetails(options, detail);
                hideKeyboard(((Activity)mContext));
            }
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
