package com.ems.naoenforqueozezinho.ui.PlayGame;

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
import com.ems.naoenforqueozezinho.ui.Word;

public class ModalPlayAgain extends DialogFragment {

    View view;
    LayoutInflater inflater;

    public ModalPlayAgain() { }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_play_again, null);

        builder
                .setView(view)
                .setTitle(R.string.dialog_play_again_title)
                .setPositiveButton(R.string.dialog_theme_remove_true, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent();
                        onActivityResult(getTargetRequestCode(), 1, intent);
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