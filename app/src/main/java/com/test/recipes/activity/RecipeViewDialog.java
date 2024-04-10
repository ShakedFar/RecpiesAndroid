package com.test.recipes.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.test.recipes.adapter.SpinAdapter;
import com.test.recipes.databinding.DialogRecipleViewBinding;
import com.test.recipes.model.Database;
import com.test.recipes.model.Recipe;

public class RecipeViewDialog extends DialogFragment implements View.OnClickListener{
    DialogRecipleViewBinding bind;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        bind();
        builder.setView(bind.getRoot());
        builder.setCancelable(false);
        return builder.create();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        initView();
        return super.getView();
    }
    private void bind(){
        bind = DialogRecipleViewBinding.inflate(requireActivity().getLayoutInflater());
    }

    private void initView(){
        bind.closeBtn.setOnClickListener(this);
        bind.cancelBtn.setOnClickListener(this);

        int id = getArguments().getInt("id");
        Recipe recipe = Database.db.where(Recipe.class).equalTo("id", id).findFirst();
        if(recipe != null) {
            bind.title.setText(recipe.getTitle());
            bind.instruction.setText(recipe.getInstruction());
            bind.category.setText(recipe.getCategory());
            bind.ingredients.setText(recipe.getIngredients());
            bind.recipeImage.setImageURI(Uri.parse(recipe.getImage()));
        }
    }

    @Override
    public void onClick(View view) {
       if(view == bind.closeBtn){
            dismiss();
        } else if(view == bind.cancelBtn){
            dismiss();
        }
    }
}
