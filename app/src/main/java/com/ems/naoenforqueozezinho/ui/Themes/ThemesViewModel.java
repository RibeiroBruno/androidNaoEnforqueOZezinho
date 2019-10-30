package com.ems.naoenforqueozezinho.ui.Themes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ThemesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ThemesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Temas");
    }

    public LiveData<String> getText() {
        return mText;
    }
}