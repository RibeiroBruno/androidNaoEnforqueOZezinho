package com.ems.naoenforqueozezinho.ui.Words;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.content.Context;

import com.ems.naoenforqueozezinho.ui.DatabaseController;

public class WordsViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    ListView wordslistView;

    public WordsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Palavras");
    }

    public LiveData<String> getText() {
        return mText;
    }
}