package com.test.recipes.activity;

import android.app.Application;

import com.test.recipes.model.Database;

import io.realm.Realm;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        initRealm();
    }

    private void initRealm() {
        Realm.init(this);
        Database.initDb();
    }
}

