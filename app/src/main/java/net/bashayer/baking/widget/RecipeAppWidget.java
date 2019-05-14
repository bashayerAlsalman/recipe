package net.bashayer.baking.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import net.bashayer.baking.JSONUtils;
import net.bashayer.baking.R;
import net.bashayer.baking.callback.LoadRecipesCallback;
import net.bashayer.baking.callback.RecipesCallback;
import net.bashayer.baking.model.Ingredient;
import net.bashayer.baking.model.Recipe;
import net.bashayer.baking.model.Recipes;
import net.bashayer.baking.network.NetworkUtils;

import java.io.InputStream;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeAppWidget extends AppWidgetProvider implements LoadRecipesCallback {
    private static List<Recipe> recipes;
    private static int recipeIndex = 0;
    private static int ingredientIndex = 0;

    public RecipeAppWidget() {
        NetworkUtils networkUtils = new NetworkUtils(this);
        networkUtils.loadRecipes();
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        Recipe recipe = recipes.get(recipeIndex);
        String string = recipe.getName()
                + "\n"
                + (ingredientIndex + 1)
                + ". "
                + recipe.getIngredients().get(ingredientIndex).toString();
        updateIndex();

        //CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_app_widget);
        views.setTextViewText(R.id.appwidget_text, string);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

//        NetworkUtils networkUtils = new NetworkUtils(this);
//        networkUtils.loadRecipes();
//
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private static void updateIndex() {
        if (recipeIndex == recipes.size()) {
            recipeIndex = 0;
        } else {
            recipeIndex++;
        }

        if (ingredientIndex == recipes.get(recipeIndex).getIngredients().size()) {
            ingredientIndex = 0;
        } else {
            ingredientIndex++;
        }

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void loadRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
}

