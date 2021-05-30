package com.pes.become.frontend;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;
import com.pes.become.R;
import com.pes.become.backend.adapters.DomainAdapter;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommunityRecyclerAdapter extends RecyclerView.Adapter<CommunityRecyclerAdapter.ViewHolder> {

    private final DomainAdapter DA = DomainAdapter.getInstance();
    private final ArrayList<ArrayList<Object>> communityRoutinesList;
    private Context global;

    private View view;
    private ViewGroup parent;

    private BottomSheetDialog voteRoutineSheet;
    private NumberPicker routineVote;

    /**
     * Constructora del CommunityRecyclerAdapter
     * @param communityRoutinesList llistat de rutines que es mostren al RecyclerView
     */
    public CommunityRecyclerAdapter(ArrayList<ArrayList<Object>> communityRoutinesList, Context global) {
        this.communityRoutinesList = communityRoutinesList;
        this.global = global;
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
        holder.routineName.setText((String)communityRoutinesList.get(position).get(2));
        holder.profilePic.setImageBitmap((Bitmap)communityRoutinesList.get(position).get(1));

        if ((boolean)communityRoutinesList.get(position).get(3)) holder.valorationIcon.setImageResource(R.drawable.ic_star_filled);
        else holder.valorationIcon.setImageResource(R.drawable.ic_star);

        holder.valorationIcon.setOnClickListener(view -> createVoteRoutineSheet(position));


        if (communityRoutinesList.get(position).get(4) != null) holder.valorationText.setText(String.valueOf(communityRoutinesList.get(position).get(4)));
        else holder.valorationText.setText("");

        holder.saveButton.setOnClickListener(view -> saveRoutine(position));

        holder.cardRoutineDisplayConstraintLayout.setOnClickListener(view -> seeRoutine(position));
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
        Log.d("saveRoutine", "saveRoutine");
        //DA.downloadSharedRoutine(String.valueOf(communityRoutinesList.get(position).get(0)));
        //MainActivity.getInstance().setProfileScreen();
    }

    /**
     * Funcio per mostrar les activitats d'una rutina de la comunitat
     * @param position posicio de la rutina dins l'array
     */
    private void seeRoutine(int position) {
        ArrayList<ArrayList<String>> activitiesList = new ArrayList<>();
        ArrayList<String> activity = new ArrayList<>();
        activity.add("Id");
        activity.add("MON A0");
        activity.add("Description");
        activity.add("Music");
        activity.add("Monday");
        activity.add("00:00");
        activity.add("01:00");
        activity.add("false");
        activitiesList.add(activity);
        activity = new ArrayList<>();
        activity.add("Id");
        activity.add("TUE A0");
        activity.add("Description");
        activity.add("Music");
        activity.add("Tuesday");
        activity.add("00:00");
        activity.add("01:00");
        activity.add("false");
        activitiesList.add(activity);
        activity = new ArrayList<>();
        activity.add("Id");
        activity.add("WED A0");
        activity.add("Description");
        activity.add("Music");
        activity.add("Wednesday");
        activity.add("00:00");
        activity.add("01:00");
        activity.add("false");
        activitiesList.add(activity);
        activity = new ArrayList<>();
        activity.add("Id");
        activity.add("THUR A0");
        activity.add("Description");
        activity.add("Music");
        activity.add("Thursday");
        activity.add("00:00");
        activity.add("01:00");
        activity.add("false");
        activitiesList.add(activity);
        activity = new ArrayList<>();
        activity.add("Id");
        activity.add("FRI A0");
        activity.add("Description");
        activity.add("Music");
        activity.add("Friday");
        activity.add("00:00");
        activity.add("01:00");
        activity.add("false");
        activitiesList.add(activity);
        activity = new ArrayList<>();
        activity.add("Id");
        activity.add("SAT A0");
        activity.add("Description");
        activity.add("Music");
        activity.add("Saturday");
        activity.add("00:00");
        activity.add("01:00");
        activity.add("false");
        activitiesList.add(activity);
        activity = new ArrayList<>();
        activity.add("Id");
        activity.add("SUN A0");
        activity.add("Description");
        activity.add("Music");
        activity.add("Sunday");
        activity.add("00:00");
        activity.add("01:00");
        activity.add("false");
        activitiesList.add(activity);
        activity = new ArrayList<>();
        activity.add("Id");
        activity.add("SUN A0");
        activity.add("Description");
        activity.add("Music");
        activity.add("Sunday");
        activity.add("00:00");
        activity.add("01:00");
        activity.add("false");
        activitiesList.add(activity);
        activity = new ArrayList<>();
        activity.add("Id");
        activity.add("SUN A0");
        activity.add("Description");
        activity.add("Music");
        activity.add("Sunday");
        activity.add("00:00");
        activity.add("01:00");
        activity.add("false");
        activitiesList.add(activity);
        activity = new ArrayList<>();
        activity.add("Id");
        activity.add("SUN A0");
        activity.add("Description");
        activity.add("Music");
        activity.add("Sunday");
        activity.add("00:00");
        activity.add("01:00");
        activity.add("false");
        activitiesList.add(activity);
        activity = new ArrayList<>();
        activity.add("Id");
        activity.add("SUN A0");
        activity.add("Description");
        activity.add("Music");
        activity.add("Sunday");
        activity.add("00:00");
        activity.add("01:00");
        activity.add("false");
        activitiesList.add(activity);
        activity = new ArrayList<>();
        activity.add("Id");
        activity.add("SUN A0");
        activity.add("Description");
        activity.add("Music");
        activity.add("Sunday");
        activity.add("00:00");
        activity.add("01:00");
        activity.add("false");
        activitiesList.add(activity);
        activity = new ArrayList<>();
        activity.add("Id");
        activity.add("SUN A0");
        activity.add("Description");
        activity.add("Music");
        activity.add("Sunday");
        activity.add("00:00");
        activity.add("01:00");
        activity.add("false");
        activitiesList.add(activity);
        activity = new ArrayList<>();
        activity.add("Id");
        activity.add("SUN A0");
        activity.add("Description");
        activity.add("Music");
        activity.add("Sunday");
        activity.add("00:00");
        activity.add("01:00");
        activity.add("false");
        activitiesList.add(activity);
        activity = new ArrayList<>();
        activity.add("Id");
        activity.add("SUN A0");
        activity.add("Description");
        activity.add("Music");
        activity.add("Sunday");
        activity.add("00:00");
        activity.add("01:00");
        activity.add("false");
        activitiesList.add(activity);
        activity = new ArrayList<>();
        activity.add("Id");
        activity.add("SUN A0");
        activity.add("Description");
        activity.add("Music");
        activity.add("Sunday");
        activity.add("00:00");
        activity.add("01:00");
        activity.add("false");
        activitiesList.add(activity);

        MainActivity.getInstance().setCommunityRoutineViewScreen(String.valueOf(communityRoutinesList.get(position).get(0)), activitiesList);
        //MainActivity.getInstance().setViewCommunityRoutineScreen(DA.getSharedRoutinesActivities(communityRoutinesList.get(position).get(0));
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
