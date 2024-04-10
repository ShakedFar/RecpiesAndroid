package com.test.recipes.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.test.recipes.adapter.FavAdapter;
import com.test.recipes.databinding.ActivityFavBinding;
import com.test.recipes.model.Database;
import com.test.recipes.model.Fav;
import com.test.recipes.model.Recipe;
import com.test.recipes.model.Utils;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

public class FavActivity extends AppCompatActivity implements View.OnClickListener{
    ActivityFavBinding bind;
    FavAdapter adapter;
    List<Recipe> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityFavBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        initView();
    }
    private void initView() {
        dataList = new ArrayList<>();
        bind.dataList.setLayoutManager(new LinearLayoutManager(this));
        RealmResults<Fav> result = Database.db.where(Fav.class)
                .equalTo("user_id", Utils.user_id)
                .findAll();
        for(Fav item : result) {
            int recipe_id = item.getRecipeId();
            Recipe recipe = Database.db.where(Recipe.class)
                    .equalTo("id", recipe_id)
                    .findFirst();
            if(recipe != null) {
                dataList.add(recipe);
            }
        }
        adapter = new FavAdapter(this, dataList);
        adapter.setListener(new FavAdapter.Listener() {
            @Override
            public void onClick(Recipe model, int pos) {
                RecipeViewDialog dlg = new RecipeViewDialog();
                Bundle bundle = new Bundle();
                bundle.putInt("id", model.getId());
                dlg.setArguments(bundle);
                dlg.show(getSupportFragmentManager(), RecipeViewDialog.class.getName());
            }
        });
        bind.dataList.setAdapter(adapter);
    }
    @Override
    public void onClick(View view) {

    }
}