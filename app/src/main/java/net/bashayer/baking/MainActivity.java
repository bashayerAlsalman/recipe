package net.bashayer.baking;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Display;
import android.view.View;

import net.bashayer.baking.callback.LoadRecipesCallback;
import net.bashayer.baking.callback.RecipesCallback;
import net.bashayer.baking.callback.StepsCallback;
import net.bashayer.baking.fragment.RecipeDetailsFragment;
import net.bashayer.baking.fragment.RecipeStepFragment;
import net.bashayer.baking.fragment.RecipesFragment;
import net.bashayer.baking.model.Recipe;
import net.bashayer.baking.model.Recipes;
import net.bashayer.baking.model.Step;
import net.bashayer.baking.network.NetworkUtils;

import java.io.InputStream;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static net.bashayer.baking.Constants.BUNDLE;
import static net.bashayer.baking.Constants.IS_TWO_PANE_LAYOUT;
import static net.bashayer.baking.Constants.RECIPE;
import static net.bashayer.baking.Constants.RECIPES;
import static net.bashayer.baking.Constants.STEP;

public class MainActivity extends AppCompatActivity implements RecipesCallback, StepsCallback, LoadRecipesCallback {

    private FragmentManager fragmentManager;
    private Recipes recipes = new Recipes();
    private Recipe recipe;
    private Step step;

    private RecipesFragment recipesFragment = new RecipesFragment();
    private RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();
    private RecipeStepFragment recipeStepFragment = new RecipeStepFragment();

    private boolean isTwoPaneLayout = false;

    private NetworkUtils networkUtils;
    private Bundle bundle = new Bundle();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkUtils = new NetworkUtils(this);
        if (findViewById(R.id.linear_layout) != null) {
            isTwoPaneLayout = true;
        }


        if (savedInstanceState != null) {
            Bundle bundle = savedInstanceState.getBundle(BUNDLE);
            if (bundle != null) {
                setSavedState(bundle);
            } else {
                networkUtils.loadRecipes();
            }
        } else {
            networkUtils.loadRecipes();
        }

    }

    private void setSavedState(Bundle bundle) {
        this.bundle = bundle;

        this.step = (Step) bundle.getSerializable(STEP);
        this.recipe = (Recipe) bundle.getSerializable(RECIPE);
        this.recipes = (Recipes) bundle.getSerializable(RECIPES);

        fragmentManager = getSupportFragmentManager();

        if (step != null) {
            show(3);
        } else if (recipe != null) {
            show(2);
        } else {
            show(1);
        }
    }

    private void showRecipes() {
        show(1);
    }

    private void show(int stepNumber) {
        switch (stepNumber) {
            case 1:
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
                break;
            case 2:
                this.step = recipe.getSteps().get(0);

                bundle.putSerializable(RECIPE, recipe);
                bundle.putSerializable(STEP, step);
                bundle.putBoolean(IS_TWO_PANE_LAYOUT, isTwoPaneLayout);

                recipeDetailsFragment.setArguments(bundle);
                recipeStepFragment.setArguments(bundle);
                recipeDetailsFragment.setCallback(this);

                if (isTwoPaneLayout) {
                    hideFragment(recipesFragment);
                    showFragment(R.id.recipes_steps, recipeStepFragment, R.id.recipes_details, recipeDetailsFragment, recipe.getName());
                    recipeStepFragment = (RecipeStepFragment) fragmentManager.findFragmentById(R.id.recipes_steps);
                  //  clickOnStep(recipe.getSteps().get(0));
                } else {
                    showFragment(R.id.frame_container, recipeDetailsFragment, recipe.getName());
                }
                break;
            case 3:
                if (isTwoPaneLayout) {
                    recipeStepFragment.setStep(step);
                } else {
                    bundle.putSerializable(STEP, step);
                    recipeStepFragment.setArguments(bundle);

                    hideFragment(recipeDetailsFragment);
                    showFragment(R.id.frame_container, recipeStepFragment, recipe.getName());
                }
                break;
        }
    }

    @Override
    public void clickOnRecipe(Recipe recipe) {
        this.recipe = recipe;
        show(2);
    }

    @Override
    public void clickOnStep(Step step) {
        this.step = step;
        show(3);
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
                .add(id, fragment)
                .add(id2, fragment2)
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

    private void hideFragment(Fragment fragment) {
        fragmentManager.beginTransaction().hide(fragment).commit();
    }

    private void showFragment(Fragment fragment) {
        fragmentManager.beginTransaction().show(fragment).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(BUNDLE, bundle);
    }

    @Override
    public void loadRecipes(List<Recipe> recipes) {
        this.recipes.setResponse(recipes);
        showRecipes();
    }
}
