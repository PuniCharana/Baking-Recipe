package com.example.android.bakingrecipe.interfaces;

import com.example.android.bakingrecipe.models.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;



public interface RecipeApiService {

    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipe();
}
