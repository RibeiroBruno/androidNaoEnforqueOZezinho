package com.ems.naoenforqueozezinho.ui.PlayGame;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.TextUtilsCompat;

import com.ems.naoenforqueozezinho.R;
import com.ems.naoenforqueozezinho.ui.DatabaseController;
import com.ems.naoenforqueozezinho.ui.Tema;
import com.ems.naoenforqueozezinho.ui.Word;

import java.util.ArrayList;
import java.util.Random;
import java.util.StringJoiner;

public class GameActivity extends AppCompatActivity {
    int currentWordIndex;
    Button btnAttemps;
    TextView themeDescription, attempsDescription, currentWordAttemps, tipDescription;
    EditText inputAttemp;
    Button chatAttemp, wordAttemp;
    DatabaseController connection;
    Tema theme;
    Word currentWord;
    String errorAttemps = new String();
    ArrayList<Word> wordList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);
        connection = new DatabaseController(getApplicationContext());

        this.getParams();
        this.loadWords();
        this.setPointers();
        this.setFirstWord();
        this.setMessages();
        this.inputAttemp.addTextChangedListener(this.eventKeyChange());
        this.chatAttemp.setOnClickListener(this.eventCharButtonClick());
    }

    private TextWatcher eventKeyChange () {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int currenntAttemp = inputAttemp.getText().toString().length();
                if (currenntAttemp == 0) {
                    chatAttemp.setClickable(false);
                    wordAttemp.setClickable(false);
                    return;
                }
                if (currenntAttemp > 1) {
                    chatAttemp.setClickable(false);
                    wordAttemp.setClickable(true);
                    return;
                }
                if (currenntAttemp == 1) {
                    chatAttemp.setClickable(true);
                    wordAttemp.setClickable(false);
                    return;
                }
                return;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    private View.OnClickListener eventCharButtonClick () {
        return new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String attemp = inputAttemp.getText().toString();
                if (!currentWord.getPalavra().contains(attemp)) {

//                    for (int index = 0; index < currentWord.getPalavra().length(); index ++) {
//
//                    }
                }
                inputAttemp.getText().clear();
                inputAttemp.clearFocus();
            }
        };
    }

    private void setPointers () {
        this.themeDescription = findViewById(R.id.txtViewGameIntentThemeDescription);
        this.attempsDescription = findViewById(R.id.textViewAttempsDescription);
        this.currentWordAttemps = findViewById(R.id.textViewCurrentWord);
        this.tipDescription = findViewById(R.id.textViewCurrentWord);
        this.chatAttemp = findViewById(R.id.btn_attemp_char);
        this.wordAttemp = findViewById(R.id.btn_attemp_word);
        this.inputAttemp = findViewById(R.id.editTextInputAttemp);
        this.chatAttemp.setClickable(false);
        this.wordAttemp.setClickable(false);
    }

    private int getRamdom () {
        Random r = new Random();
        return r.nextInt((this.wordList.size()));
    }

    private void setFirstWord () {
        this.currentWordIndex = getRamdom();
        this.currentWord = this.wordList.get(this.currentWordIndex);
    }

    private void setMessages () {
        this.themeDescription.setText("Tema atual: " + this.theme.getTema());
        this.attempsDescription.setText("Letras erradas: - " + String.join(" - ", errorAttemps);
        this.currentWordAttemps.setText("Teste: " + this.currentWord.getPalavra());
        this.tipDescription.setText("Dica: " + this.currentWord.getDica());
    }

    private updateerrorAttemps () {
        String errorAttemps = this.errorAttemps.join(" - ", this.errorAttemps);
    }

    private void getParams () {
        Intent it = getIntent();
        Bundle dados = it.getExtras();
        theme = (Tema) dados.getSerializable("theme");
    }

    private void loadWords () {
        this.wordList.addAll(this.getWords());
    }

    private ArrayList<Word> getWords () {
        ArrayList<Word> tempWordList;
        if (this.theme.getTema().equals("Aleatório")) {
            return this.connection.getWordsList();
        }
        return this.connection.getWordsByTheme(this.theme.getTemaId());

    }
}
