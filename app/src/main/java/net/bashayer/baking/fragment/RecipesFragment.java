package net.bashayer.baking.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.bashayer.baking.R;
import net.bashayer.baking.adapter.RecipeAdapter;
import net.bashayer.baking.callback.RecipesCallback;
import net.bashayer.baking.model.Recipes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static net.bashayer.baking.Constants.IS_TWO_PANE_LAYOUT;
import static net.bashayer.baking.Constants.RECIPES;

public class RecipesFragment extends Fragment {

    private RecipeAdapter adapter;
    private Recipes recipes;
    private RecipesCallback callback;

    private boolean isTwoPaneLayout = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            recipes = (Recipes) bundle.getSerializable(RECIPES);
            isTwoPaneLayout = bundle.getBoolean(IS_TWO_PANE_LAYOUT);

            adapter = new RecipeAdapter(callback, recipes.getResponse());

            if (isTwoPaneLayout) {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
                recyclerView.setLayoutManager(gridLayoutManager);
            } else {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(linearLayoutManager);
            }
            recyclerView.setAdapter(adapter);
        }
    }

    public void setCallback(RecipesCallback callback) {
        this.callback = callback;
    }
}
