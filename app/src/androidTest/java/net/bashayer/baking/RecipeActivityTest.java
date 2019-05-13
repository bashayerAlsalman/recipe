package net.bashayer.baking;


import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)

public class RecipeActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testListIsNotNull() {
        Espresso.onView(withId(R.id.recycler_view))
                .check(matches(isDisplayed()));
    }


    @Test
    public void testListContainsRecipe() {
        Espresso.onView(withId(R.id.recycler_view))
                .check(matches(hasDescendant(withText("Nutella Pie"))));
    }


    @Test
    public void clickOnFirstItem() {
        Espresso.onView(withId(R.id.recycler_view))
                .perform(actionOnItemAtPosition(0, click()));

    }

}
