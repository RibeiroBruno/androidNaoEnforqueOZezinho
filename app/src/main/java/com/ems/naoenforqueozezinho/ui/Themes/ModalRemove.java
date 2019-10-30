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

public class ModalRemove extends DialogFragment {

    EditText inputTheme;
    Tema theme;

    public ModalRemove () { }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_remove_theme, null);
        this.theme = (Tema) getArguments().getSerializable("theme");

        builder
                .setView(view)
                .setTitle(R.string.dialog_theme_remove_view)
                .setPositiveButton(R.string.dialog_theme_remove_true, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent();
                        intent.putExtra("themeId", theme.getTemaId());
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