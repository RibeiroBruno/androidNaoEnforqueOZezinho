package com.ems.naoenforqueozezinho.ui.Words;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ems.naoenforqueozezinho.R;
import com.ems.naoenforqueozezinho.ui.DatabaseController;
import com.ems.naoenforqueozezinho.ui.Tema;
import com.ems.naoenforqueozezinho.ui.Word;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class WordsFragment extends Fragment {

    View root;
    FragmentManager fragmentManager;
    private WordsViewModel createWordsViewModel;
    private ListView wordslistView;
    private ArrayList<Word> wordsList = new ArrayList<Word>();
    private ArrayList<Tema> themesList;
    private ArrayAdapter<Word> wordsListAdapter;
    private ArrayAdapter adapterThemes, adapterWords;
    private DatabaseController connection;
    private Spinner spinner;
    private EditText inputWord, inputTip;
    private Button btnConfirm;
    private Word editWord;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        createWordsViewModel =
                ViewModelProviders.of(this).get(WordsViewModel.class);
        this.root = inflater.inflate(R.layout.fragment_words, container, false);
        this.fragmentManager = getFragmentManager();

        this.connection = new DatabaseController(WordsFragment.this.getContext());
        findViews();
        initSpinner();
        initWordsList();

        btnConfirm.setOnClickListener(this.eventConfirmButton(this.connection));

        wordslistView.setOnItemClickListener(this.eventEditItem(this));
        wordslistView.setOnItemLongClickListener(this.eventRemoveItem(this));

        final TextView textView = root.findViewById(R.id.txtViewTitleWords);
        createWordsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    private void findViews() {
        this.inputWord = root.findViewById(R.id.inputWord);
        inputWord.setFocusable(true);
        this.inputTip = root.findViewById(R.id.inputTip);
        this.btnConfirm = root.findViewById(R.id.btnCreateWord);
        this.btnConfirm.setClickable(false);
        this.wordslistView = root.findViewById(R.id.listViewWords);
        this.spinner = root.findViewById(R.id.spinner_themes);
    }

    private void initSpinner () {
        this.themesList = connection.getTemasList();
        this.adapterThemes = new ArrayAdapter(this.getContext(), android.R.layout.simple_spinner_item, this.themesList);
        this.adapterThemes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinner.setAdapter(this.adapterThemes);
    }

    private void initWordsList () {
        this.wordsList = connection.getWordsList();
        this.adapterWords = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, this.wordsList);
        this.wordslistView.setAdapter(this.adapterWords);
    }

    private View.OnClickListener eventConfirmButton(final DatabaseController connection) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String wordText = inputWord.getText().toString().trim();
                String tipText = inputTip.getText().toString().trim();
                Tema theme = (Tema) spinner.getSelectedItem();
                if (wordText.length() == 0 || tipText.length() == 0) {
                    Snackbar.make(view, "Não são aceitas palavras vazias", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    clearText();
                    return;
                }
                Word word = new Word(wordText, tipText, theme);
                connection.createWord(word);
                connection.getWordsListLog();
                Snackbar.make(view, "Palavra criada - " + word.getPalavra(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                populateWordsList(connection);
                clearText();
            }
        };
    }

    private AdapterView.OnItemClickListener eventEditItem(final Fragment frag) {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Word wordItem = (Word) wordslistView.getItemAtPosition(position);
                ModalEdit modalEdit = new ModalEdit();
                modalEdit.setTargetFragment(frag, 1);
                modalEdit.setArguments(getWordBundle(wordItem));
                modalEdit.show(fragmentManager, "EditDialogFragment");
            }
        };
    }

    private AdapterView.OnItemLongClickListener eventRemoveItem(final Fragment frag) {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                Word wordItem = (Word) wordslistView.getItemAtPosition(position);
                ModalRemove modalRemove = new ModalRemove();
                modalRemove.setTargetFragment(frag, 1);
                modalRemove.setArguments(getWordBundle(wordItem));
                modalRemove.show(fragmentManager, "RemoveDialogFragment");
                return true;
            };
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            String wordText = data.getStringExtra("word").trim();
            String tipText = data.getStringExtra("tip").trim();
            if (wordText.length() == 0 || tipText.length() == 0) {
                Snackbar.make(this.root, "Campo vazio", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                clearText();
                return;
            }
            String wordId = data.getStringExtra("wordId");
            String themeId = data.getStringExtra("themeId");
            String themeText = data.getStringExtra("theme");
            Tema theme = new Tema(themeId, themeText);
            this.editWord = new Word(wordId, wordText, tipText, theme);
            this.connection.setWordItem(this.editWord);
            this.populateWordsList(connection);
            Snackbar.make(this.root, "Tema atualizado - " + this.editWord.getPalavra(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        if (resultCode == 2) {
            String wordId = data.getStringExtra("wordId");
            this.connection.removeWordItem(wordId);
            this.populateWordsList(connection);
            Snackbar.make(this.root, "Tema Removido", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    private Bundle getWordBundle (Word wordItem) {
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("word", wordItem);
        mBundle.putSerializable("themesList", this.themesList);
        return mBundle;
    }

    private void clearText() {
        this.inputWord.getText().clear();
        this.inputTip.getText().clear();
        this.closeKeyBoard();
    }

    private void closeKeyBoard() {
        ((InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                getActivity().getCurrentFocus().getWindowToken(), 0);
    }

    private void populateWordsList (DatabaseController connection) {
        this.wordsList.clear();
        ArrayList<Word> words = connection.getWordsList();
        this.wordsList.addAll(words);
        this.adapterWords.notifyDataSetChanged();
        Log.i("log", "Populated");
    }
};