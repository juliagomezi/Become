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
import android.widget.ImageView;
import android.widget.TableLayout;
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

import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

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

    private TableLayout hours;
    private TextView hoursTitle, chartTitle;
    private View hoursSeparator, chartSeparator;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.stats, container, false);
        global = this.getActivity();

        hours = view.findViewById(R.id.hours);
        hoursTitle = view.findViewById(R.id.hoursTitle);
        chartTitle = view.findViewById(R.id.chartTitle);
        hoursSeparator = view.findViewById(R.id.hoursSeparator);
        chartSeparator = view.findViewById(R.id.chartSeparator);

        label.add(getString(R.string.shortMonday));
        label.add(getString(R.string.shortTuesday));
        label.add(getString(R.string.shortWednesday));
        label.add(getString(R.string.shortThursday));
        label.add(getString(R.string.shortFriday));
        label.add(getString(R.string.shortSaturday));
        label.add(getString(R.string.shortSunday));

        initWidgets();
        selectedDate = LocalDate.now();

        mpLineChart = view.findViewById(R.id.linechart);
        ImageButton back = view.findViewById(R.id.previousMonthButton);
        back.setOnClickListener(v -> previousMonthAction());
        ImageButton next = view.findViewById(R.id.nextMonthButton);
        next.setOnClickListener(v -> nextMonthAction());


        setStreak();

        setRoutineStats();

        setMonthView();

        return view;
    }

    /**
     * Metode que comprova les ratxes i les mostra o no
     */
    private void setStreak() {
        TextView streakText = view.findViewById(R.id.streakText);
        TextView congrats = view.findViewById(R.id.congrats);
        TextView streakNum = view.findViewById(R.id.streakNum);
        ImageView fireIcon = view.findViewById(R.id.fireIcon);
        View separator = view.findViewById(R.id.streakSeparator);

        int streak = DA.getUserStreak();
        if(streak > 1) {
            TextPaint paint = streakText.getPaint();
            float width = paint.measureText(getString(R.string.streak));
            Shader textShader=new LinearGradient(0, 0, width, streakText.getTextSize(),
                    new int[]{Color.parseColor("#12c2e9"),Color.parseColor("#c471ed"),Color.parseColor("#f64f59")},
                    null, Shader.TileMode.CLAMP);
            streakText.getPaint().setShader(textShader);
            streakText.setTextColor(Color.parseColor("#12c2e9"));

            streakNum.setText(String.valueOf(streak));

            streakText.setVisibility(View.VISIBLE);
            congrats.setVisibility(View.VISIBLE);
            streakNum.setVisibility(View.VISIBLE);
            fireIcon.setVisibility(View.VISIBLE);
            separator.setVisibility(View.VISIBLE);
        } else {
            streakText.setVisibility(View.GONE);
            congrats.setVisibility(View.GONE);
            streakNum.setVisibility(View.GONE);
            fireIcon.setVisibility(View.GONE);
            separator.setVisibility(View.GONE);
        }
    }

    /**
     * Metode que comprova si ha de mostrar estadistiques o no, en cas afirmatiu les carrega i mostra
     */
    public void setRoutineStats() {
        if(DA.getSelectedRoutineId().equals("")) noRoutineSelected();
        else showStats();
    }

    /**
     * Metode que amaga les estadistiques
     */
    private void noRoutineSelected() {
        hoursTitle.setVisibility(View.GONE);
        hours.setVisibility(View.GONE);
        chartTitle.setVisibility(View.GONE);
        mpLineChart.setVisibility(View.GONE);
        hoursSeparator.setVisibility(View.GONE);
        chartSeparator.setVisibility(View.GONE);
    }

    /**
     * Metode que carrega i mostra les estadistiques
     */
    private void showStats() {
        setHoursByTheme();
        setChart();

        hoursTitle.setVisibility(View.VISIBLE);
        hours.setVisibility(View.VISIBLE);
        chartTitle.setVisibility(View.VISIBLE);
        mpLineChart.setVisibility(View.VISIBLE);
        hoursSeparator.setVisibility(View.VISIBLE);
        chartSeparator.setVisibility(View.VISIBLE);
    }

    /**
     * Metode que carrega les hores per tema de la rutina seleccionada
     */
    private void setHoursByTheme() {
        ArrayList<Double> hoursTheme = DA.getHoursByTheme();

        TextView sportHour = view.findViewById(R.id.sportHour);
        TextView sleepHour = view.findViewById(R.id.sleepingHour);
        TextView musicHour = view.findViewById(R.id.musicHour);
        TextView cookingHour = view.findViewById(R.id.cookingHour);
        TextView workingHour = view.findViewById(R.id.workingHour);
        TextView entertainmentHour = view.findViewById(R.id.entertainmentHour);
        TextView plantsHour = view.findViewById(R.id.plantsHour);
        TextView otherHour = view.findViewById(R.id.otherHour);

        musicHour.setText(formatHoursMinutes(hoursTheme.get(0)));
        sportHour.setText(formatHoursMinutes(hoursTheme.get(1)));
        sleepHour.setText(formatHoursMinutes(hoursTheme.get(2)));
        cookingHour.setText(formatHoursMinutes(hoursTheme.get(3)));
        workingHour.setText(formatHoursMinutes(hoursTheme.get(4)));
        entertainmentHour.setText(formatHoursMinutes(hoursTheme.get(5)));
        plantsHour.setText(formatHoursMinutes(hoursTheme.get(6)));
        otherHour.setText(formatHoursMinutes(hoursTheme.get(7)));
    }

    /**
     * Metode que inicialitza els components del calendari
     */
    private void initWidgets()
    {
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        calendarRecyclerView.setNestedScrollingEnabled(false);
        monthYearText = view.findViewById(R.id.monthYearTV);
    }

    /**
     * Metode que carrega el mes que es mostra del calendari
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView()
    {
        monthYearText.setText(monthYearFromDate(selectedDate));
        DA.updateCalendar(selectedDate.getMonthValue(), selectedDate.getYear(), this);
    }

    /**
     * Metode que rep la resposta de carregar el mes seleccionat del calendari
     * @param dayStats estadistiques del mes
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void calendarCallback(ArrayList<Integer> dayStats){
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, dayStats);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(global, 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    /**
     * Metode que construeix el mes del calendari
     * @param date data seleccionada
     * @return dies del mes
     */
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

    /**
     * Metode per carregar el mes previ
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void previousMonthAction()
    {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    /**
     * Metode per carregar el mes seguent
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void nextMonthAction()
    {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    /**
     * Metode que construeix el grafic
     */
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

    /**
     * Metode que carrega les dades del grafic
     */
    private void setDataValues(){
        ArrayList<ArrayList<Double>> allValues = DA.getStatisticsSelectedRoutine();
        for (int tema=0; tema<8; ++tema) {
            ArrayList<Entry> array = new ArrayList<>();
            for (int dia=0; dia<7; ++dia) {
                array.add(new Entry(dia, (float)(double)allValues.get(tema).get(dia)));
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

    /**
     * Metode que formateja un double a hores i minuts
     * @param time double a formatejar
     * @return string amb format hh:mm h
     */
    private String formatHoursMinutes(double time) {
        char decimalSeparator = DecimalFormatSymbols.getInstance().getDecimalSeparator();
        String timeString = String.format("%.2f", (float)time);
        String[] hoursMinutes = timeString.split(Pattern.quote(String.valueOf(decimalSeparator)));
        double minutes = Integer.parseInt(hoursMinutes[1]);
        minutes /= 100;
        minutes *= 60;
        String minutesString = String.format("%.0f", (float)minutes);
        return hoursMinutes[0] + ":" + minutesString + "h";
    }

}