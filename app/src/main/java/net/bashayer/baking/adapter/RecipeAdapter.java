package net.bashayer.baking.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.bashayer.baking.R;
import net.bashayer.baking.callback.RecipesCallback;
import net.bashayer.baking.model.Recipe;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipes;
    private RecipesCallback callback;

    public RecipeAdapter(RecipesCallback callback, List<Recipe> recipes) {
        this.callback = callback;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecipeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        final Recipe recipe = recipes.get(position);
        holder.stringTextView.setText(recipe.getName());
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.clickOnRecipe(recipe);
            }
        });

    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView stringTextView;
        CardView root;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            this.stringTextView = itemView.findViewById(R.id.string);
            this.root = itemView.findViewById(R.id.root);
        }
    }
}
