package com.test.recipes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.test.recipes.databinding.ActivityLoginBinding;
import com.test.recipes.model.Database;
import com.test.recipes.model.Recipe;
import com.test.recipes.model.SessionManager;
import com.test.recipes.model.User;
import com.test.recipes.model.Utils;

import io.realm.RealmResults;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    ActivityLoginBinding bind;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        initView();
    }
    private void initView() {
        sessionManager = new SessionManager(this);
        bind.loginBtn.setOnClickListener(this);
        bind.registerBtn.setOnClickListener(this);
        if(sessionManager.IsLogin()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {
        if(view == bind.registerBtn) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        } else if(view == bind.loginBtn) {
            login();
        }
    }
    private void login() {
        String username = bind.email.getText().toString().trim();
        String password = bind.password.getText().toString().trim();
        if(username.isEmpty() || password.isEmpty()) {
            return;
        }
        User user = Database.db.where(User.class)
                .equalTo("username", username)
                .equalTo("password", password)
                .findFirst();

        if(user != null) {
            sessionManager.setLogin(true);
            Utils.user_id = user.getId();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Invalid user !", Toast.LENGTH_LONG).show();
        }
    }
}