package com.pes.become.frontend;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.pes.become.R;
import com.pes.become.backend.adapters.DomainAdapter;
import com.pes.become.backend.exceptions.RoutinePrimaryKeyException;

import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommunityRecyclerAdapter extends RecyclerView.Adapter<CommunityRecyclerAdapter.ViewHolder> {

    private final DomainAdapter DA = DomainAdapter.getInstance();
    private final ArrayList<ArrayList<Object>> communityRoutinesList;
    private final Context global;
    private static CommunityRecyclerAdapter instance;

    private String routineId;
    private String routineName;

    private int selected;

    /**
     * Constructora del CommunityRecyclerAdapter
     * @param communityRoutinesList llistat de rutines que es mostren al RecyclerView
     */
    public CommunityRecyclerAdapter(ArrayList<ArrayList<Object>> communityRoutinesList, Context global) {
        this.communityRoutinesList = communityRoutinesList;
        this.global = global;
        instance = this;
    }

    public static CommunityRecyclerAdapter getInstance() {
        return instance;
    }

    /**
     * Funcio del CommunityRecyclerAdapter que s'executa al crear-lo
     * @return retorna el ViewHolder que representa cada element de la llista
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.community_element, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Funcio del CommunityRecyclerAdapter que s'executa per cada element de la llista
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bitmap profilePic = (Bitmap)communityRoutinesList.get(position).get(5);
        if(profilePic != null)
            holder.profilePic.setImageBitmap(profilePic);
        holder.routineName.setText((String)communityRoutinesList.get(position).get(1));

        if (communityRoutinesList.get(position).get(3) != null){
            String avg = String.format("%.2f", (float)(double)communityRoutinesList.get(position).get(3));
            holder.valorationText.setText(avg);
        }
        else holder.valorationText.setText("0" + DecimalFormatSymbols.getInstance().getDecimalSeparator() + "0");

        holder.saveButton.setOnClickListener(view -> saveRoutine(position));

        holder.cardRoutineDisplayConstraintLayout.setOnClickListener(view -> getSharedRoutineActivities(position));
    }

    /**
     * Funcio del CommunityRecyclerAdapter per obtenir la mida del RecyclerView
     * @return la mida del llistat que es mostra al RecyclerView
     */
    @Override
    public int getItemCount() {
        return communityRoutinesList.size();
    }

    /**
     * Classe que representa un element de la llista del RecyclerView
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout cardRoutineDisplayConstraintLayout;
        TextView routineName, valorationText;
        ImageButton saveButton;
        CircleImageView profilePic;

        /**
         * Constructora del ViewHolder
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardRoutineDisplayConstraintLayout = itemView.findViewById(R.id.cardRoutineDisplayConstraintLayout);
            routineName = itemView.findViewById(R.id.routineName);
            saveButton = itemView.findViewById(R.id.saveButton);
            valorationText = itemView.findViewById(R.id.valorationText);
            profilePic = itemView.findViewById(R.id.profilePic);
        }
    }

    /**
     * Funcio per guardar una rutina de la comunitat
     * @param position posicio de la rutina dins l'array
     */
    private void saveRoutine(int position) {
        try {
            DA.downloadSharedRoutine(String.valueOf(communityRoutinesList.get(position).get(0)), String.valueOf(communityRoutinesList.get(position).get(1)));
            if(DA.checkAchievement("DownloadRoutine")) MainActivity.getInstance().showTrophyWon(global.getResources().getString(R.string.DownloadRoutine));
            Toast.makeText(global, global.getApplicationContext().getString(R.string.downOk), Toast.LENGTH_SHORT).show();
        } catch (RoutinePrimaryKeyException e) {
            Toast.makeText(global, global.getApplicationContext().getString(R.string.existingRoutineName), Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Funcio per mostrar les activitats d'una rutina de la comunitat
     * @param position posicio de la rutina dins l'array
     */
    private void getSharedRoutineActivities(int position) {
        this.selected = position;
        routineId = (String)communityRoutinesList.get(position).get(0);
        routineName = (String)communityRoutinesList.get(position).get(1);
        DA.getSharedRoutineActivities(routineId);
    }

    /**
     * Funcio de callback per mostrar les activitats d'una rutina de la comunitat
     * @param activitiesList llistat de rutines compartides
     */
    public void getSharedRoutineActivitiesCallback(HashMap<String, ArrayList<ArrayList<String>>> activitiesList) {
        double currentAverage;
        if (communityRoutinesList.get(selected).get(3) == null) currentAverage = 0;
        else currentAverage = Double.parseDouble(communityRoutinesList.get(selected).get(3).toString());
        MainActivity.getInstance().setCommunityRoutineViewScreen(routineId, routineName, activitiesList, (int)communityRoutinesList.get(this.selected).get(2), currentAverage, Integer.parseInt(communityRoutinesList.get(selected).get(4).toString()));
    }
}
