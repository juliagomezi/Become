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

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    // llistat d'activitats
    private List<ActivityDummy> activitiesList;

    public RecyclerAdapter(List<ActivityDummy> activitiesList) {
        this.activitiesList = activitiesList;
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
        ActivityDummy activity = activitiesList.get(position);
        holder.startTimeDisplay.setText(activity.getStartTime());
        holder.endTimeDisplay.setText(activity.getEndTime());
        holder.activityNameDisplay.setText(activity.getName());
        holder.activityDescriptionDisplay.setText(activity.getDescription());

        boolean isExpanded = activity.getExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        String theme = activity.getTheme();
        if (theme.equals("Cooking")) holder.cardActivityDisplay.setBackgroundResource(R.drawable.theme_cooking_background);
        else if (theme.equals("Entertainment")) holder.cardActivityDisplay.setBackgroundResource(R.drawable.theme_entertainment_background);
        else if (theme.equals("Music")) holder.cardActivityDisplay.setBackgroundResource(R.drawable.theme_music_background);
        else if (theme.equals("Sleeping")) holder.cardActivityDisplay.setBackgroundResource(R.drawable.theme_sleeping_bacground);
        else if (theme.equals("Sport")) holder.cardActivityDisplay.setBackgroundResource(R.drawable.theme_sport_background);
        else if (theme.equals("Studying")) holder.cardActivityDisplay.setBackgroundResource(R.drawable.theme_studying_background);
        else if (theme.equals("Working")) holder.cardActivityDisplay.setBackgroundResource(R.drawable.theme_working_background);
        else holder.cardActivityDisplay.setBackgroundResource(R.drawable.theme_other_background);

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RoutineEdit.getInstance().createActivitySheet(true);
                RoutineEdit.getInstance().fillActivitySheet(activity.getName(), activity.getDescription(), activity.getTheme(), activity.getStartDay(), activity.getStartTime(), activity.getEndDay(), activity.getEndTime());
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
                    ActivityDummy activity = activitiesList.get(getAdapterPosition());
                    activity.setExpanded(!activity.getExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }

}