package com.example.android.bakingrecipe.fragments;


import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;

import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.R2;
import com.example.android.bakingrecipe.adapters.RecipeIngredientsAdapter;
import com.example.android.bakingrecipe.models.RecipeIngredients;
import com.example.android.bakingrecipe.utils.ArgKeys;
import com.example.android.bakingrecipe.widget.WidgetProvider;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeIngredientsFragment extends Fragment {

    private ArrayList<RecipeIngredients> mIngredientsList = new ArrayList<>();
    private String mRecipeName;

    @BindView(R2.id.add_to_widget_btn)
    Button mButtonAddToWidget;

    @BindView(R2.id.rv_recipe_ingredients)
    RecyclerView mRecyclerView;

    @BindView(R2.id.parent_scroll_view_ingredient)
    ScrollView mScrollView;

    private LinearLayoutManager linearLayoutManager;
    private int mPosition = 0;

    public RecipeIngredientsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_recipe_ingredients, container, false);

        ButterKnife.bind(this, rootView);

        linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        if (savedInstanceState != null &&
                savedInstanceState.containsKey(ArgKeys.RECIPE_NAME_ARG_ID) &&
                savedInstanceState.containsKey(ArgKeys.RECIPE_INGREDIENTS_LISTS_ARG_ID)) {

            mPosition = savedInstanceState.getInt(ArgKeys.SCROLLED_POSITION_ARG_ID);
            mRecipeName = savedInstanceState.getString(ArgKeys.RECIPE_NAME_ARG_ID);
            mIngredientsList = savedInstanceState.getParcelableArrayList(ArgKeys.RECIPE_INGREDIENTS_LISTS_ARG_ID);
        } else {
            if (getArguments().containsKey(ArgKeys.RECIPE_INGREDIENTS_ARG_ID) &&
                    getArguments().containsKey(ArgKeys.RECIPE_NAME_ARG_ID)) {

                mRecipeName = getArguments().getString(ArgKeys.RECIPE_NAME_ARG_ID);
                mIngredientsList = getArguments().getParcelableArrayList(ArgKeys.RECIPE_INGREDIENTS_ARG_ID);
            }
        }

        RecipeIngredientsAdapter mIngredientsAdapter = new RecipeIngredientsAdapter(getContext(), mIngredientsList);
        mRecyclerView.setAdapter(mIngredientsAdapter);

//        mScrollView.scrollTo(0, mPosition);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // I don't know why but this is working
        mScrollView.post(new Runnable() {
            public void run() {
                mScrollView.scrollTo(0, mPosition);
            }
        });
    }

    @OnClick(R2.id.add_to_widget_btn)
    public void addToWidget(View view) {
        String recipeName = getArguments().getString(ArgKeys.RECIPE_NAME_ARG_ID);
        Gson gson = new Gson();
        String jsonIngredient = gson.toJson(mIngredientsList);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor;
        editor = pref.edit();
        editor.putString(ArgKeys.RECIPE_NAME_ARG_ID, recipeName);
        editor.putString(ArgKeys.RECIPE_INGREDIENTS_ARG_ID, jsonIngredient);
        editor.apply();

        updateAllWidgets();

        Snackbar.make(view, "Added to home widget", Snackbar.LENGTH_LONG).show();

    }

    private void updateAllWidgets(){
        Intent widgetUpdater = new Intent(getContext(), WidgetProvider.class);
        widgetUpdater.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int ids[] = AppWidgetManager.getInstance(getContext()).getAppWidgetIds(new ComponentName(getContext(), WidgetProvider.class));
        widgetUpdater.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        getContext().sendBroadcast(widgetUpdater);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(ArgKeys.SCROLLED_POSITION_ARG_ID, mScrollView.getScrollY());
        outState.putParcelableArrayList(ArgKeys.RECIPE_INGREDIENTS_LISTS_ARG_ID, mIngredientsList);
        outState.putString(ArgKeys.RECIPE_NAME_ARG_ID, mRecipeName);
    }
}
