package net.bashayer.baking.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.bashayer.baking.R;
import net.bashayer.baking.adapter.IngredientsAdapter;
import net.bashayer.baking.adapter.StepsAdapter;
import net.bashayer.baking.callback.StepsCallback;
import net.bashayer.baking.model.Ingredient;
import net.bashayer.baking.model.Recipe;
import net.bashayer.baking.model.Step;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static net.bashayer.baking.Constants.IS_TWO_PANE_LAYOUT;
import static net.bashayer.baking.Constants.RECIPE;

public class RecipeDetailsFragment extends Fragment {

    private Recipe recipe;
    private List<Ingredient> ingrediens;
    private List<Step> steps;

    private IngredientsAdapter ingredientsAdapter;
    private StepsAdapter stepsAdapter;

    private StepsCallback callback;

    private RecyclerView ingredientsRecyclerView;
    private RecyclerView stepsRecyclerView;

    private boolean isTwoPaneLayout = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ingredientsRecyclerView = view.findViewById(R.id.ingredients_recycler_view);
        stepsRecyclerView = view.findViewById(R.id.steps_recycler_view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            recipe = (Recipe) bundle.getSerializable(RECIPE);
            isTwoPaneLayout = bundle.getBoolean(IS_TWO_PANE_LAYOUT);
            ingrediens = recipe.getIngredients();
            steps = recipe.getSteps();

            initAdapter();
        }

    }

    private void initAdapter() {
        ingredientsAdapter = new IngredientsAdapter(ingrediens);
        stepsAdapter = new StepsAdapter(callback, steps);

        LinearLayoutManager ingredientLinearLayoutManager = new LinearLayoutManager(getContext());
        LinearLayoutManager stepLinearLayoutManager = new LinearLayoutManager(getContext());

        ingredientsRecyclerView.setLayoutManager(ingredientLinearLayoutManager);
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);

        stepsRecyclerView.setLayoutManager(stepLinearLayoutManager);
        stepsRecyclerView.setAdapter(stepsAdapter);
    }

    public void setCallback(StepsCallback callback) {
        this.callback = callback;
    }
}
