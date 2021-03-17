package com.pes.become.frontend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pes.become.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<ActivityDummy> activitiesList;

    public RecyclerAdapter(List<ActivityDummy> activitiesList) {
        this.activitiesList = activitiesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.routine_activity_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActivityDummy activity = activitiesList.get(position);
        holder.startTimeDisplay.setText(activity.getStartTime());
        //holder.endTimeDisplay.setText(activity.getEndTime());
        holder.activityNameDisplay.setText(activity.getName());
        holder.activityDescriptionDisplay.setText(activity.getDescription());
    }

    @Override
    public int getItemCount() {
        return activitiesList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView startTimeDisplay, endTimeDisplay, activityNameDisplay, activityDescriptionDisplay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            startTimeDisplay = itemView.findViewById(R.id.startTimeDisplay);
            //endTimeDisplay = itemView.findViewById(R.id.endTimeDisplay);
            activityNameDisplay = itemView.findViewById(R.id.activityNameDisplay);
            activityDescriptionDisplay = itemView.findViewById(R.id.activityDescriptionDisplay);
        }
    }
}
