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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.test.recipes.adapter.SpinAdapter;
import com.test.recipes.databinding.DialogAddRecipeBinding;
import com.test.recipes.model.Database;
import com.test.recipes.model.Recipe;
import com.test.recipes.model.Utils;

public class RecipeAddDialog extends DialogFragment implements View.OnClickListener{
    DialogAddRecipeBinding bind;
    SpinAdapter category_adapter;
    SpinAdapter ingredient_adapter;
    String ingredients = "";
    Listener mListener;
    Uri imageUri;

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
        bind = DialogAddRecipeBinding.inflate(requireActivity().getLayoutInflater());
    }
    public interface Listener{
        void onAdd(Recipe model);
    }
    public void setListener(Listener listener){
        this.mListener = listener;
    }
    private void initView(){
        bind.addBtn.setOnClickListener(this);
        bind.addIngredientBtn.setOnClickListener(this);
        bind.closeBtn.setOnClickListener(this);
        bind.cancelBtn.setOnClickListener(this);
        bind.recipeImage.setOnClickListener(this);

        category_adapter = new SpinAdapter(requireContext(), Utils.CATEGORIES);
        ingredient_adapter = new SpinAdapter(requireContext(), Utils.INGREDIENTS);

        bind.categoryList.setAdapter(category_adapter);
        bind.ingredientList.setAdapter(ingredient_adapter);
    }

    @Override
    public void onClick(View view) {
        if(view == bind.addBtn){
            addRecipe();
        } else if(view == bind.addIngredientBtn){
            addIngredient();
        } else if(view == bind.closeBtn){
            dismiss();
        } else if(view == bind.cancelBtn){
            dismiss();
        } else if(view == bind.recipeImage) {
            selectImage();
        }
    }

    private void addIngredient() {
        ingredients += (String)bind.ingredientList.getSelectedItem() + ", ";
        bind.ingredient.setText(ingredients);
    }

    private void addRecipe() {
        String title = bind.title.getText().toString().trim();
        String instruction = bind.instruction.getText().toString().trim();
        String category = (String)bind.categoryList.getSelectedItem();
        String ingredients = bind.ingredient.getText().toString().trim();

        if(title.isEmpty()) {
            bind.title.setError("Please enter title");
            return;
        }
        Recipe model = new Recipe();
        model.setTitle(title);
        model.setInstruction(instruction);
        model.setCategory(category);
        model.setIngredients(ingredients);
        if(imageUri == null) {
            model.setImage("");
        } else {
            model.setImage(imageUri.toString());
        }

        Database.db.executeTransaction (transactionRealm -> {
            Number currentIdNum = Database.db.where(Recipe.class).max("id");
            int nextId;
            if(currentIdNum == null) {
                nextId = 1;
            } else {
                nextId = currentIdNum.intValue() + 1;
            }
            model.setId(nextId);
            transactionRealm.insertOrUpdate(model);
            if(mListener != null) {
                mListener.onAdd(model);
            }
        });
        dismiss();
    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        videoSelectionResultLauncher.launch(intent);
    }
    ActivityResultLauncher<Intent> videoSelectionResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if(data == null) return;
                        if(data.getData() != null){
                            imageUri = data.getData();
                            try {
                                getActivity().getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }
                            catch (SecurityException e){
                                e.printStackTrace();
                            }
                            if(imageUri != null){
                                bind.recipeImage.setImageURI(imageUri);
                            }
                        }
                    }
                }
            });
}
