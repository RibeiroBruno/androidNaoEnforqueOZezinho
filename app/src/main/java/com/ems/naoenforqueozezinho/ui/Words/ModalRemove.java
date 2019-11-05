package com.ems.naoenforqueozezinho.ui.Words;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.ems.naoenforqueozezinho.R;
import com.ems.naoenforqueozezinho.ui.Tema;
import com.ems.naoenforqueozezinho.ui.Word;

public class ModalRemove extends DialogFragment {

    EditText inputTheme;
    Word word;

    public ModalRemove() { }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_remove_word, null);
        this.word = (Word) getArguments().getSerializable("word");

        builder
                .setView(view)
                .setTitle(R.string.dialog_theme_remove_view)
                .setPositiveButton(R.string.dialog_theme_remove_true, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent();
                        intent.putExtra("wordId", word.getIdPalavra());
                        getTargetFragment().onActivityResult(getTargetRequestCode(), 2, intent);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.dialog_theme_remove_false, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}