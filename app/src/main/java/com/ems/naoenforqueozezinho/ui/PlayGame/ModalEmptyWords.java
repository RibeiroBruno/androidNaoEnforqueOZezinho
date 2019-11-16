package com.ems.naoenforqueozezinho.ui.PlayGame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.fragment.app.DialogFragment;

import com.ems.naoenforqueozezinho.R;

public class ModalEmptyWords extends DialogFragment {

    View view;
    LayoutInflater inflater;
    Intent intent;
    String modalTitle;
    public ModalEmptyWords() { }

    public InterfaceCommunicator interfaceCommunicator;

    public interface InterfaceCommunicator {
        void sendEmptyWordsRequestCode(int code);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_play_again, null);
        modalTitle = getArguments().getString("modalTitle");

        builder
                .setView(view)
                .setTitle(modalTitle)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        interfaceCommunicator = (InterfaceCommunicator) getActivity();
                        interfaceCommunicator.sendEmptyWordsRequestCode(1);
                        dialog.cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}