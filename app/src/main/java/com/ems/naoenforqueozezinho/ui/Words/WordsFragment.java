package com.ems.naoenforqueozezinho.ui.Words;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import com.ems.naoenforqueozezinho.ui.Word;

import java.util.ArrayList;

public class WordsFragment extends Fragment {

    private WordsViewModel createWordsViewModel;
    private ListView wordslistView;
    private ArrayList<Word> wordsList = new ArrayList<Word>();
    private ArrayList<Tema> themesList;
    private ArrayAdapter<Word> wordsListAdapter;
    private ArrayAdapter adapter;
    private DatabaseController connection;
    private Spinner spinner;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        createWordsViewModel =
                ViewModelProviders.of(this).get(WordsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_words, container, false);

        this.connection = new DatabaseController(WordsFragment.this.getContext());
        this.themesList = connection.getTemasList();
        this.spinner = root.findViewById(R.id.spinner);

        this.adapter = new ArrayAdapter(WordsFragment.this.getContext(),
                android.R.layout.simple_spinner_item, R.id.spinner, themesList);
        this.adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinner.setAdapter(adapter);

        final TextView textView = root.findViewById(R.id.txtViewTitleThemes);
        createWordsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}