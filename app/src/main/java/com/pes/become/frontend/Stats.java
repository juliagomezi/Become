package com.pes.become.frontend;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.pes.become.R;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class Stats extends Fragment {

    private Context global;
    private View view;

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    private LineChart mpLineChart;
    private ArrayList<Entry> sportValues, sleepValues, musicValues, cookingValues,
                            workValues, enterValues, plantsValues, otherValues;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.stats, container, false);
        global = this.getActivity();

        initWidgets();
        selectedDate = LocalDate.now();
        setMonthView();

        mpLineChart = (LineChart) view.findViewById(R.id.linechart);
        ImageButton back = view.findViewById(R.id.previousMonthButton);
        back.setOnClickListener(v -> previousMonthAction());
        ImageButton next = view.findViewById(R.id.nextMonthButton);
        next.setOnClickListener(v -> nextMonthAction());

        setHoursStats();
        setValuesChart();

        return view;
    }

    private void initWidgets()
    {
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        calendarRecyclerView.setNestedScrollingEnabled(false);
        monthYearText = view.findViewById(R.id.monthYearTV);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView()
    {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        // TEMPORALMENT HARDCODEJAT INICI

        ArrayList<Integer> dayStats = new ArrayList<>();
        for(int i = 0; i < 31; ++i) {
            if(i == 0) dayStats.add(0);
            else if(i == 1) dayStats.add(1);
            else if(i == 2) dayStats.add(25);
            else if(i == 3) dayStats.add(50);
            else if(i == 4) dayStats.add(75);
            else if(i == 5) dayStats.add(-1);
            else {
                Random r = new Random();
                dayStats.add(r.nextInt(101));
            }
        }

        //TEMPORALMENT HARDCODEJAT FI

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, dayStats);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(global, 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<String> daysInMonthArray(LocalDate date)
    {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i = 2; i <= 43; i++)
        {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
            {
                daysInMonthArray.add("");
            }
            else
            {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return  daysInMonthArray;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String monthYearFromDate(LocalDate date)
    {
        String str = date.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault());
        String cap = str.substring(0, 1).toUpperCase() + str.substring(1);
        return cap + " " + date.getYear();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousMonthAction()
    {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextMonthAction()
    {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    private void setHoursStats() {
        TextView sportHour = view.findViewById(R.id.sportHour);
        TextView sleepHour = view.findViewById(R.id.sleepingHour);
        TextView musicHour = view.findViewById(R.id.musicHour);
        TextView cookingHour = view.findViewById(R.id.cookingHour);
        TextView workingHour = view.findViewById(R.id.workingHour);
        TextView entertainmentHour = view.findViewById(R.id.entertainmentHour);
        TextView plantsHour = view.findViewById(R.id.plantsHour);
        TextView otherHour = view.findViewById(R.id.otherHour);

        sportHour.setText("0" +"h");
        sleepHour.setText("0" +"h");
        musicHour.setText("0" +"h");
        cookingHour.setText("0" +"h");
        workingHour.setText("0" +"h");
        entertainmentHour.setText("0" +"h");
        plantsHour.setText("0" +"h");
        otherHour.setText("0" +"h");
    }

    private void setValuesChart(){
        setDataValues();

        LineDataSet dataSport = new LineDataSet(sportValues,"Sport");
        LineDataSet dataSleep = new LineDataSet(sleepValues,"Sleep");
        LineDataSet dataMusic = new LineDataSet(musicValues,"Music");
        LineDataSet dataCooking = new LineDataSet(cookingValues,"Cooking");
        LineDataSet dataWorking = new LineDataSet(workValues,"Work");
        LineDataSet dataEnter = new LineDataSet(enterValues,"Entertainment");
        LineDataSet dataPlants = new LineDataSet(plantsValues,"Plants");
        LineDataSet dataOther = new LineDataSet(otherValues,"Other");

        ArrayList<ILineDataSet> dataSet = new ArrayList<>();
        dataSet.add(dataSport);
        dataSet.add(dataSleep);
        dataSet.add(dataMusic);
        dataSet.add(dataCooking);
        dataSet.add(dataWorking);
        dataSet.add(dataEnter);
        dataSet.add(dataPlants);
        dataSet.add(dataOther);

        LineData data = new LineData(dataSet);
        mpLineChart.setData(data);
        mpLineChart.invalidate();
    }

    private void setDataValues(){
        //ArrayList<ArrayList<Integer>> allValues = getStatisticsSelectedRoutine();
        ArrayList<ArrayList<Integer>> allValues = new ArrayList<>();
        for (int tema=0; tema<8; ++tema) {
            ArrayList<Entry> array = new ArrayList<>();
            for (int dia=0; dia<7; ++dia) {
                array.add(new Entry(dia, allValues.get(tema).get(dia)));
            }
            switch (tema)
            {
                case 0:  musicValues = array;
                    break;
                case 1:  sportValues = array;
                    break;
                case 2:  sleepValues = array;
                    break;
                case 3:  cookingValues = array;
                    break;
                case 4:  workValues = array;
                    break;
                case 5:  enterValues = array;
                    break;
                case 6:  plantsValues = array;
                    break;
                default: otherValues = array;
                    break;
            }
        }
    }



}