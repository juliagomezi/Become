package com.pes.become.frontend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.collect.Iterables;
import com.pes.become.R;
import com.pes.become.backend.adapters.DomainAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class TrophiesRecyclerAdapter extends RecyclerView.Adapter<TrophiesRecyclerAdapter.ViewHolder> {

    private final DomainAdapter DA = DomainAdapter.getInstance();
    ArrayList<ArrayList<String>> trophiesList;
    ArrayList<Boolean> obtainedTrophiesList;
    private Context global;

    /**
     * Constructora del RecyclerAdapterRoutinesList
     * @param trophiesList llistat de trofeus que es mostren al RecyclerView
     * @param obtainedTrophiesList boleans que indiquen si l'usuari te o no el trofeu
     */
    public TrophiesRecyclerAdapter(ArrayList<ArrayList<String>> trophiesList, ArrayList<Boolean> obtainedTrophiesList, Context global) {
        this.trophiesList = trophiesList;
        this.obtainedTrophiesList = obtainedTrophiesList;
        this.global = global;
    }

    /**
     * Funcio del RecyclerAdapterRoutinesList que s'executa al crear-lo
     * @return retorna el ViewHolder que representa cada element de la llista
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.trophies_element, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    /**
     * Funcio del RecyclerAdapterRoutinesList que s'executa per cada element de la llista
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.trophyName.setText(trophiesList.get(position).get(0));
        holder.trophyDescription.setText(trophiesList.get(position).get(1));


        if (obtainedTrophiesList.get(position)) {
            holder.trophyCard.setCardBackgroundColor(global.getResources().getColor(R.color.gold25));
            holder.trophyName.setTextColor(global.getResources().getColor(R.color.gold100));
            holder.trophyWonIcon.setVisibility(View.VISIBLE);
            holder.trophyNotWonIcon.setVisibility(View.INVISIBLE);
        }
        else {
            holder.trophyCard.setCardBackgroundColor(global.getResources().getColor(R.color.coldwhite));
            holder.trophyWonIcon.setVisibility(View.INVISIBLE);
            holder.trophyNotWonIcon.setVisibility(View.VISIBLE);
        }

    }


    /**
     * Funcio del RecyclerAdapterRoutinesList per obtenir la mida del RecyclerView
     * @return la mida del llistat que es mostra al RecyclerView
     */
    @Override
    public int getItemCount() {
        return trophiesList.size();
    }

    /**
     * Classe que representa un element de la llista del RecyclerView
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        CardView trophyCard;
        TextView trophyName, trophyDescription;
        ImageButton trophyWonIcon, trophyNotWonIcon;

        /**
         * Constructora del ViewHolder
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            trophyCard = itemView.findViewById(R.id.trophyCard);
            trophyName = itemView.findViewById(R.id.trophyName);
            trophyDescription = itemView.findViewById(R.id.trophyDescription);
            trophyWonIcon = itemView.findViewById(R.id.trophyWonIcon);
            trophyNotWonIcon = itemView.findViewById(R.id.trophyNotWonIcon);
        }
    }

}
