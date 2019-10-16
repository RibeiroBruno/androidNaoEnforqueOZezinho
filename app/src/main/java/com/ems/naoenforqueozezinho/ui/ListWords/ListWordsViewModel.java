package com.ems.naoenforqueozezinho.ui.ListWords;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListWordsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ListWordsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}