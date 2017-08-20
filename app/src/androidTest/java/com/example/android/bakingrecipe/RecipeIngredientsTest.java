package com.example.android.bakingrecipe;


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
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RecipeIngredientsTest {

    @Rule
    public ActivityTestRule<RecipeItemsActivity> mActivityTestRule = new ActivityTestRule<>(RecipeItemsActivity.class);

    @Test
    public void recipeIngredientsTest() {
        onView(withId(R.id.rv_recipe_card))
                .check(matches(isDisplayed()))
                .perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.viewIngredients))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.add_to_widget_btn))
                .check(matches(isDisplayed()));

    }
}
