package com.pes.become.frontend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.pes.become.R;

import java.util.ArrayList;
import java.util.Arrays;

public class CommunityRoutineViewRecyclerAdapter extends RecyclerView.Adapter<CommunityRoutineViewRecyclerAdapter.ViewHolder> {

    private final ArrayList<ArrayList<String>> activitiesList;
    private final Boolean[] isExpanded;

    /**
     * Constructora del RecyclerAdapter
     * @param activitiesList llistat d'activitats que es mostren al RecyclerView
     */
    public CommunityRoutineViewRecyclerAdapter(ArrayList<ArrayList<String>> activitiesList) {
        this.activitiesList = activitiesList;
        isExpanded = new Boolean[activitiesList.size()];
        Arrays.fill(isExpanded, Boolean.FALSE);
    }

    /**
     * Funcio del RecyclerAdapter que s'executa al crear-lo
     * @return retorna el ViewHolder que representa cada element de la llista
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.routine_view_element, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Funcio del RecyclerAdapter que s'executa per cada element de la llista
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.activityNameDisplay.setText(activitiesList.get(position).get(1));
        holder.activityDescriptionDisplay.setText(activitiesList.get(position).get(2));
        holder.startTimeDisplay.setText(activitiesList.get(position).get(5));
        holder.endTimeDisplay.setText(activitiesList.get(position).get(6));
        holder.expandableLayout.setVisibility(isExpanded[position] ? View.VISIBLE : View.GONE);

        boolean isDone = activitiesList.get(position).get(7).equals("true");
        String theme = activitiesList.get(position).get(3);

        switch (theme) {
            case "Cooking":
                holder.cardActivityDisplay.setBackgroundResource(R.drawable.theme_cooking_background);
                break;
            case "Entertainment":
                holder.cardActivityDisplay.setBackgroundResource(R.drawable.theme_entertainment_background);
                break;
            case "Music":
                holder.cardActivityDisplay.setBackgroundResource(R.drawable.theme_music_background);
                break;
            case "Sleeping":
                holder.cardActivityDisplay.setBackgroundResource(R.drawable.theme_sleeping_bacground);
                break;
            case "Sport":
                holder.cardActivityDisplay.setBackgroundResource(R.drawable.theme_sport_background);
                break;
            case "Plants":
                holder.cardActivityDisplay.setBackgroundResource(R.drawable.theme_plants_background);
                break;
            case "Working":
                holder.cardActivityDisplay.setBackgroundResource(R.drawable.theme_working_background);
                break;
            default:
                holder.cardActivityDisplay.setBackgroundResource(R.drawable.theme_other_background);
                break;
        }
        if(isDone) holder.cardActivityDisplay.setAlpha(0.3f);
    }

    /**
     * Funcio del RecyclerAdapter per obtenir la mida del RecyclerView
     * @return la mida del llistat que es mostra al RecyclerView
     */
    @Override
    public int getItemCount() {
        return activitiesList.size();
    }

    /**
     * Classe que representa un element de la llista del RecyclerView
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardActivityDisplay;
        TextView startTimeDisplay, endTimeDisplay, activityNameDisplay, activityDescriptionDisplay;
        ConstraintLayout expandableLayout;

        /**
         * Constructora del ViewHolder
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardActivityDisplay = itemView.findViewById(R.id.cardActivityDisplay);

            startTimeDisplay = itemView.findViewById(R.id.startTimeDisplay);
            endTimeDisplay = itemView.findViewById(R.id.endTimeDisplay);
            activityNameDisplay = itemView.findViewById(R.id.activityNameDisplay);
            activityDescriptionDisplay = itemView.findViewById(R.id.activityDescriptionDisplay);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);

            activityNameDisplay.setOnClickListener(view -> {
                isExpanded[getAdapterPosition()] = !isExpanded[getAdapterPosition()];
                notifyItemChanged(getAdapterPosition());
            });

        }

    }

}