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

    private Context context;
    private AppWidgetManager appWidgetManager;
    private int[] appWidgetIds;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Recipe recipe = recipes.get(recipeIndex);
        StringBuilder string = new StringBuilder();
        string.append(recipe.getName());
        string.append("\n");
        for (int i = 0; i < recipes.get(recipeIndex).getIngredients().size(); i++) {
            string.append(i + 1)
                    .append(". ")
                    .append(recipe.getIngredients().get(i));
        }

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
        this.context = context;
        this.appWidgetManager = appWidgetManager;
        this.appWidgetIds = appWidgetIds;
        NetworkUtils networkUtils = new NetworkUtils(this);
        networkUtils.loadRecipes();
    }

    private static void updateIndex() {
        if (recipeIndex == recipes.size()) {
            recipeIndex = 0;
        } else {
            recipeIndex++;
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

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
}

