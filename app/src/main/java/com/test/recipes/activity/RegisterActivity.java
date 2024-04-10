package com.test.recipes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.test.recipes.databinding.ActivityRegisterBinding;
import com.test.recipes.model.Database;
import com.test.recipes.model.Recipe;
import com.test.recipes.model.SessionManager;
import com.test.recipes.model.User;
import com.test.recipes.model.Utils;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    ActivityRegisterBinding bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        initView();
    }
    private void initView() {
        bind.back.setOnClickListener(this);
        bind.registerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == bind.registerBtn) {
            register();
        } else if(view == bind.back) {
            finish();
        }
    }
    private void register() {
        String username = bind.email.getText().toString().trim();
        String password = bind.password.getText().toString().trim();
        if(username.isEmpty() || password.isEmpty()) {
            return;
        }
        User user = Database.db.where(User.class)
                .equalTo("username", username)
                .findFirst();
        if(user != null) {
            Toast.makeText(this, "Same username is exist now !", Toast.LENGTH_LONG).show();
            return;
        }
        SessionManager sessionManager = new SessionManager(this);
        User model = new User();
        model.setUsername(username);
        model.setPassword(password);
        Database.db.executeTransaction (transactionRealm -> {
            Number currentIdNum = Database.db.where(User.class).max("id");
            int nextId;
            if(currentIdNum == null) {
                nextId = 1;
            } else {
                nextId = currentIdNum.intValue() + 1;
            }
            model.setId(nextId);
            transactionRealm.insertOrUpdate(model);
            Utils.user_id = nextId;
            sessionManager.setLogin(true);
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}
