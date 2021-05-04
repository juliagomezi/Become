package com.pes.become.frontend;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.pes.become.R;
import com.pes.become.backend.adapters.DomainAdapter;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class Stats extends Fragment {

    private Context global;
    private View view;

    private final DomainAdapter DA = DomainAdapter.getInstance();
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    private LineChart mpLineChart;
    private ArrayList<Entry> sportValues, sleepValues, musicValues, cookingValues,
                            workValues, enterValues, plantsValues, otherValues;

    private final ArrayList<String> label= new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.stats, container, false);
        global = this.getActivity();

        label.add(getString(R.string.shortMonday));
        label.add(getString(R.string.shortTuesday));
        label.add(getString(R.string.shortWednesday));
        label.add(getString(R.string.shortThursday));
        label.add(getString(R.string.shortFriday));
        label.add(getString(R.string.shortSaturday));
        label.add(getString(R.string.shortSunday));

        initWidgets();
        selectedDate = LocalDate.now();
        setMonthView();

        mpLineChart = view.findViewById(R.id.linechart);
        ImageButton back = view.findViewById(R.id.previousMonthButton);
        back.setOnClickListener(v -> previousMonthAction());
        ImageButton next = view.findViewById(R.id.nextMonthButton);
        next.setOnClickListener(v -> nextMonthAction());

        TextView streakText = view.findViewById(R.id.streakText);
        TextPaint paint = streakText.getPaint();
        float width = paint.measureText(getString(R.string.streak));
        Shader textShader=new LinearGradient(0, 0, width, streakText.getTextSize(),
                new int[]{Color.parseColor("#12c2e9"),Color.parseColor("#c471ed"),Color.parseColor("#f64f59")},
                null, Shader.TileMode.CLAMP);
        streakText.getPaint().setShader(textShader);
        streakText.setTextColor(Color.parseColor("#12c2e9"));

        setHoursStats();
        setChart();

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

        ArrayList<Integer> hoursTheme = DA.getHoursByTheme();

        musicHour.setText(hoursTheme.get(0).toString() +"h");
        sportHour.setText(hoursTheme.get(1).toString() +"h");
        sleepHour.setText(hoursTheme.get(2).toString() +"h");
        cookingHour.setText(hoursTheme.get(3).toString() +"h");
        workingHour.setText(hoursTheme.get(4).toString() +"h");
        entertainmentHour.setText(hoursTheme.get(5).toString() +"h");
        plantsHour.setText(hoursTheme.get(6).toString() +"h");
        otherHour.setText(hoursTheme.get(7).toString() +"h");
    }

    private void setChart() {
        setDataValues();

        LineDataSet dataSport = new LineDataSet(sportValues,"Sport");
        dataSport.setColor(ContextCompat.getColor(getContext(), R.color.sport));
        dataSport.setCircleColor(ContextCompat.getColor(getContext(), R.color.sport));
        dataSport.setDrawCircleHole(false);
        dataSport.setLineWidth(2);

        LineDataSet dataSleep = new LineDataSet(sleepValues,"Sleep");
        dataSleep.setColor(ContextCompat.getColor(getContext(), R.color.sleep));
        dataSleep.setCircleColor(ContextCompat.getColor(getContext(), R.color.sleep));
        dataSleep.setDrawCircleHole(false);
        dataSleep.setLineWidth(2);

        LineDataSet dataMusic = new LineDataSet(musicValues,"Music");
        dataMusic.setColor(ContextCompat.getColor(getContext(), R.color.music));
        dataMusic.setCircleColor(ContextCompat.getColor(getContext(), R.color.music));
        dataMusic.setDrawCircleHole(false);
        dataMusic.setLineWidth(2);

        LineDataSet dataCooking = new LineDataSet(cookingValues,"Cooking");
        dataCooking.setColor(ContextCompat.getColor(getContext(), R.color.cooking));
        dataCooking.setCircleColor(ContextCompat.getColor(getContext(), R.color.cooking));
        dataCooking.setDrawCircleHole(false);
        dataCooking.setLineWidth(2);

        LineDataSet dataWorking = new LineDataSet(workValues,"Work");
        dataWorking.setColor(ContextCompat.getColor(getContext(), R.color.work));
        dataWorking.setCircleColor(ContextCompat.getColor(getContext(), R.color.work));
        dataWorking.setDrawCircleHole(false);
        dataWorking.setLineWidth(2);

        LineDataSet dataEnter = new LineDataSet(enterValues,"Entertainment");
        dataEnter.setColor(ContextCompat.getColor(getContext(), R.color.entertainment));
        dataEnter.setCircleColor(ContextCompat.getColor(getContext(), R.color.entertainment));
        dataEnter.setDrawCircleHole(false);
        dataEnter.setLineWidth(2);

        LineDataSet dataPlants = new LineDataSet(plantsValues,"Plants");
        dataPlants.setColor(ContextCompat.getColor(getContext(), R.color.plants));
        dataPlants.setCircleColor(ContextCompat.getColor(getContext(), R.color.plants));
        dataPlants.setDrawCircleHole(false);
        dataPlants.setLineWidth(2);

        LineDataSet dataOther = new LineDataSet(otherValues,"Other");
        dataOther.setColor(ContextCompat.getColor(getContext(), R.color.other));
        dataOther.setCircleColor(ContextCompat.getColor(getContext(), R.color.other));
        dataOther.setDrawCircleHole(false);
        dataOther.setLineWidth(2);

        Legend legend = mpLineChart.getLegend();
        legend.setEnabled(false);

        XAxis xAxis = mpLineChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                axis.setLabelCount(7,true);
                return label.get((int) value);

            }
        });

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

        Description desc = new Description();
        desc.setText("");
        mpLineChart.setDescription(desc);
        mpLineChart.setData(data);
        mpLineChart.invalidate();
    }

    private void setDataValues(){
        ArrayList<ArrayList<Integer>> allValues = DA.getStatisticsSelectedRoutine();
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