package net.bashayer.baking.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.bashayer.baking.R;
import net.bashayer.baking.callback.StepsCallback;
import net.bashayer.baking.model.Step;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.RecipeViewHolder> {

    private List<Step> steps;
    private StepsCallback callback;

    public StepsAdapter(StepsCallback callback, List<Step> steps) {
        this.callback = callback;
        this.steps = steps;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecipeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_step, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        final Step step = steps.get(position);
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.clickOnStep(step);
            }
        });

        holder.descriptionTextView.setText(step.getShortDescription());
    }


    @Override
    public int getItemCount() {
        return steps.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView descriptionTextView;
        CardView root;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            this.descriptionTextView = itemView.findViewById(R.id.string);
            this.root = itemView.findViewById(R.id.root);
        }
    }
}
