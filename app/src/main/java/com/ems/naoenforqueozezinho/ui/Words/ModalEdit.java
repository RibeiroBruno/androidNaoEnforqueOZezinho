package com.ems.naoenforqueozezinho.ui.Words;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ems.naoenforqueozezinho.R;
import com.ems.naoenforqueozezinho.ui.Tema;
import com.ems.naoenforqueozezinho.ui.Word;

import java.util.ArrayList;

public class ModalEdit extends DialogFragment {
    View view;
    EditText inputWord, inputTip;
    Word word;
    ArrayList<Tema> themesList;
    private ArrayAdapter adapterThemes;
    Spinner spinner;

    public ModalEdit() { }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_edit_word, null);
        initViews();
        initSpinner();

        builder
                .setView(view)
                .setTitle(R.string.dialog_themeEdit)
                .setPositiveButton(R.string.dialog_themeEdit_true, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        word.setPalavra(inputWord.getText().toString());
                        word.setDica(inputTip.getText().toString());
                        Tema theme = (Tema) spinner.getSelectedItem();
                        Intent intent = new Intent();
                        intent.putExtra("wordId", word.getIdPalavra());
                        intent.putExtra("word", word.getPalavra());
                        intent.putExtra("tip", word.getDica());
                        intent.putExtra("themeId", theme.getTemaId());
                        intent.putExtra("theme", theme.getTema());
                        getTargetFragment().onActivityResult(getTargetRequestCode(), 1, intent);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.dialog_themeEdit_false, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void initViews () {
        this.inputWord = (EditText) view.findViewById(R.id.dialog_edit_input_word);
        this.inputTip = (EditText) view.findViewById(R.id.dialog_edit_input_tip);
        this.spinner = (Spinner) view.findViewById(R.id.dialog_spinner);
        this.word = (Word) getArguments().getSerializable("word");
        this.themesList = (ArrayList<Tema>) getArguments().getSerializable("themesList");
        this.inputWord.setText(this.word.getPalavra());
        this.inputTip.setText(this.word.getDica());
    }

    private void initSpinner () {
        this.adapterThemes = new ArrayAdapter(this.getContext(), android.R.layout.simple_spinner_item, this.themesList);
        this.adapterThemes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinner.setAdapter(this.adapterThemes);
    }
}