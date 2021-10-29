package com.example.elm.main_menu.ui.prediksi;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PrediksiViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PrediksiViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}