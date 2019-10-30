package com.ems.naoenforqueozezinho.ui.Themes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ems.naoenforqueozezinho.R;
import com.ems.naoenforqueozezinho.ui.DatabaseController;
import com.ems.naoenforqueozezinho.ui.Tema;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ThemesFragment extends Fragment {

    private ThemesViewModel themesViewModel;
    View root;
    DatabaseController connection;
    EditText inputTheme;
    Button btnConfirm;
    ListView listViewThemes;
    ArrayAdapter adapter;
    ArrayList<Tema> themesList;
    Tema editTheme;
    FragmentManager fragmentManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        themesViewModel =
                ViewModelProviders.of(this).get(ThemesViewModel.class);
        this.root = inflater.inflate(R.layout.fragment_themes, container, false);
        this.fragmentManager = getFragmentManager();
        this.connection = new DatabaseController(root.getContext());
        this.inputTheme = root.findViewById(R.id.inputWord);
        this.btnConfirm = root.findViewById(R.id.btnCreateWord);
        this.listViewThemes = root.findViewById(R.id.listViewThemes);

        this.themesList = connection.getTemasList();
        this.adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, this.themesList);
        this.listViewThemes.setAdapter(adapter);

        btnConfirm.setOnClickListener(this.eventConfirmButton(this.connection));
        listViewThemes.setOnItemClickListener(this.eventEditItem(this));
        listViewThemes.setOnItemLongClickListener(this.eventRemoveItem(this));

        final TextView textView = root.findViewById(R.id.txtViewTitleWords);
        themesViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    private View.OnClickListener eventConfirmButton(final DatabaseController connection) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String themeText = inputTheme.getText().toString().trim();
                if (themeText.length() == 0) {
                    Snackbar.make(view, "Não são aceitas palavras vazias", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    clearText();
                    return;
                }
                Tema theme = new Tema(themeText);
                connection.createTema(theme);
                Snackbar.make(view, "Tema criado - " + theme.getTema(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                populateThemesList(connection);
                clearText();
            }
        };
    }

    private AdapterView.OnItemClickListener eventEditItem(final Fragment frag) {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Tema themeItem = (Tema) listViewThemes.getItemAtPosition(position);
                ModalEdit modalEdit = new ModalEdit();
                modalEdit.setTargetFragment(frag, 1);
                modalEdit.setArguments(getThemeBundle(themeItem));
                modalEdit.show(fragmentManager, "EditDialogFragment");
            };
        };
    }

    private AdapterView.OnItemLongClickListener eventRemoveItem(final Fragment frag) {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                Tema themeItem = (Tema) listViewThemes.getItemAtPosition(position);
                ModalRemove modalRemove = new ModalRemove();
                modalRemove.setTargetFragment(frag, 1);
                modalRemove.setArguments(getThemeBundle(themeItem));
                modalRemove.show(fragmentManager, "RemoveDialogFragment");
                return true;
            };
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            String theme = data.getStringExtra("theme").trim();
            if (theme.length() == 0) {
                Snackbar.make(this.root, "Campo vazio", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                clearText();
                return;
            }
            String themeId = data.getStringExtra("themeId");
            this.editTheme = new Tema(themeId, theme);
            this.connection.setThemeItem(this.editTheme);
            this.populateThemesList(connection);
            Snackbar.make(this.root, "Tema criado - " + editTheme.getTema(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        if (resultCode == 2) {
            String themeId = data.getStringExtra("themeId");
            this.connection.removeThemeItem(themeId);
            this.populateThemesList(connection);
            Snackbar.make(this.root, "Tema Removido", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    private Bundle getThemeBundle (Tema themeItem) {
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("theme", themeItem);
        return mBundle;
    }

    private void populateThemesList (DatabaseController connection) {
        this.themesList.clear();
        ArrayList<Tema> list = connection.getTemasList();
        this.themesList.addAll(list);
        this.adapter.notifyDataSetChanged();
        Log.i("log", "Populated");
    }

    private void clearText() {
        this.inputTheme.getText().clear();
        // fecha o teclado virtual
        this.closeKeyBoard();
    }

    private void closeKeyBoard() {
        ((InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                getActivity().getCurrentFocus().getWindowToken(), 0);
    }
}