package net.bashayer.baking.network;

import net.bashayer.baking.callback.LoadRecipesCallback;
import net.bashayer.baking.model.Recipe;
import net.bashayer.baking.model.Recipes;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtils {

    private RecipeService service;
    private LoadRecipesCallback loadRecipesCallback;

    public NetworkUtils(LoadRecipesCallback loadRecipesCallback) {
        this.loadRecipesCallback = loadRecipesCallback;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(RecipeService.class);
    }

    public void loadRecipes() {
        Call<List<Recipe>> call = service.getRecipes();

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                setRecipes(response.body());
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setRecipes(List<Recipe> recipes) {
        this.loadRecipesCallback.loadRecipes(recipes);
    }

}
