package com.automation.tago.Administration;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.automation.tago.Main.MainActivity;
import com.automation.tago.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private Context mContext;

    private FirebaseAuth mAuth;

    private EditText user_email;
    private EditText user_password;
    private TextView reset_link;
    private Button login_button;
    private TextView register_link;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_login);
        mContext = LoginActivity.this;
        mAuth = FirebaseAuth.getInstance();

        startingSetup();
    }

    private void startingSetup(){
        user_email = findViewById(R.id.user_email);
        user_password = findViewById(R.id.user_password);
        reset_link = findViewById(R.id.reset_link);
        login_button = findViewById(R.id.login_button);
        register_link = findViewById(R.id.register_link);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isTextEmpty(user_email.getText().toString()) && isTextEmpty(user_password.getText().toString())){
                    signInUser(user_email.getText().toString(),user_password.getText().toString());
                }else{
                    Toast.makeText(mContext, "You have empty entries", Toast.LENGTH_SHORT).show();
                }
            }
        });

        register_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,RegisterActivity.class);
                mContext.startActivity(intent);
                finish();
            }
        });
    }

    private boolean isTextEmpty(String text){
        if(!text.equals("")){
            return true;
        }else{
            return false;
        }
    }

    private void signInUser(String email, String password){
        if(isNetworkAvailable()) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(mContext, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(mContext, "Please check your credentials", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else{
            Toast.makeText(mContext, "Please check your internet connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
