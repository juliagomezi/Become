package com.pes.become.frontend;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pes.become.R;
import com.pes.become.backend.adapters.DomainAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommunityRecyclerAdapter extends RecyclerView.Adapter<CommunityRecyclerAdapter.ViewHolder> {

    private final DomainAdapter DA = DomainAdapter.getInstance();
    private final ArrayList<ArrayList<Object>> communityRoutinesList;
    private final Context global;
    private static CommunityRecyclerAdapter instace;

    private View view;
    private ViewGroup parent;

    private BottomSheetDialog voteRoutineSheet;
    private NumberPicker routineVote;

    private String routineId;
    private String routineName;

    /**
     * Constructora del CommunityRecyclerAdapter
     * @param communityRoutinesList llistat de rutines que es mostren al RecyclerView
     */
    public CommunityRecyclerAdapter(ArrayList<ArrayList<Object>> communityRoutinesList, Context global) {
        this.communityRoutinesList = communityRoutinesList;
        this.global = global;
        instace = this;
    }

    public static CommunityRecyclerAdapter getInstance() {
        return instace;
    }

    /**
     * Funcio del CommunityRecyclerAdapter que s'executa al crear-lo
     * @return retorna el ViewHolder que representa cada element de la llista
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.parent = parent;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.community_element, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    /**
     * Funcio del CommunityRecyclerAdapter que s'executa per cada element de la llista
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        communityRoutinesList.get(position).get(5);
        //holder.profilePic.setImageBitmap((Bitmap)communityRoutinesList.get(position).get(5));
        holder.routineName.setText((String)communityRoutinesList.get(position).get(1));

        if ((boolean)communityRoutinesList.get(position).get(2)) holder.valorationIcon.setImageResource(R.drawable.ic_star_filled);
        else holder.valorationIcon.setImageResource(R.drawable.ic_star);

        holder.valorationIcon.setOnClickListener(view -> createVoteRoutineSheet(position));

        if (communityRoutinesList.get(position).get(3) != null) holder.valorationText.setText(String.valueOf(communityRoutinesList.get(position).get(3)));
        else holder.valorationText.setText("--");

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
        ImageButton saveButton, valorationIcon;
        CircleImageView profilePic;

        /**
         * Constructora del ViewHolder
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardRoutineDisplayConstraintLayout = itemView.findViewById(R.id.cardRoutineDisplayConstraintLayout);
            routineName = itemView.findViewById(R.id.routineName);
            saveButton = itemView.findViewById(R.id.saveButton);
            valorationIcon = itemView.findViewById(R.id.valorationIcon);
            valorationText = itemView.findViewById(R.id.valorationText);
            profilePic = itemView.findViewById(R.id.profilePic);
        }
    };

    /**
     * Funcio per guardar una rutina de la comunitat
     * @param position posicio de la rutina dins l'array
     */
    private void saveRoutine(int position) {
        DA.downloadSharedRoutine(String.valueOf(communityRoutinesList.get(position).get(0)), String.valueOf(communityRoutinesList.get(position).get(1)));
        MainActivity.getInstance().setProfileScreen();
    }

    /**
     * Funcio per mostrar les activitats d'una rutina de la comunitat
     * @param position posicio de la rutina dins l'array
     */
    private void getSharedRoutineActivities(int position) {
        routineId = (String)communityRoutinesList.get(position).get(0);
        routineName = (String)communityRoutinesList.get(position).get(1);
        DA.getSharedRoutineActivities(routineId);
    }

    public void getSharedRoutineActivitiesCallback(HashMap<String, ArrayList<ArrayList<String>>> activitiesList) {
        MainActivity.getInstance().setCommunityRoutineViewScreen(routineId, routineName, activitiesList);
    }

    /**
     * Funció per crear la pestanya de votacio de rutina
     * @param position posicio de la rutina al llistat
     */
    public void createVoteRoutineSheet(int position) {
        voteRoutineSheet = new BottomSheetDialog(global,R.style.BottomSheetTheme);
        View sheetView = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_routine_vote, view.findViewById(R.id.bottom_sheet));

        routineVote = sheetView.findViewById(R.id.routineVote);
        routineVote.setMaxValue(10);
        routineVote.setMinValue(0);
        Button doneButton = sheetView.findViewById(R.id.doneButton);
        Button cancelButton = sheetView.findViewById(R.id.cancelButton);
        doneButton.setOnClickListener(v -> voteRoutine(position));
        cancelButton.setOnClickListener(v -> voteRoutineSheet.dismiss());

        voteRoutineSheet.setContentView(sheetView);
        voteRoutineSheet.show();
    }

    /**
     * Funció per valorar una nova rutina
     * @param position posicio de la rutina al llistat
     */
    private void voteRoutine(int position) {
        //DA.voteRoutine((String) communityRoutinesList.get(position).get(0), routineVote.getValue(), Double.parseDouble((String) communityRoutinesList.get(position).get(4)), (Integer)communityRoutinesList.get(position).get(5));
        voteRoutineSheet.dismiss();
        communityRoutinesList.get(position).set(4, 10);
        MainActivity.getInstance().setCommunityScreen();
    }
}
