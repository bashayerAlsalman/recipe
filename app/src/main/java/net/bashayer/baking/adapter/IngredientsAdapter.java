package net.bashayer.baking.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.bashayer.baking.R;
import net.bashayer.baking.model.Ingredient;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.RecipeViewHolder> {

    private List<Ingredient> ingredients;

    public IngredientsAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecipeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
            Ingredient ingredient = ingredients.get(position);
            holder.quantityTextView.setText(ingredient.getQuantity() + "");
            holder.measureTextView.setText(ingredient.getMeasure());
            holder.ingredientTextView.setText(ingredient.getIngredient());
    }


    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView quantityTextView;
        TextView measureTextView;
        TextView ingredientTextView;
        CardView root;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            this.quantityTextView = itemView.findViewById(R.id.quantity);
            this.measureTextView = itemView.findViewById(R.id.measure);
            this.ingredientTextView = itemView.findViewById(R.id.ingredient);
            this.root = itemView.findViewById(R.id.root);
        }
    }
}
