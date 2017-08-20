package com.example.android.bakingrecipe;


import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.activities.RecipeItemsActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RecipeItemsActivityTest {

    @Rule
    public ActivityTestRule<RecipeItemsActivity> mActivityTestRule = new ActivityTestRule<>(RecipeItemsActivity.class);

    @Test
    public void recipeItemsClickDisplayCorrectNumberOfIngredientsCase1() {

        onView(withId(R.id.rv_recipe_card))
                .check(matches(isDisplayed()))
                .perform(scrollToPosition(3))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));

        onView(withId(R.id.num_ingredients))
                .check(matches(isDisplayed()))
                .check(matches(withText("9 Ingredients")));

    }

    @Test
    public void recipeItemsClickDisplayCorrectNumberOfIngredientsCase2() {

        onView(withId(R.id.rv_recipe_card))
                .check(matches(isDisplayed()))
                .perform(scrollToPosition(1))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.num_ingredients))
                .check(matches(isDisplayed()))
                .check(matches(withText("10 Ingredients")));

    }
}
