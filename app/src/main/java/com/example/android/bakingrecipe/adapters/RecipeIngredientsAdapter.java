package com.example.android.bakingrecipe.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.R2;
import com.example.android.bakingrecipe.models.RecipeIngredients;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeIngredientsAdapter extends RecyclerView.Adapter<RecipeIngredientsAdapter.IngredientsViewHolder> {

    private final ArrayList<RecipeIngredients> mIngredientsArrayList;
    private final Context mContext;

    public RecipeIngredientsAdapter(Context context, ArrayList<RecipeIngredients> ingredientsArrayList) {
        this.mContext = context;
        this.mIngredientsArrayList = ingredientsArrayList;
    }

    @Override
    public IngredientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recipe_ingredients, parent, false);
        return new IngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientsViewHolder holder, int position) {

        RecipeIngredients ingredients = mIngredientsArrayList.get(position);

        String ingredient = (position+1)+". "+ingredients.getIngredient()+" "+ingredients.getQuantity()+" "+ingredients.getMeasure();
        holder.ingredient.setText(ingredient);
    }

    @Override
    public int getItemCount() {
        return mIngredientsArrayList.size();
    }

    public class IngredientsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.ingredient) TextView ingredient;

        public IngredientsViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
    }
}
