package com.ems.naoenforqueozezinho.ui.Themes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.ems.naoenforqueozezinho.R;
import com.ems.naoenforqueozezinho.ui.Tema;

public class ModalEdit extends DialogFragment {

    EditText inputTheme;
    Tema theme;

    public ModalEdit () { }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_theme, null);
        this.inputTheme = (EditText) view.findViewById(R.id.dialog_edit_input);
        this.theme = (Tema) getArguments().getSerializable("theme");
        this.inputTheme.setText(this.theme.getTema());

        builder
                .setView(view)
                .setTitle(R.string.dialog_themeEdit)
                .setPositiveButton(R.string.dialog_themeEdit_true, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        theme.setTema(inputTheme.getText().toString());
                        Intent intent = new Intent();
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        this.inputTheme.setText(this.theme.getTema());
        inputTheme.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}