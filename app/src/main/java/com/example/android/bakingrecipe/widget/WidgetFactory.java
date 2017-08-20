package com.example.android.bakingrecipe.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.models.RecipeIngredients;
import com.example.android.bakingrecipe.utils.RecipeContract;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;


class WidgetFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context mContext;
    private ArrayList<RecipeIngredients> recipeIngredients = new ArrayList<>();
    private String recipeName;

    public WidgetFactory(Context mContext, Intent mIntent) {
        this.mContext = mContext;
        Intent mIntent1 = mIntent;
    }

    private void initData() {
        Log.d("UPDATE", "initData");
        if (recipeIngredients != null) {
            recipeIngredients.clear();
        }

        // retrieve sharedPref
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();

        if (pref.contains(RecipeContract.RECIPE_INGREDIENTS_ARG_ID)) {
            String jsonIngredients = pref.getString(RecipeContract.RECIPE_INGREDIENTS_ARG_ID, null);
            TypeToken<ArrayList<RecipeIngredients>> token = new TypeToken<ArrayList<RecipeIngredients>>(){};

            recipeIngredients = gson.fromJson(jsonIngredients, token.getType());
            recipeName = pref.getString(RecipeContract.RECIPE_NAME_ARG_ID, "Recipe name");
        }
    }
    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {
        initData();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return recipeIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews remoteView = new RemoteViews(mContext.getPackageName(), android.R.layout.simple_list_item_1);

        RecipeIngredients recipeIngredient = recipeIngredients.get(i);
        String ingredientMeasureQuantity = (i+1)+
                ". "+recipeIngredient.getIngredient()+
                " "+recipeIngredient.getQuantity()+
                " "+recipeIngredient.getMeasure();
        remoteView.setTextViewText(android.R.id.text1, ingredientMeasureQuantity);
        remoteView.setTextColor(android.R.id.text1, ContextCompat.getColor(mContext, R.color.colorBlack));

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(RecipeContract.RECIPE_NAME_ARG_ID, recipeName);
        fillInIntent.putParcelableArrayListExtra(RecipeContract.RECIPE_INGREDIENTS_ARG_ID, recipeIngredients);
        remoteView.setOnClickFillInIntent(android.R.id.text1, fillInIntent);
        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
