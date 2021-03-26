package com.pes.become.frontend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.pes.become.R;

import java.util.ArrayList;
import java.util.Arrays;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    // llistat d'activitats
    private ArrayList<ArrayList<String>> activitiesList;
    private Boolean[] isExpanded;

    public RecyclerAdapter(ArrayList<ArrayList<String>> activitiesList) {
        this.activitiesList = activitiesList;
        isExpanded = new Boolean[activitiesList.size()];
        Arrays.fill(isExpanded, Boolean.FALSE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.routine_edit_element, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.activityNameDisplay.setText(activitiesList.get(position).get(0));
        holder.activityDescriptionDisplay.setText(activitiesList.get(position).get(1));
        holder.startTimeDisplay.setText(activitiesList.get(position).get(4));
        holder.endTimeDisplay.setText(activitiesList.get(position).get(5));

        holder.expandableLayout.setVisibility(isExpanded[position] ? View.VISIBLE : View.GONE);

        String theme = activitiesList.get(position).get(2);
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
            case "Studying":
                holder.cardActivityDisplay.setBackgroundResource(R.drawable.theme_studying_background);
                break;
            case "Working":
                holder.cardActivityDisplay.setBackgroundResource(R.drawable.theme_working_background);
                break;
            default:
                holder.cardActivityDisplay.setBackgroundResource(R.drawable.theme_other_background);
                break;
        }

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RoutineEdit.getInstance().createActivitySheet(true);
                RoutineEdit.getInstance().fillActivitySheet(activitiesList.get(position).get(0), activitiesList.get(position).get(1), activitiesList.get(position).get(2), activitiesList.get(position).get(3), activitiesList.get(position).get(4), activitiesList.get(position).get(5));
            }
        });
    }

    @Override
    public int getItemCount() {
        return activitiesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardActivityDisplay;
        TextView startTimeDisplay, endTimeDisplay, activityNameDisplay, activityDescriptionDisplay;
        ConstraintLayout expandableLayout;
        ImageButton editButton, deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardActivityDisplay = itemView.findViewById(R.id.cardActivityDisplay);

            startTimeDisplay = itemView.findViewById(R.id.startTimeDisplay);
            endTimeDisplay = itemView.findViewById(R.id.endTimeDisplay);
            activityNameDisplay = itemView.findViewById(R.id.activityNameDisplay);
            activityDescriptionDisplay = itemView.findViewById(R.id.activityDescriptionDisplay);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            activityNameDisplay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isExpanded[getAdapterPosition()] = !isExpanded[getAdapterPosition()];
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }

}