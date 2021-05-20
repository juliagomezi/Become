package com.pes.become.frontend;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pes.become.R;
import com.pes.become.backend.adapters.DomainAdapter;

import java.util.ArrayList;

public class RoutinesListRecyclerAdapter extends RecyclerView.Adapter<RoutinesListRecyclerAdapter.ViewHolder> {

    private final DomainAdapter DA = DomainAdapter.getInstance();
    private final ArrayList<ArrayList<String>> routinesList;
    private String selectedRoutineID;
    private Context global;

    /**
     * Constructora del RecyclerAdapterRoutinesList
     * @param routinesList llistat d'activitats que es mostren al RecyclerView
     */
    public RoutinesListRecyclerAdapter(ArrayList<ArrayList<String>> routinesList, String selectedRoutineID, Context global) {
        this.routinesList = routinesList;
        this.selectedRoutineID = selectedRoutineID;
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
        View view = layoutInflater.inflate(R.layout.routines_list_element, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.switchButton.setOnClickListener(view1 -> selectRoutine(viewHolder.getAdapterPosition()));

        return viewHolder;
    }

    /**
     * Funcio del RecyclerAdapterRoutinesList que s'executa per cada element de la llista
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.routineName.setText(routinesList.get(position).get(1));

        holder.editButton.setOnClickListener(view -> {
            DA.selectRoutine(routinesList.get(position), false);
            MainActivity.getInstance().setEditRoutineScreen(routinesList.get(position).get(0),routinesList.get(position).get(1));
        });

        holder.deleteButton.setOnClickListener(view -> {
            if(routinesList.get(position).get(0).equals(selectedRoutineID)) {
                Toast.makeText(global, global.getApplicationContext().getString(R.string.deselectToRemove), Toast.LENGTH_SHORT).show();
            } else {
                new AlertDialog.Builder(global)
                        .setTitle(R.string.deleteRoutine)
                        .setMessage(R.string.deleteRoutineConfirmation)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                            String currentId = routinesList.get(position).get(0);
                            if (!currentId.equals(selectedRoutineID)) {
                                DA.deleteRoutine(currentId);
                                notifyDataSetChanged();
                                if (routinesList.isEmpty())
                                    RoutinesList.getInstance().initEmptyView();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        boolean isSelected = routinesList.get(position).get(0).equals(selectedRoutineID);
        holder.switchButton.setChecked(isSelected);
    }


    /**
     * Funcio del RecyclerAdapterRoutinesList per obtenir la mida del RecyclerView
     * @return la mida del llistat que es mostra al RecyclerView
     */
    @Override
    public int getItemCount() {
        return routinesList.size();
    }

    /**
     * Classe que representa un element de la llista del RecyclerView
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView routineName;
        ImageButton editButton, deleteButton;
        Switch switchButton;

        /**
         * Constructora del ViewHolder
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            routineName = itemView.findViewById(R.id.routineName);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            switchButton = itemView.findViewById(R.id.switchButton);
        }
    }

    private void selectRoutine(int position) {
        if(routinesList.get(position).get(0).equals(selectedRoutineID)) {
            DA.selectRoutine(null, true);
            selectedRoutineID = "";
        } else {
            DA.selectRoutine(routinesList.get(position), true);
            selectedRoutineID = routinesList.get(position).get(0);
        }
        notifyDataSetChanged();
    }
}
