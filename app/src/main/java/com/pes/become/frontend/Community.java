package com.pes.become.frontend;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pes.become.R;
import com.pes.become.backend.adapters.DomainAdapter;

import java.util.ArrayList;

public class Community extends Fragment {

    private static Community instance;

    private View view;
    private Context global;

    private final DomainAdapter DA = DomainAdapter.getInstance();

    private ArrayList<ArrayList<Object>> communityRoutinesList;

    CommunityRecyclerAdapter communityRecyclerAdapter;
    RecyclerView recyclerView;

    EditText searchText;
    ImageButton searchButton;

    /**
     * Constructora per defecte de RoutinesList
     */
    public Community() {}

    /**
     * Funció obtenir la instancia de la RoutinesList actual
     * */
    public static Community getInstance() {
        if (instance == null) {
            instance = new Community();
        }
        return instance;
    }

    /**
     * Funcio del RoutinesList que s'executa al crear-la
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.community, container, false);
        super.onCreate(savedInstanceState);
        global = this.getActivity();
        instance = this;

        recyclerView = view.findViewById(R.id.communityRoutinesList);
        searchText = view.findViewById(R.id.searchText);
        searchButton = view.findViewById(R.id.searchButton);

        searchButton.setOnClickListener(view -> search());
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) { }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search();
            }
        });
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    search();
                    handled = true;
                }
                return handled;
            }
        });

        getSharedRoutines();

        return view;
    }

    /**
     * Funcio que es crida al cercar
     */
    private void search() {
        ArrayList<ArrayList<Object>> filteredCommunityRoutinesList = new ArrayList<>();
        String filter = searchText.getText().toString().toLowerCase();
        for (int i = 0; i < communityRoutinesList.size(); i++) {
            String routineName = String.valueOf(communityRoutinesList.get(i).get(1)).toLowerCase();
            if (routineName.contains(filter)) filteredCommunityRoutinesList.add(communityRoutinesList.get(i));
        }
        initRecyclerView(filteredCommunityRoutinesList);
    }

    /**
     * Funció per inicialitzar l'element que mostra el llistat d'activitats
     */
    private void initRecyclerView(ArrayList<ArrayList<Object>> list) {
        recyclerView.setLayoutManager((new LinearLayoutManager(global)));
        communityRecyclerAdapter = new CommunityRecyclerAdapter(list, global);
        recyclerView.setAdapter(communityRecyclerAdapter);
    }

    /**
     * Funció per obtenir les rutines de la communitat endreçada per valoració i punts
     */
    private void getSharedRoutines() {
        DA.getSharedRoutines();
    }

    /**
     * Funció de callback per obtenir les rutines de la communitat endreçada per valoració i punts
     * @param sharedRoutinesInfo ArrayList de informacio de les rutines compartides, representada com una ArrayList d'Objects que conte a cada posicio:
     *                           0 - String dmb la ID de la rutina compartida (ID autor + ID rutina)
     *                           1 - String amb el nom de la rutina
     *                           2 - Integer que indica si el currentuser pot votar la rutina (-1 propia; 0 pot votar; 1 ja ha votat)
     *                           3 - Puntacio mitjana de la rutina
     *                           4 - Nombre d'usuaris que han votat la rutina
     */
    public void getSharedRoutinesCallback(ArrayList<ArrayList<Object>> sharedRoutinesInfo) {
        communityRoutinesList = sharedRoutinesInfo;
        initRecyclerView(communityRoutinesList);
    }
}
