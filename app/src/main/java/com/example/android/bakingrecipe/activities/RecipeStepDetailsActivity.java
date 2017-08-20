package com.example.android.bakingrecipe.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.fragments.RecipeStepDetailsFragment;
import com.example.android.bakingrecipe.utils.ArgKeys;

public class RecipeStepDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_details);

        Bundle bundle = getIntent().getExtras();

        if (bundle.containsKey(ArgKeys.RECIPE_STEPS_ARG_ID) &&
                bundle.containsKey(ArgKeys.RECIPE_NAME_ARG_ID) &&
                bundle.containsKey(ArgKeys.RECIPE_POSITION_ARG_ID)) {

            if(getSupportActionBar() != null) {
                // set title
                getSupportActionBar().setTitle(bundle.getString(ArgKeys.RECIPE_NAME_ARG_ID));
            }

            RecipeStepDetailsFragment recipeStepDetailsFragment = new RecipeStepDetailsFragment();
            recipeStepDetailsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, recipeStepDetailsFragment)
                    .commit();

        } else {
            finish();
        }
    }
}
