package com.example.android.bakingrecipe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.R2;
import com.example.android.bakingrecipe.adapters.RecipeAdapter;
import com.example.android.bakingrecipe.interfaces.RecipeApiService;
import com.example.android.bakingrecipe.models.Recipe;
import com.example.android.bakingrecipe.network.ApiClient;
import com.example.android.bakingrecipe.utils.InternetConnectivity;
import com.example.android.bakingrecipe.utils.RecipeContract;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeItemsActivity extends AppCompatActivity {

    @BindView(R2.id.rv_recipe_card) RecyclerView mRecyclerView;
    @BindView(R2.id.error_message) TextView mErrorMessage;
    @BindView(R2.id.loading_indicator) ProgressBar mProgressBar;

    @BindString(R.string.no_internet) String noInternet;
    @BindString(R.string.recipe_not_found) String recipeNotFound;

    private RecipeAdapter mRecipeAdapter;
    private ArrayList<Recipe> mRecipeList = new ArrayList<>();
    private Call<ArrayList<Recipe>> mApiCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_items);

        ButterKnife.bind(this);

        GridLayoutManager gridLayoutManager;
        // Set grid column base on screen size
        if (getResources().getBoolean(R.bool.is_tablet)) {
            gridLayoutManager = new GridLayoutManager(this, 4);
        } else {
            gridLayoutManager = new GridLayoutManager(this, 1);
        }

        // set recycler view layout manager
        mRecyclerView.setLayoutManager(gridLayoutManager);

        // Check if savedInstanceState is null
        // For screen rotation
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(RecipeContract.RECIPE_LIST_ARG_ID)) {
                mRecipeList.clear();
                mRecipeList = savedInstanceState.getParcelableArrayList(RecipeContract.RECIPE_LIST_ARG_ID);
            } else {
                // No prev data found
                loadData();
            }
        } else {
            // No prev data found
            loadData();
        }
        // Instantiate new RecipeAdapter adapter
        mRecipeAdapter = new RecipeAdapter(this,  mRecipeList);

        // Set recycler view adapter
        mRecyclerView.setAdapter(mRecipeAdapter);
    }

    // Load new data
    private void loadData(){

        // Check for internet connection
        if(InternetConnectivity.isConnected(this)) {
            mProgressBar.setVisibility(View.VISIBLE);
            RecipeApiService service =  ApiClient.getClient().create(RecipeApiService.class);
            mApiCall = service.getRecipe();

            mApiCall.enqueue(new Callback<ArrayList<Recipe>>() {
                @Override
                public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                    // Hide progress bar
                    mProgressBar.setVisibility(View.GONE);

                    if (response.isSuccessful()) {
                        ArrayList<Recipe> recipe = response.body();
                        mRecipeList.addAll(recipe);
                        mRecipeAdapter.notifyDataSetChanged();
                    } else {
                        showErrorMessage(recipeNotFound);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                    showErrorMessage(t.getMessage());
                }
            });
        } else {
            showErrorMessage(noInternet);
        }
    }

    // Error message
    private void showErrorMessage(String errorMessage) {
        mErrorMessage.setText(errorMessage);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    // Handle recycler view item click
    // Called from adapter
    public void recipeItemClicked(int position) {
        Recipe recipe = mRecipeList.get(position);
        Intent intent = new Intent(this, RecipeStepActivity.class);
        intent.putExtra(RecipeContract.RECIPE_ARG_ID, recipe);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save the data
        outState.putParcelableArrayList(RecipeContract.RECIPE_LIST_ARG_ID, mRecipeList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mApiCall !=null && !mApiCall.isCanceled()) {
            // cancel if network request is still in progress
            mApiCall.cancel();
        }
    }
}
