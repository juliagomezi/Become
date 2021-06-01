package com.pes.become.frontend;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.ArrayUtils;
import com.pes.become.R;
import com.pes.become.backend.adapters.DomainAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class PopUpClass {

    private static PopUpClass instance;

    private View view;
    private Context global;
    private final DomainAdapter DA = DomainAdapter.getInstance();

    RecommendationsRecyclerAdapter recommendationsRecyclerAdapter;
    RecyclerView recyclerView;
    private TextView emptyView;

    ArrayList<Integer> recommendationsList;

    public PopUpClass(ArrayList<Integer> recommendationsList, Context global) {
        this.recommendationsList = recommendationsList;
        this.global = global;
        instance = this;
    }

    public PopUpClass getInstance() {
        return instance;
    }

    //PopupWindow display method

    public void showPopupWindow(final View view) {

        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.recommendations_pop_up, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.TOP|Gravity.END, 0, 0);

        //Initialize the elements of our window, install the handler
        emptyView = popupView.findViewById(R.id.emptyView);
        recyclerView = popupView.findViewById(R.id.recommendationsList);
        initView();

        //Handler for clicking on the inactive zone of the window
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Close the window when clicked
                popupWindow.dismiss();
                return true;
            }
        });
    }

    private void initView() {
        int count = 0;
        for (int i = 0; i < this.recommendationsList.size(); ++i) {
            if (this.recommendationsList.get(i) == 0) count++;
        }
        if (count == this.recommendationsList.size()) initEmptyView();
        else initRecyclerView();
    }

    /**
     * Funció per inicialitzar l'element que mostra el llistat d'activitats
     */
    private void initRecyclerView() {
        recyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);

        recyclerView.setLayoutManager((new LinearLayoutManager(global)));
        recommendationsRecyclerAdapter = new RecommendationsRecyclerAdapter(recommendationsList, global);
        recyclerView.setAdapter(recommendationsRecyclerAdapter);
    }

    /**
     * Funció per inicialitzar l'element que es mostra quan no hi ha recomanacions
     */
    public void initEmptyView() {
        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);

        emptyView.setText(R.string.noRecommendations);
    }

}