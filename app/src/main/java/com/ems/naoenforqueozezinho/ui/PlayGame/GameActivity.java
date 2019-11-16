package com.ems.naoenforqueozezinho.ui.PlayGame;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ems.naoenforqueozezinho.R;
import com.ems.naoenforqueozezinho.ui.DatabaseController;
import com.ems.naoenforqueozezinho.ui.Tema;
import com.ems.naoenforqueozezinho.ui.Word;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity
        extends AppCompatActivity
        implements ModalPlayAgainFail.InterfaceCommunicator,
        ModalPlayAgainSuccess.InterfaceCommunicator,
        ModalEmptyWords.InterfaceCommunicator
{
    int currentWordIndex, currentHits, currentWordLength;
    TextView themeDescription, attempsDescription, currentWordAttemps, tipDescription;
    String currentDashedWord;
    ImageView zezinhoImage;
    EditText inputAttemp;
    Button chatAttemp, wordAttemp;
    DatabaseController connection;
    Tema theme;
    Word currentWord;
    ArrayList<String> errorAttempsList, attempsList;
    ArrayList<Word> wordList;
    ArrayList zezinhoGameOverImages, zezinhoGameOverMessages, zezinhoSuccessMessages, zezinhoSuccessModalImages;
    ModalPlayAgainFail modalPlayAgainFail;
    ModalPlayAgainSuccess modalPlayAgainSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);

        this.initActivityState();

        this.getParams();
        this.loadWords();
        this.setPointers();
        this.setCurrentWord();
        this.initDashedWord();
        this.setMessages();
        this.inputAttemp.addTextChangedListener(this.eventKeyChange());
        this.chatAttemp.setOnClickListener(this.eventCharButtonClick());
        this.wordAttemp.setOnClickListener(this.eventWordButtonClick());
    }

    @Override public void sendPlayAgainFailRequestCode (int code) { if (code == 1) this.initGame(); }

    @Override public void sendPlayAgainSuccessRequestCode (int code) { if (code == 1) this.initGame(); }

    @Override public void sendEmptyWordsRequestCode (int code) {
        if (code == 1) finish();
    }

    void initActivityState () {
        this.connection = new DatabaseController(getApplicationContext());
        this.modalPlayAgainFail = new ModalPlayAgainFail();
        this.modalPlayAgainSuccess = new ModalPlayAgainSuccess();
        this.zezinhoGameOverImages = new ArrayList();
        this.wordList = new ArrayList<>();
        this.errorAttempsList = new ArrayList<>();
        this.attempsList = new ArrayList<>();
        this.zezinhoGameOverMessages = new ArrayList<String>();
        this.zezinhoSuccessMessages = new ArrayList<String>();
        this.zezinhoSuccessModalImages = new ArrayList<>();
        this.currentDashedWord = new String();
        this.currentHits = 0;

        this.zezinhoGameOverImages.add(R.drawable.morte_1);
        this.zezinhoGameOverImages.add(R.drawable.morte_2);
        this.zezinhoGameOverImages.add(R.drawable.morte_3);
        this.zezinhoGameOverMessages.add("Errrrrooooouuu");
        this.zezinhoGameOverMessages.add("Deu ruim...");
        this.zezinhoGameOverMessages.add("Como você é burro cara...");
        this.zezinhoSuccessMessages.add("Mizeravi, acertô");
        this.zezinhoSuccessMessages.add("Deu bom!");
        this.zezinhoSuccessMessages.add("´Você é um gênio");
    }

    private void getParams () {
        Intent it = getIntent();
        Bundle dados = it.getExtras();
        theme = (Tema) dados.getSerializable("theme");
    }

    private ArrayList<Word> getWords () {
        ArrayList<Word> tempWordList;
        if (this.theme.getTema().equals("Aleatório")) {
            return this.connection.getWordsList();
        }
        return this.connection.getWordsByTheme(this.theme.getTemaId());

    }

    private void loadWords () { this.wordList.addAll(this.getWords()); }

    private int getRamdom (int range) {
        Random r = new Random();
        return r.nextInt(range);
    }

    private void setCurrentWord () {
        int currentWordsListSize = this.wordList.size();
        if (currentWordsListSize > 0) {
            this.currentWordIndex = getRamdom(this.wordList.size());
            this.currentWord = this.wordList.get(this.currentWordIndex);
            return;
        }
        this.showEmptyWordsModal();
    }

    private void initDashedWord() {
        this.currentDashedWord = this.currentWord.getPalavra().replaceAll("[a-zA-Z]", "_");
        this.currentHits = 0;
        this.currentWordLength = this.currentDashedWord.replaceAll("[^_]", "").length();
    }

    private void setMessages () {
        this.themeDescription.setText("Tema atual: " + this.theme.getTema());
        this.attempsDescription.setText("Letras erradas: - " + String.join(" - ", this.errorAttempsList));
        this.currentWordAttemps.setText(this.currentDashedWord);
        this.tipDescription.setText("Dica: " + this.currentWord.getDica());
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
            public void afterTextChanged(Editable editable) { }
        };
    }

    private View.OnClickListener eventCharButtonClick () {
        return new View.OnClickListener() {
            @Override public void onClick(View view) {
                String attemp = inputAttemp.getText().toString();
                if (attempsList.indexOf(attemp) >= 0) {
                    Snackbar.make(view, "Letra " + attemp + " já foi!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                attempsList.add(attemp);
                if (!currentWord.getPalavra().toLowerCase().contains(attemp.toLowerCase())) {
                    this.errorAttemp(view, attemp);
                    return;
                }
                this.hitAttemp(attemp);
                if (currentHits == currentWordLength) endGame();
            }

            private void errorAttemp (View view, String attemp) {
                if (errorAttempsList.indexOf(attemp) >= 0) {
                    Snackbar.make(view, "Letra " + attemp + " já foi!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                errorAttempsList.add(attemp);
                updateErrorAttempsDescription();
                updateZezinhoError();
                Snackbar.make(view, "Letra " + attemp + " não existe na palavra!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                initKeyboard();
            }

            private void hitAttemp (String attemp) {
                updateAttempsDescription(attemp);
                initKeyboard();
            }
        };
    }

    private View.OnClickListener eventWordButtonClick () {
        return new View.OnClickListener() {
            @Override public void onClick(View view) {
                String attemp = inputAttemp.getText().toString().toLowerCase();
                if (currentWord.getPalavra().toLowerCase().compareTo(attemp) == 0) {
                    endGame();
                    initKeyboard();
                    return;
                }
                setDeadImage();
                showPlayAgainModal();
                initKeyboard();
            }
        };
    }

    private void initKeyboard () {
        inputAttemp.getText().clear();
        inputAttemp.clearFocus();
    }

    private void initGame() {
        this.errorAttempsList.clear();
        this.attempsList.clear();
        this.wordList.remove(this.currentWordIndex);
        this.zezinhoImage.setImageResource(R.drawable.inicio);
        this.setCurrentWord();
        this.initDashedWord();
        this.setMessages();
        this.inputAttemp.setActivated(true);
    }

    private void endGame () {
        this.inputAttemp.setActivated(false);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Bundle bundle = new Bundle();
                        bundle.putString("modalTitle", getSuccessMessage());
                        modalPlayAgainSuccess.setArguments(bundle);
                        modalPlayAgainSuccess.show(getSupportFragmentManager(), "PlayAgainSuccessDialogFragment");
                    }
                },1000);
    }

    private void showPlayAgainModal() {
        this.inputAttemp.setActivated(false);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Bundle bundle = new Bundle();
                        bundle.putString("modalTitle", getDeadMessage());
                        modalPlayAgainFail.setArguments(bundle);
                        modalPlayAgainFail.show(getSupportFragmentManager(), "PlayAgainFailDialogFragment");
                    }
                },2500);
    }

    private void updateErrorAttempsDescription() {
        this.attempsDescription.setText("Letras erradas: - " + String.join(" - ", this.errorAttempsList));
    }

    private void updateAttempsDescription (String attemp) {
        String temporaryString = this.currentWord.getPalavra().toLowerCase();
        StringBuilder temporaryCurrentDashedWord = new StringBuilder(this.currentDashedWord);
        String attempToLower = attemp.toLowerCase();
        do {
            int index = temporaryString.indexOf(attempToLower);
            temporaryString = temporaryString.replaceFirst("["+ attemp + "]", "_");
            temporaryCurrentDashedWord.setCharAt(index, attemp.charAt(0));
            this.currentHits++;
        } while (temporaryString.indexOf(attemp) >= 0);
        this.currentDashedWord = temporaryCurrentDashedWord.toString();
        this.currentWordAttemps.setText(this.currentDashedWord);
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

    private String getDeadMessage () {
        int index = this.getRamdom(this.zezinhoGameOverImages.size());
        return this.zezinhoGameOverMessages.get(index).toString();
    }

    private String getSuccessMessage () {
        int index = this.getRamdom(this.zezinhoSuccessMessages.size());
        return this.zezinhoSuccessMessages.get(index).toString();
    }

    private void setPointers () {
        this.themeDescription = findViewById(R.id.txtViewGameIntentThemeDescription);
        this.attempsDescription = findViewById(R.id.textViewAttempsDescription);
        this.currentWordAttemps = findViewById(R.id.textViewCurrentWord);
        this.tipDescription = findViewById(R.id.textViewTipDescription);
        this.chatAttemp = findViewById(R.id.btn_attemp_char);
        this.wordAttemp = findViewById(R.id.btn_attemp_word);
        this.inputAttemp = findViewById(R.id.editTextInputAttemp);
        this.zezinhoImage = findViewById(R.id.imgViewErros);
        this.chatAttemp.setClickable(false);
        this.wordAttemp.setClickable(false);
    }

    private void showEmptyWordsModal () {
        ModalEmptyWords modalEmptyWords = new ModalEmptyWords();
        Bundle bundle = new Bundle();
        bundle.putString("modalTitle", "Não á mais palavras para o tema especificado");
        modalEmptyWords.setArguments(bundle);
        modalEmptyWords.show(getSupportFragmentManager(), "EmptyWordsDialogFragment");
    }
}
