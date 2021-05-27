package com.pes.become.frontend;

import android.content.Context;
import android.graphics.Bitmap;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class CommunityRecyclerAdapter extends RecyclerView.Adapter<CommunityRecyclerAdapter.ViewHolder> {

    private final DomainAdapter DA = DomainAdapter.getInstance();
    private final ArrayList<ArrayList<Object>> communityRoutinesList;
    private Context global;

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
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.community_element, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    /**
     * Funcio del CommunityRecyclerAdapter que s'executa per cada element de la llista
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.routineName.setText((String)communityRoutinesList.get(position).get(3));
        holder.profilePic.setImageBitmap((Bitmap)communityRoutinesList.get(position).get(1));

        if (!(Boolean)communityRoutinesList.get(position).get(4)) holder.saveButton.setImageResource(R.drawable.ic_bookmark);
        else holder.saveButton.setImageResource(R.drawable.ic_bookmark_filled);

        if (communityRoutinesList.get(position).get(5) != null) holder.valorationIcon.setImageResource(R.drawable.ic_star_filled);
        else holder.valorationIcon.setImageResource(R.drawable.ic_star);

        if (communityRoutinesList.get(position).get(5) != null) holder.valorationText.setText(String.valueOf(communityRoutinesList.get(position).get(5)));
        else holder.valorationText.setText("");

        holder.saveButton.setOnClickListener(view -> saveRoutine(holder, position));
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

        TextView routineName, valorationText;
        ImageButton saveButton, valorationIcon;
        CircleImageView profilePic;

        /**
         * Constructora del ViewHolder
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            routineName = itemView.findViewById(R.id.routineName);
            saveButton = itemView.findViewById(R.id.saveButton);
            valorationIcon = itemView.findViewById(R.id.valorationIcon);
            valorationText = itemView.findViewById(R.id.valorationText);
            profilePic = itemView.findViewById(R.id.profilePic);
        }
    };

    private void saveRoutine(ViewHolder holder, int position) {
        if(!(boolean)communityRoutinesList.get(position).get(4)) {
            holder.saveButton.setImageResource(R.drawable.ic_bookmark_filled);
            //DA.saveRoutine(usedId, routineId);
        } else {
            holder.saveButton.setImageResource(R.drawable.ic_bookmark);
            //DA.unsaveRoutine(usedId, routineId);
        }
    }
}
