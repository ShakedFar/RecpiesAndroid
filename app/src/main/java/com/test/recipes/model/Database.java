package com.test.recipes.model;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Database {
    public static Realm db;

    public static void initDb() {
        String realmName = "recipe";
        RealmConfiguration config = new RealmConfiguration.Builder().name(realmName)
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .build();
        db = Realm.getInstance(config);
    }
}
