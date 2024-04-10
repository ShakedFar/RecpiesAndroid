package com.test.recipes.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.test.recipes.R;
import com.test.recipes.model.Recipe;

import java.util.List;

public class FavAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Recipe> localDataSet;
    private Listener mListener;

    public interface Listener{
        void onClick(Recipe model, int pos);
    }
    public void setListener(Listener listener){
        mListener = listener;
    }

    public static class Holder extends RecyclerView.ViewHolder {
        View view;
        ImageView recipeImage;
        TextView titleView;
        TextView categoryView;

        public Holder(View view) {
            super(view);
            this.view = view;
            recipeImage = view.findViewById(R.id.recipe_image);
            titleView = view.findViewById(R.id.title);
            categoryView = view.findViewById(R.id.category);
        }
    }

    public FavAdapter(Context context, List<Recipe> dataSet) {
        this.context = context;
        localDataSet = dataSet;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(List<Recipe> filterllist) {
        localDataSet = filterllist;
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_fav_item, viewGroup, false);
        return new RecipeAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        Recipe model = localDataSet.get(position);
        RecipeAdapter.Holder holder = (RecipeAdapter.Holder) viewHolder;

        holder.recipeImage.setImageURI(Uri.parse(model.getImage()));
        holder.titleView.setText(model.getTitle());
        holder.categoryView.setText(model.getCategory());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null) {
                    mListener.onClick(model, position);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}