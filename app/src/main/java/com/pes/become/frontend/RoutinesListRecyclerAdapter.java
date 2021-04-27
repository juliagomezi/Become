package com.pes.become.frontend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pes.become.R;
import com.pes.become.backend.adapters.DomainAdapter;

import java.util.ArrayList;

public class RoutinesListRecyclerAdapter extends RecyclerView.Adapter<RoutinesListRecyclerAdapter.ViewHolder> {

    private final DomainAdapter DA = DomainAdapter.getInstance();
    private final ArrayList<ArrayList<String>> routinesList;
    private String selectedRoutineID;

    /**
     * Constructora del RecyclerAdapterRoutinesList
     * @param routinesList llistat d'activitats que es mostren al RecyclerView
     */
    public RoutinesListRecyclerAdapter(ArrayList<ArrayList<String>> routinesList, String selectedRoutineID) {
        this.routinesList = routinesList;
        this.selectedRoutineID = selectedRoutineID;
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

        viewHolder.switchButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    selectRoutine(viewHolder.getAdapterPosition());
                } catch (NoSuchMethodException ignore) { }
            }
        });

        return viewHolder;
    }

    /**
     * Funcio del RecyclerAdapterRoutinesList que s'executa per cada element de la llista
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.routineName.setText(routinesList.get(position).get(1));

        holder.editButton.setOnClickListener(view -> {
            try {
                DA.selectRoutine(routinesList.get(position));
            } catch (NoSuchMethodException ignore) {}
            MainActivity.getInstance().setEditRoutineScreen(routinesList.get(position).get(0),routinesList.get(position).get(1));
        });

        holder.deleteButton.setOnClickListener(view -> {
            String currentId = routinesList.get(position).get(0);
            DA.deleteRoutine(currentId);
            notifyDataSetChanged();
            if (routinesList.isEmpty()) RoutinesList.getInstance().initEmptyView();
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

    private void selectRoutine(int position) throws NoSuchMethodException {
        DA.selectRoutine(routinesList.get(position));
        selectedRoutineID = routinesList.get(position).get(0);
        notifyDataSetChanged();
    }
}
