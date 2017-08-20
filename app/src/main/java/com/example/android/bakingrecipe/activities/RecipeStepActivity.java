package com.example.android.bakingrecipe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.R2;
import com.example.android.bakingrecipe.adapters.RecipeStepsAdapter;
import com.example.android.bakingrecipe.fragments.RecipeIngredientsFragment;
import com.example.android.bakingrecipe.fragments.RecipeStepDetailsFragment;
import com.example.android.bakingrecipe.models.Recipe;
import com.example.android.bakingrecipe.models.RecipeIngredients;
import com.example.android.bakingrecipe.models.RecipeStep;
import com.example.android.bakingrecipe.utils.RecipeContract;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepActivity extends AppCompatActivity {

    private static final String LOG_TAG = RecipeStepActivity.class.getSimpleName();
    private boolean isTablet;
    private ArrayList<RecipeIngredients> mRecipeIngredientsList;
    private ArrayList<RecipeStep> mRecipeStepList = new ArrayList<>();
    private String mRecipeName;

    @BindView(R2.id.num_ingredients) TextView mNumIngredients;
    @BindView(R2.id.rv_recipe_steps) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        ButterKnife.bind(this);

        isTablet = getResources().getBoolean(R.bool.is_tablet);

        // Add back button to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        // Add horizontal line separator
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, linearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            Recipe mRecipe = bundle.getParcelable(RecipeContract.RECIPE_ARG_ID);

            assert mRecipe != null;
            mRecipeName = mRecipe.getName();
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(mRecipeName);
            }

            mRecipeIngredientsList = mRecipe.getIngredients();
            mRecipeStepList = mRecipe.getSteps();

            mNumIngredients.setText(mRecipeIngredientsList.size()+" Ingredients");

            RecipeStepsAdapter mRecipeStepsAdapter = new RecipeStepsAdapter(this, mRecipeStepList);
            mRecyclerView.setAdapter(mRecipeStepsAdapter);

            // on click of view ingredients
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.viewIngredients);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateViewIngredients();
                }
            });

            if (isTablet) {
                // Show ingredients list by default
                updateViewIngredients();
            }
        } else {
            finish();
        }
    }

    /*
    * Update ingredients display view based on screen size
    * On large screen display on same screen
    * On smaller screen display on next activity
    * */
    private void updateViewIngredients() {

        if (isTablet) {
            Bundle arguments = new Bundle();
            arguments.putParcelableArrayList(RecipeContract.RECIPE_INGREDIENTS_ARG_ID, mRecipeIngredientsList);
            arguments.putString(RecipeContract.RECIPE_NAME_ARG_ID, mRecipeName);

            RecipeIngredientsFragment ingredientsFragment = new RecipeIngredientsFragment();
            ingredientsFragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ingredientsFragment)
                    .commit();

        } else {
            Intent intent = new Intent(this, RecipeIngredientsActivity.class);
            intent.putParcelableArrayListExtra(RecipeContract.RECIPE_INGREDIENTS_ARG_ID, mRecipeIngredientsList);
            intent.putExtra(RecipeContract.RECIPE_NAME_ARG_ID, mRecipeName);
            startActivity(intent);
        }
    }

    /*
    * Update the ui base on screen size
    * On large screen display on same screen
    * On smaller screen display on next activity
    *
    * @param (int) position : click item position
    * */
    private void updateUI(int position) {
        if (isTablet) {
            Bundle arguments = new Bundle();
            arguments.putParcelableArrayList(RecipeContract.RECIPE_STEPS_ARG_ID, mRecipeStepList);
            arguments.putString(RecipeContract.RECIPE_NAME_ARG_ID, mRecipeName);
            arguments.putInt(RecipeContract.RECIPE_POSITION_ARG_ID, position);

            RecipeStepDetailsFragment recipeStepDetailsFragment = new RecipeStepDetailsFragment();
            recipeStepDetailsFragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, recipeStepDetailsFragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, RecipeStepDetailsActivity.class);
            intent.putParcelableArrayListExtra(RecipeContract.RECIPE_STEPS_ARG_ID, mRecipeStepList);
            intent.putExtra(RecipeContract.RECIPE_NAME_ARG_ID, mRecipeName);
            intent.putExtra(RecipeContract.RECIPE_POSITION_ARG_ID, position);
            startActivity(intent);
        }
    }

    // called when a recipe item is clicked
    // called from adapter
    public void stepItemClicked(int position) {
        updateUI(position);
    }
}
