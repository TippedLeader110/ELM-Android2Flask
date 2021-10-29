package com.example.elm.main_menu.ui.panduan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PanduanViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PanduanViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}