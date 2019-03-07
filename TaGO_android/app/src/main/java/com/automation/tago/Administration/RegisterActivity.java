package com.automation.tago.Administration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.automation.tago.Models.User;
import com.automation.tago.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private Context mContext;

    private FirebaseAuth mAuth;

    private ImageView close;
    private EditText user_username;
    private EditText user_email;
    private EditText user_password;
    private EditText user_confirm_password;
    private Button register_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext = RegisterActivity.this;

        startingSetup();
    }

    private void startingSetup(){
        mAuth = FirebaseAuth.getInstance();
        close = findViewById(R.id.close);
        user_username = findViewById(R.id.user_username);
        user_email = findViewById(R.id.user_email);
        user_password = findViewById(R.id.user_password);
        user_confirm_password = findViewById(R.id.user_confirm_password);
        register_button = findViewById(R.id.register_button);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(intent);
                finish();
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = user_username.getText().toString(); String email = user_email.getText().toString();
                String password = user_password.getText().toString(); String confirm_password = user_confirm_password.getText().toString();
                if(isTextEmpty(username) && isTextEmpty(email) && isTextEmpty(password) && isTextEmpty(confirm_password)){
                    if(password.equals(confirm_password)){
                        registerUser(username, email, password);
                    }else{
                        Toast.makeText(mContext, "The confirm password does match with the password", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(mContext, "You have empty entries", Toast.LENGTH_SHORT).show();
                }
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

    private void registerUser(final String username, final String email, final String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            addNewUser(username, email, password);
                        }else{
                            Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void addNewUser(String username, String email, String password){
        String user_id = mAuth.getCurrentUser().getUid();
        User user = new User(user_id, username, email, password);

        FirebaseDatabase.getInstance().getReference()
        .child("users")
        .child(user_id)
        .setValue(user)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(intent);
                finish();
            }
        });
    }
}
