package com.example.android.bakingrecipe.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.R2;
import com.example.android.bakingrecipe.activities.RecipeStepActivity;
import com.example.android.bakingrecipe.models.RecipeStep;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.RecipeStepsViewHolder> {

    private final Context mContext;
    private final ArrayList<RecipeStep> mRecipeStepList;

    public RecipeStepsAdapter(Context mContext, ArrayList<RecipeStep> mRecipeList) {
        this.mContext = mContext;
        this.mRecipeStepList = mRecipeList;
    }

    @Override
    public RecipeStepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recipe_step, parent, false);
        return new RecipeStepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeStepsViewHolder holder, final int position) {

        RecipeStep recipeStep = mRecipeStepList.get(position);
        holder.stepNum.setText((recipeStep.getId()+1)+".");
        holder.recipeTitle.setText(recipeStep.getShortDescription());

        if (TextUtils.isEmpty(recipeStep.getVideoURL())) {
            holder.videoImageView.setVisibility(View.GONE);
            holder.descriptionImageView.setVisibility(View.VISIBLE);
        } else {
            holder.videoImageView.setVisibility(View.VISIBLE);
            holder.descriptionImageView.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RecipeStepActivity) mContext).stepItemClicked(position);
            }
        });
    }
    @Override
    public int getItemCount() {
        return mRecipeStepList.size();
    }


    public class RecipeStepsViewHolder extends RecyclerView.ViewHolder{

        @BindView(R2.id.tv_step_number) TextView stepNum;
        @BindView(R2.id.tv_recipe_steps_description) TextView recipeTitle;
        @BindView(R2.id.iv_recipe_video) ImageView videoImageView;
        @BindView(R2.id.iv_recipe_description) ImageView descriptionImageView;

        public RecipeStepsViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
