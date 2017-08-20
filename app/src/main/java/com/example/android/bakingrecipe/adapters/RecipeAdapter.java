package com.example.android.bakingrecipe.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.R2;
import com.example.android.bakingrecipe.activities.RecipeItemsActivity;
import com.example.android.bakingrecipe.models.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final Context mContext;
    private final ArrayList<Recipe> mRecipeList;

    public RecipeAdapter(Context mContext, ArrayList<Recipe> mRecipeList) {
        this.mContext = mContext;
        this.mRecipeList = mRecipeList;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, final int position) {

        holder.recipeTitle.setText(mRecipeList.get(position).getName());

        Glide.with(mContext).load(mRecipeList.get(position).getImage())
                .placeholder(R.drawable.cake_ingredients)
                .centerCrop()
                .dontAnimate()
                .into(holder.recipeImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RecipeItemsActivity) mContext).recipeItemClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.recipe_thumbnail) ImageView recipeImageView;
        @BindView(R2.id.recipe_title) TextView recipeTitle;

        public RecipeViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
