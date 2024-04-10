package com.test.recipes.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.test.recipes.adapter.RecipeAdapter;
import com.test.recipes.databinding.ActivityMainBinding;
import com.test.recipes.model.Database;
import com.test.recipes.model.Fav;
import com.test.recipes.model.Recipe;
import com.test.recipes.model.SessionManager;
import com.test.recipes.model.Utils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ActivityMainBinding bind;
    RecipeAdapter adapter;
    List<Recipe> dataList;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        initView();
    }
    private void initView() {
        sessionManager = new SessionManager(this);
        checkFilePermission();
        dataList = new ArrayList<>();
        bind.dataList.setLayoutManager(new LinearLayoutManager(this));
        RealmResults<Recipe> result = Database.db.where(Recipe.class).findAll();
        dataList.addAll(result.subList(0, result.size()));
        adapter = new RecipeAdapter(this, dataList);
        adapter.setListener(new RecipeAdapter.Listener() {
            @Override
            public void onClick(Recipe model, int pos) {
                RecipeViewDialog dlg = new RecipeViewDialog();
                Bundle bundle = new Bundle();
                bundle.putInt("id", model.getId());
                dlg.setArguments(bundle);
                dlg.show(getSupportFragmentManager(), RecipeViewDialog.class.getName());
            }
            @Override
            public void onFav(Recipe model, int pos) {
                Fav fav = Database.db.where(Fav.class).equalTo("user_id", Utils.user_id)
                                .equalTo("recipe_id", model.getId()).findFirst();
                if(fav != null) {
                    Database.db.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            fav.deleteFromRealm();
                            adapter.notifyItemChanged(pos);
                        }
                    });
                } else {
                    Fav new_fav = new Fav();
                    new_fav.setUserId(Utils.user_id);
                    new_fav.setRecipeId(model.getId());
                    Database.db.executeTransaction (transactionRealm -> {
                        transactionRealm.insertOrUpdate(new_fav);
                        adapter.notifyItemChanged(pos);
                    });
                }
            }
        });
        bind.dataList.setAdapter(adapter);

        bind.searchRecipe.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                filter(s.toString());
            }
        });

        bind.addBtn.setOnClickListener(this);
        bind.favBtn.setOnClickListener(this);
        bind.logoutBtn.setOnClickListener(this);

    }
    private void filter(String text) {
        List<Recipe> filteredlist = new ArrayList<>();
        if(text == null || text.trim().isEmpty()) {
            filteredlist.addAll(dataList);
            adapter.filterList(filteredlist);
            return;
        }
        for (Recipe item : dataList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getTitle().toLowerCase().contains(text.toLowerCase()) || item.getCategory().toLowerCase().contains(text.toLowerCase())
            || item.getIngredients().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        adapter.filterList(filteredlist);
    }
    public void checkFilePermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1234);
        }
    }
    @Override
    public void onClick(View view) {
        if(view == bind.addBtn) {
            RecipeAddDialog dlg = new RecipeAddDialog();
            dlg.setListener(model -> {
                adapter.getDataList().add(model);
                adapter.notifyDataSetChanged();
            });
            dlg.show(getSupportFragmentManager(), RecipeAddDialog.class.getName());
        } else if(view == bind.favBtn) {
            Intent intent = new Intent(this, FavActivity.class);
            startActivity(intent);
        } else if(view == bind.logoutBtn) {
            sessionManager.setLogin(false);
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}