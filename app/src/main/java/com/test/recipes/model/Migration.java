package com.test.recipes.model;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class Migration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        Long version = oldVersion;
        // DynamicRealm exposes an editable schema
        RealmSchema schema = realm.getSchema();
        // Changes from version 0 to 1: Adding lastName.
        // All properties will be initialized with the default value "".
    }
}