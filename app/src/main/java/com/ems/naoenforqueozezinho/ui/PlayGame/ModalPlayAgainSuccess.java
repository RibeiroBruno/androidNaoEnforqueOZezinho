package com.ems.naoenforqueozezinho.ui.PlayGame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.DialogFragment;

import com.ems.naoenforqueozezinho.R;

import java.util.ArrayList;
import java.util.Random;

public class ModalPlayAgainSuccess extends DialogFragment {

    View view;
    LayoutInflater inflater;
    Intent intent;
    String modalTitle;
    ArrayList zezinhoModalImages;
    ImageView imgView;
    public ModalPlayAgainSuccess() { }

    public InterfaceCommunicator interfaceCommunicator;

    public interface InterfaceCommunicator {
        void sendPlayAgainSuccessRequestCode(int code);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_play_again, null);
        modalTitle = getArguments().getString("modalTitle");
        this.zezinhoModalImages = new ArrayList<>();
        this.initImageOptions();
        this.imgView = view.findViewById(R.id.imgViewDialog);
        this.setImage();
        intent = new Intent();
        initImageOptions();

        builder
                .setView(view)
                .setIcon(R.drawable.zezinho_icon)
                .setTitle(modalTitle)
                .setPositiveButton("Jogar de novo!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        interfaceCommunicator = (InterfaceCommunicator) getActivity();
                        interfaceCommunicator.sendPlayAgainSuccessRequestCode(1);
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void initImageOptions () {
        this.zezinhoModalImages.add(R.drawable.zezinho_success_01);
        this.zezinhoModalImages.add(R.drawable.zezinho_success_02);
        this.zezinhoModalImages.add(R.drawable.zezinho_success_03);
        this.zezinhoModalImages.add(R.drawable.zezinho_success_04);
        this.zezinhoModalImages.add(R.drawable.zezinho_success_05);
    }

    private int getRamdom (int range) {
        Random r = new Random();
        return r.nextInt(range);
    }

    private void setImage () {
        int index = this.getRamdom(this.zezinhoModalImages.size());
        this.imgView.setImageResource(this.zezinhoModalImages.get(index).hashCode());
    }
}