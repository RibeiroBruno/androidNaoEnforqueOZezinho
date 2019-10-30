package com.ems.naoenforqueozezinho.ui.Words;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.material.snackbar.Snackbar;

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
    private EditText inputWord;
    private Button btnConfirm;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        createWordsViewModel =
                ViewModelProviders.of(this).get(WordsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_words, container, false);

        this.connection = new DatabaseController(WordsFragment.this.getContext());
        this.themesList = connection.getTemasList();
        this.inputWord = root.findViewById(R.id.inputWord);
        this.btnConfirm = root.findViewById(R.id.btnCreateWord);

        this.spinner = root.findViewById(R.id.spinner_themes);

        this.adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_spinner_item, themesList);
        this.adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinner.setAdapter(adapter);

        btnConfirm.setOnClickListener(this.eventConfirmButton(this.connection));

        final TextView textView = root.findViewById(R.id.txtViewTitleWords);
        createWordsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    private View.OnClickListener eventConfirmButton(final DatabaseController connection) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String themeText = inputWord.getText().toString().trim();
                if (themeText.length() == 0) {
                    Snackbar.make(view, "Não são aceitas palavras vazias", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    clearText();
                    return;
                }
                Tema theme = new Tema(themeText);
                connection.createTema(theme);
                Snackbar.make(view, "Tema criado - " + theme.getTema(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                populateThemesList(connection);
                clearText();
            }
        };
    }

    private void clearText() {
        this.inputWord.getText().clear();
        // fecha o teclado virtual
        this.closeKeyBoard();
    }

    private void closeKeyBoard() {
        ((InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                getActivity().getCurrentFocus().getWindowToken(), 0);
    }
};