package net.bashayer.baking;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Display;
import android.view.View;

import net.bashayer.baking.callback.RecipesCallback;
import net.bashayer.baking.callback.StepsCallback;
import net.bashayer.baking.fragment.RecipeDetailsFragment;
import net.bashayer.baking.fragment.RecipeStepFragment;
import net.bashayer.baking.fragment.RecipesFragment;
import net.bashayer.baking.model.Recipe;
import net.bashayer.baking.model.Recipes;
import net.bashayer.baking.model.Step;

import java.io.InputStream;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static net.bashayer.baking.Constants.BUNDLE;
import static net.bashayer.baking.Constants.IS_TWO_PANE_LAYOUT;
import static net.bashayer.baking.Constants.ORIENTATION;
import static net.bashayer.baking.Constants.RECIPE;
import static net.bashayer.baking.Constants.RECIPES;
import static net.bashayer.baking.Constants.STEP;

public class MainActivity extends AppCompatActivity implements RecipesCallback, StepsCallback {

    private FragmentManager fragmentManager;
    private Recipes recipes;
    private Recipe recipe;
    private Step step;

    private RecipesFragment recipesFragment = new RecipesFragment();
    private RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();
    private RecipeStepFragment recipeStepFragment = new RecipeStepFragment();

    private boolean isTwoPaneLayout = false;

    private Bundle bundle = new Bundle();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.linear_layout) != null) {
            isTwoPaneLayout = true;
        }


        if (savedInstanceState != null) {
            Bundle bundle = savedInstanceState.getBundle(BUNDLE);
            if (bundle != null) {
                setSavedState(bundle);
            }
        }


        showRecipes();
    }

    private void setSavedState(Bundle bundle) {
        this.bundle = bundle;

        this.step = (Step) bundle.getSerializable(STEP);
        this.recipe = (Recipe) bundle.getSerializable(RECIPE);
        this.recipes = (Recipes) bundle.getSerializable(RECIPES);

        fragmentManager = getSupportFragmentManager();

        int id;
        if (isTwoPaneLayout && step != null) {
            id = R.id.recipes_steps;
        } else {
            id = R.id.frame_container;
        }
        showFragment(id, recipeStepFragment, recipe.getName());

    }

    private void showRecipes() {
        InputStream resourceReader = getResources().openRawResource(R.raw.response);
        recipes = JSONUtils.getResponse(resourceReader);

        recipesFragment.setCallback(this);
        fragmentManager = getSupportFragmentManager();

        bundle.putSerializable(RECIPES, recipes);
        bundle.putBoolean(IS_TWO_PANE_LAYOUT, isTwoPaneLayout);

        recipesFragment.setArguments(bundle);

        if (isTwoPaneLayout) {
            showFragment(R.id.recipes, recipesFragment, getString(R.string.recipes));
        } else {
            showFragment(R.id.frame_container, recipesFragment, getString(R.string.recipes));
        }
    }

    @Override
    public void clickOnRecipe(Recipe recipe) {
        this.recipe = recipe;
        bundle.putSerializable(RECIPE, recipe);
        bundle.putBoolean(IS_TWO_PANE_LAYOUT, isTwoPaneLayout);

        recipeDetailsFragment.setArguments(bundle);

        recipeDetailsFragment.setCallback(this);

        if (isTwoPaneLayout) {
            hideFragment(recipesFragment);
            showFragment(R.id.recipes_steps, recipeStepFragment, R.id.recipes_details, recipeDetailsFragment, recipe.getName());
            recipeStepFragment = (RecipeStepFragment) fragmentManager.findFragmentById(R.id.recipes_steps);
            clickOnStep(recipe.getSteps().get(0));
        } else {
            showFragment(R.id.frame_container, recipeDetailsFragment, recipe.getName());
        }
    }

    @Override
    public void clickOnStep(Step step) {
        this.step = step;
        if (isTwoPaneLayout) {
            recipeStepFragment.setStep(step);
        } else {
            bundle.putSerializable(STEP, step);
            recipeStepFragment.setArguments(bundle);

            hideFragment(recipeDetailsFragment);
            showFragment(R.id.frame_container, recipeStepFragment, recipe.getName());
        }
    }

    private void showFragment(int id, Fragment fragment, String title) {
        getSupportActionBar().setTitle(title);

        fragmentManager.beginTransaction()
                .replace(id, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit();
    }

    private void showFragment(int id, Fragment fragment, int id2, Fragment fragment2, String title) {
        getSupportActionBar().setTitle(title);

        fragmentManager.beginTransaction()
                .replace(id, fragment)
                .replace(id2, fragment2)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        int count = fragmentManager.getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            fragmentManager.popBackStack();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void hideFragment(Fragment fragment) {
        fragmentManager.beginTransaction().hide(fragment).commit();
    }

    private void showFragment(Fragment fragment) {
        fragmentManager.beginTransaction().show(fragment).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBundle(BUNDLE, bundle);
        super.onSaveInstanceState(outState);
    }
}
