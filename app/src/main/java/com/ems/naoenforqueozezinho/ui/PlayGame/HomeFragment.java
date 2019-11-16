package com.ems.naoenforqueozezinho.ui.PlayGame;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ems.naoenforqueozezinho.R;
import com.ems.naoenforqueozezinho.ui.DatabaseController;
import com.ems.naoenforqueozezinho.ui.Tema;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    View root;
    private HomeViewModel homeViewModel;
    private DatabaseController connection;
    private Button btnPlayGame;
    private Spinner spinnerThemes;
    private ArrayList<Tema> themesList;
    private ArrayAdapter<Tema> adapterThemes;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView textView = root.findViewById(R.id.text_home);
        spinnerThemes = root.findViewById(R.id.spinner_themes);
        btnPlayGame = root.findViewById(R.id.btn_play_game);
        connection = new DatabaseController(root.getContext());

        btnPlayGame.setOnClickListener(this.eventNewGame());
        initSpinner();

        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    private View.OnClickListener eventNewGame () {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tema theme = (Tema) spinnerThemes.getSelectedItem();
                Fragment frag = new Fragment();
                Intent it = new Intent(root.getContext(), GameActivity.class);
                Bundle dados = new Bundle();
                dados.putSerializable("theme", theme);
                it.putExtras(dados);
                startActivity(it);
            };
        };
    }

    private void initSpinner () {
        this.themesList = connection.getValidTemasList();
        Tema defaultTheme = new Tema("Aleatório");
        this.themesList.add(0, defaultTheme);
        this.adapterThemes = new ArrayAdapter(root.getContext(), android.R.layout.simple_spinner_item, this.themesList);
        this.adapterThemes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerThemes.setAdapter(this.adapterThemes);
    }

}