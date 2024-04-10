package com.test.recipes.model;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class Fav extends RealmObject {
    private int user_id;
    private int recipe_id;

    public void setUserId(int user_id){
        this.user_id = user_id;
    }
    public void setRecipeId(int recipe_id) {
        this.recipe_id = recipe_id;
    }
    public int getUserId() {
        return user_id;
    }
    public int getRecipeId() {
        return recipe_id;
    }
}