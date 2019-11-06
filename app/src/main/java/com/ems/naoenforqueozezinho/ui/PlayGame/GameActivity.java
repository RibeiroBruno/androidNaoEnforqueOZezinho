package com.ems.naoenforqueozezinho.ui.PlayGame;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.TextUtilsCompat;
import androidx.fragment.app.Fragment;

import com.ems.naoenforqueozezinho.R;
import com.ems.naoenforqueozezinho.ui.DatabaseController;
import com.ems.naoenforqueozezinho.ui.Tema;
import com.ems.naoenforqueozezinho.ui.Word;
import com.ems.naoenforqueozezinho.ui.Words.ModalRemove;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Random;
import java.util.StringJoiner;

public class GameActivity extends AppCompatActivity {
    int currentWordIndex;
    TextView themeDescription, attempsDescription, currentWordAttemps, tipDescription;
    ImageView zezinhoImage;
    EditText inputAttemp;
    Button chatAttemp, wordAttemp;
    DatabaseController connection;
    Tema theme;
    Word currentWord;
    ArrayList<String> errorAttempsList = new ArrayList<>();
    ArrayList<Word> wordList = new ArrayList<>();
    ArrayList zezinhoGameOverImages = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);
        connection = new DatabaseController(getApplicationContext());
        zezinhoGameOverImages.add(R.drawable.morte_1);
        zezinhoGameOverImages.add(R.drawable.morte_2);
        zezinhoGameOverImages.add(R.drawable.morte_3);

        this.getParams();
        this.loadWords();
        this.setPointers();
        this.setFirstWord();
        this.setMessages();
        this.inputAttemp.addTextChangedListener(this.eventKeyChange());
        this.chatAttemp.setOnClickListener(this.eventCharButtonClick());
    }

    void initGame() {
        this.errorAttempsList.clear();
        this.wordList.remove(this.currentWordIndex);
        this.zezinhoImage.setImageResource(R.drawable.inicio);
        this.setFirstWord();
        this.setMessages();
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
                    if (errorAttempsList.indexOf(attemp) >= 0) {
                        Snackbar.make(view, "Letra " + attemp + "ja foi!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        return;
                    }
                    errorAttempsList.add(attemp);
                    updateAttempsDescription();
                    updateZezinhoError();
                    Snackbar.make(view, "Letra " + attemp + "não está na palavra!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                inputAttemp.getText().clear();
                inputAttemp.clearFocus();
            }
        };
    }

    private void showPlayAgainModal() {
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        ModalPlayAgain modalPlayAgain = new ModalPlayAgain();
                        modalPlayAgain.setTargetFragment(getSupportFragmentManager().getPrimaryNavigationFragment(), 1);
                        modalPlayAgain.show(getSupportFragmentManager(), "PlayAgainDialogFragment");
                    }
                },2500);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.dualPane);
        fragment.onActivityResult(requestCode, resultCode, data);
        Log.i("log", "teste" + resultCode);
        if (resultCode == 1) {
            initGame();
        }
    }

    private void updateAttempsDescription() {
        this.attempsDescription.setText("Letras erradas: - " + String.join(" - ", this.errorAttempsList));
    }

    private void updateZezinhoError() {
        switch (this.errorAttempsList.size()) {
            case 1: {
                this.zezinhoImage.setImageResource(R.drawable.erros_1);
                break;
            }
            case 2: {
                this.zezinhoImage.setImageResource(R.drawable.erros_2);
                break;
            }
            case 3: {
                this.zezinhoImage.setImageResource(R.drawable.erros_3);
                break;
            }
            case 4: {
                this.zezinhoImage.setImageResource(R.drawable.erros_4);
                break;
            }
            case 5: {
                this.zezinhoImage.setImageResource(R.drawable.erros_5);
                break;
            }
            case 6: {
                this.setDeadImage();
                this.showPlayAgainModal();
                break;
            }
        }
    }

    private void setDeadImage () {
        int index = this.getRamdom(this.zezinhoGameOverImages.size());
        this.zezinhoImage.setImageResource(this.zezinhoGameOverImages.get(index).hashCode());
    }

    private void setPointers () {
        this.themeDescription = findViewById(R.id.txtViewGameIntentThemeDescription);
        this.attempsDescription = findViewById(R.id.textViewAttempsDescription);
        this.currentWordAttemps = findViewById(R.id.textViewCurrentWord);
        this.tipDescription = findViewById(R.id.textViewCurrentWord);
        this.chatAttemp = findViewById(R.id.btn_attemp_char);
        this.wordAttemp = findViewById(R.id.btn_attemp_word);
        this.inputAttemp = findViewById(R.id.editTextInputAttemp);
        this.zezinhoImage = findViewById(R.id.imgViewErros);
        this.chatAttemp.setClickable(false);
        this.wordAttemp.setClickable(false);
    }

    private int getRamdom (int range) {
        Random r = new Random();
        return r.nextInt(range);
    }

    private void setFirstWord () {
        this.currentWordIndex = getRamdom(this.wordList.size());
        this.currentWord = this.wordList.get(this.currentWordIndex);
    }

    private void setMessages () {
        this.themeDescription.setText("Tema atual: " + this.theme.getTema());
        this.attempsDescription.setText("Letras erradas: - " + String.join(" - ", this.errorAttempsList));
        this.currentWordAttemps.setText("Teste: " + this.currentWord.getPalavra());
        this.tipDescription.setText("Dica: " + this.currentWord.getDica());
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
