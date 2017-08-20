package com.example.android.bakingrecipe.fragments;


import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.R2;
import com.example.android.bakingrecipe.adapters.RecipeIngredientsAdapter;
import com.example.android.bakingrecipe.models.RecipeIngredients;
import com.example.android.bakingrecipe.utils.RecipeContract;
import com.example.android.bakingrecipe.widget.WidgetProvider;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeIngredientsFragment extends Fragment {


    private ArrayList<RecipeIngredients> mIngredientsList = new ArrayList<>();

    @BindView(R2.id.add_to_widget_btn)
    Button buttonAddToWodget;

    @BindView(R2.id.rv_recipe_ingredients)
    RecyclerView mRecyclerView;

    public RecipeIngredientsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipe_ingredients, container, false);

        ButterKnife.bind(this, rootView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        if (getArguments().containsKey(RecipeContract.RECIPE_INGREDIENTS_ARG_ID) &&
                getArguments().containsKey(RecipeContract.RECIPE_NAME_ARG_ID)) {

            mIngredientsList = getArguments().getParcelableArrayList(RecipeContract.RECIPE_INGREDIENTS_ARG_ID);
            RecipeIngredientsAdapter mIngredientsAdapter = new RecipeIngredientsAdapter(getContext(), mIngredientsList);
            mRecyclerView.setAdapter(mIngredientsAdapter);
        }

        buttonAddToWodget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String recipeName = getArguments().getString(RecipeContract.RECIPE_NAME_ARG_ID);

                Gson gson = new Gson();
                String jsonIngredient = gson.toJson(mIngredientsList);

                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor;
                editor = pref.edit();
                editor.putString(RecipeContract.RECIPE_NAME_ARG_ID, recipeName);
                editor.putString(RecipeContract.RECIPE_INGREDIENTS_ARG_ID, jsonIngredient);
                editor.apply();

                updateAllWidgets();
                Toast.makeText(getContext(), "Added to widget", Toast.LENGTH_SHORT).show();

            }
        });

        return rootView;
    }

    private void updateAllWidgets(){
        Intent widgetUpdater = new Intent(getContext(), WidgetProvider.class);
        widgetUpdater.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int ids[] = AppWidgetManager.getInstance(getContext()).getAppWidgetIds(new ComponentName(getContext(), WidgetProvider.class));
        widgetUpdater.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        getContext().sendBroadcast(widgetUpdater);
    }

}
