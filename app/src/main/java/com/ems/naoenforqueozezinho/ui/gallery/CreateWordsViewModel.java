package com.ems.naoenforqueozezinho.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CreateWordsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CreateWordsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Aqui você cria as palavras");
    }

    public LiveData<String> getText() {
        return mText;
    }
}