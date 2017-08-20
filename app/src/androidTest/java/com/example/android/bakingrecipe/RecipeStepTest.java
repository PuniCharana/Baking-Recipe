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
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class RecipeStepTest {

    @Rule
    public ActivityTestRule<RecipeItemsActivity> mActivityTestRule = new ActivityTestRule<>(RecipeItemsActivity.class);

    @Test
    public void recipeStepTest() {

        onView(withId(R.id.rv_recipe_card))
                .check(matches(isDisplayed()))
                .perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.rv_recipe_steps))
                .perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.prev_btn))
                .check(matches(not(isDisplayed())));

        onView(withId(R.id.next_btn))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.prev_btn))
                .check(matches(isDisplayed()));

    }
}
