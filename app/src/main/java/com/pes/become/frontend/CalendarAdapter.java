package com.pes.become.frontend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pes.become.R;

import java.util.ArrayList;

class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>
{
    private final ArrayList<String> daysOfMonth;
    private final ArrayList<Integer> dayStats;

    public CalendarAdapter(ArrayList<String> daysOfMonth, ArrayList<Integer> dayStats)
    {
        this.daysOfMonth = daysOfMonth;
        this.dayStats = dayStats;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position)
    {
        String day = daysOfMonth.get(position);
        holder.dayOfMonth.setText(day);
        if(!day.equals("")) {
            int stat = dayStats.get(Integer.valueOf(day) - 1);
            if(stat != -1) {
                if (stat == 0)
                    holder.dayOfMonth.setBackgroundColor(MainActivity.getInstance().getResources().getColor(R.color.softred));
                else if (stat < 25)
                    holder.dayOfMonth.setBackgroundColor(MainActivity.getInstance().getResources().getColor(R.color.green25));
                else if (stat < 50)
                    holder.dayOfMonth.setBackgroundColor(MainActivity.getInstance().getResources().getColor(R.color.green50));
                else if (stat < 75)
                    holder.dayOfMonth.setBackgroundColor(MainActivity.getInstance().getResources().getColor(R.color.green75));
                else
                    holder.dayOfMonth.setBackgroundColor(MainActivity.getInstance().getResources().getColor(R.color.green100));
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return daysOfMonth.size();
    }

}
