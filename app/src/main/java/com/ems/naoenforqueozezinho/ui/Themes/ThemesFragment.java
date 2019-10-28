package com.ems.naoenforqueozezinho.ui.Themes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.InputDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textservice.TextInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.ems.naoenforqueozezinho.R;
import com.ems.naoenforqueozezinho.ui.DatabaseController;
import com.ems.naoenforqueozezinho.ui.Tema;
import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class ThemesFragment extends Fragment {

    private ThemesViewModel themesViewModel;
    DatabaseController connection;
    EditText themeInput;
    Button btnConfirm;
    ListView listViewThemes;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        themesViewModel =
                ViewModelProviders.of(this).get(ThemesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_themes, container, false);
        this.connection = new DatabaseController(root.getContext());
        this.themeInput = root.findViewById(R.id.inputTheme);
        this.btnConfirm = root.findViewById(R.id.btnCreateTheme);
        this.listViewThemes = root.findViewById(R.id.listViewThemes);

        btnConfirm.setOnClickListener(this.eventConfirmButton(this.connection));
        this.populateThemesList(this.connection);

        final TextView textView = root.findViewById(R.id.txtViewTitleThemes);
//        themesViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    private View.OnClickListener eventConfirmButton(final DatabaseController connection) {
        final String themeText = this.themeInput.getText().toString();
        final DatabaseController controller = connection;
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                Tema theme = new Tema(themeText);
                controller.createTema(theme);
                Snackbar.make(view, "Tema criado", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                populateThemesList(connection);
            }
        };
    }

    private void populateThemesList (DatabaseController connection) {
        ArrayList<String> themesList;
        themesList = connection.getTemasList();
        System.out.println(themesList.toString());
        ArrayAdapter adapter = new ArrayAdapter(
                getContext(),
                android.R.layout.simple_list_item_1,
                themesList);

        // Anexa o adapter à ListView
        this.listViewThemes.setAdapter(adapter);
    }
}