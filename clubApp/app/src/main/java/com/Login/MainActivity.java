package com.Login;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.content.Context;
import android.content.Intent;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import com.example.clubapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import androidx.annotation.VisibleForTesting;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;

    @VisibleForTesting
    public ProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        findViewById(R.id.loginButton).setOnClickListener(this);
        findViewById(R.id.signUp).setOnClickListener(this);

        isUserLoggedIn();
    }

    public void isUserLoggedIn(){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser!=null){
//            Intent intent = new Intent(this, ViewPosts.class);
//            startActivity(intent);
        }

    }

    public void setProgressBar(int resId) {
        mProgressBar = findViewById(resId);
    }

    public void showProgressBar() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgressBar() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressBar();
    }

    public void onClick(View v){
        int i = v.getId();

        if (i == R.id.loginButton){
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }

        if(i == R.id.signUp){
            Intent intent1 = new Intent(MainActivity.this, signUpActivity.class);
            startActivity(intent1);
        }
    }

}