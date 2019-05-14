package net.bashayer.baking.callback;

import net.bashayer.baking.model.Recipe;
import net.bashayer.baking.model.Recipes;

import java.util.List;

public interface LoadRecipesCallback {

    void loadRecipes(List<Recipe> recipes);
}
