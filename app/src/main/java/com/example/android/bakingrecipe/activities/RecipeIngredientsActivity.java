package com.example.android.bakingrecipe.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.fragments.RecipeIngredientsFragment;
import com.example.android.bakingrecipe.utils.ArgKeys;

public class RecipeIngredientsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_ingredients);

        // Add back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Extract bundle sent from prev screen
        // This activity can be started from widget and RecipeStepDetailsActivity

        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey(ArgKeys.RECIPE_NAME_ARG_ID) &&
        bundle.containsKey(ArgKeys.RECIPE_INGREDIENTS_ARG_ID)) {

            // Set title with the current Recipe name
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(bundle.getString(ArgKeys.RECIPE_NAME_ARG_ID));
            }

            RecipeIngredientsFragment ingredientsFragment = new RecipeIngredientsFragment();
            // send all the data to fragment
            ingredientsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ingredientsFragment)
                    .commit();
        } else {
            finish();
        }
    }
}
