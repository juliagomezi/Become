package com.pes.become.frontend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.pes.become.R;

import java.util.ArrayList;

public class RecommendationsRecyclerAdapter extends RecyclerView.Adapter<RecommendationsRecyclerAdapter.ViewHolder> {

    ArrayList<Integer> recommendationsList;
    private Context global;

    /**
     * Constructora del RecommendationsRecyclerAdapter
     * @param recommendationsList llistat de recomanacions que es mostren al RecyclerView
     */
    public RecommendationsRecyclerAdapter(ArrayList<Integer> recommendationsList, Context global) {
        this.recommendationsList = recommendationsList;
        this.global = global;
    }

    /**
     * Funcio del RecommendationsRecyclerAdapter que s'executa al crear-lo
     * @return retorna el ViewHolder que representa cada element de la llista
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recommendations_element, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    /**
     * Funcio del RecommendationsRecyclerAdapter que s'executa per cada element de la llista
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        initializeCards(holder, position);
        if (recommendationsList.get(position) == -1) {
            String hours = "";
            switch (position){
                case 0:
                case 6:
                    hours = "00:30h";
                    break;
                case 1:
                    hours = "03:30h";
                    break;
                case 2:
                    hours = "42:00h";
                    break;
                case 3:
                    hours = "10:00h";
                    break;
                case 4:
                    hours = "40:00h";
                    break;
                case 5:
                    hours = "20:00h";
                    break;
                case 7:
                    hours = "00:00h";
                    break;
            }
            String text = global.getApplicationContext().getResources().getString(R.string.recommendationMoreTime);
            holder.recommendationsDescription.setText(text + " " + hours);
            holder.cardRecommendationDisplay.setBackgroundResource(R.drawable.theme_working_background);
        }
        else if (recommendationsList.get(position) == 1) {
            String hours = "";
            switch (position){
                case 0:
                    hours = "20:00h";
                    break;
                case 1:
                case 3:
                    hours = "28:00h";
                    break;
                case 2:
                    hours = "72:00h";
                    break;
                case 4:
                    hours = "70:00h";
                    break;
                case 5:
                    hours = "36:00h";
                    break;
                case 6:
                    hours = "15:00h";
                    break;
                case 7:
                    hours = "170:00h";
                    break;
            }
            String text = global.getApplicationContext().getResources().getString(R.string.recommendationsLessTime);
            holder.recommendationsDescription.setText(text + " " + hours);
            holder.cardRecommendationDisplay.setBackgroundResource(R.drawable.theme_working_background);
        }
        else {
            holder.recommendationsDescription.setText(R.string.recommendationsOkTime);
            holder.cardRecommendationDisplay.setBackgroundResource(R.drawable.theme_plants_background);
        }
    }

    /**
     * Funcio per inicialitzar les cards
     * @param holder viewholder
     * @param position posicio dins el array
     */
    private void initializeCards(ViewHolder holder, int position) {
        switch (position) {
            case 0:
                holder.recommendationsTitle.setText(R.string.music);
                break;
            case 1:
                holder.recommendationsTitle.setText(R.string.sport);
                break;
            case 2:
                holder.recommendationsTitle.setText(R.string.sleeping);
                break;
            case 3:
                holder.recommendationsTitle.setText(R.string.cooking);
                break;
            case 4:
                holder.recommendationsTitle.setText(R.string.working);
                break;
            case 5:
                holder.recommendationsTitle.setText(R.string.entertainment);
                break;
            case 6:
                holder.recommendationsTitle.setText(R.string.plants);
                break;
            default:
                holder.recommendationsTitle.setText(R.string.other);
                break;
        }
    }

    /**
     * Funcio del RecommendationsRecyclerAdapter per obtenir la mida del RecyclerView
     * @return la mida del llistat que es mostra al RecyclerView
     */
    @Override
    public int getItemCount() {
        return recommendationsList.size();
    }

    /**
     * Classe que representa un element de la llista del RecyclerView
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayoutDisplay;
        CardView cardRecommendationDisplay;
        TextView recommendationsTitle, recommendationsDescription;

        /**
         * Constructora del ViewHolder
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardRecommendationDisplay = itemView.findViewById(R.id.cardRecommendationDisplay);
            recommendationsTitle = itemView.findViewById(R.id.recommendationsTitle);
            recommendationsDescription = itemView.findViewById(R.id.recommendationsDescription);
        }
    }

}
