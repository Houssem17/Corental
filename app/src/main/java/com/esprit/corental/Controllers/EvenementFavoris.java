package com.esprit.corental.Controllers;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esprit.corental.Entities.Evenement;
import com.esprit.corental.Adapters.EvenementFaAdapter;
import com.esprit.corental.R;
import com.esprit.corental.Utils.database.AppDataBase;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class EvenementFavoris extends Fragment {
    private AppDataBase database;
    private List<Evenement> event_list = new ArrayList<>();


    public EvenementFavoris() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_evenement_favoris, container, false);


        database = AppDataBase.getAppDatabase(getContext());
        event_list = database.produitDao().getAll();
        RecyclerView MatchRecyclerView = (RecyclerView) view.findViewById(R.id.publications);
        EvenementFaAdapter matchAdap = new EvenementFaAdapter(getContext(),event_list);
        MatchRecyclerView.setAdapter(matchAdap);
        MatchRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;
    }

}
