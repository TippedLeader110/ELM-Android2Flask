package com.example.elm.main_menu.ui.tentang;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TentangViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TentangViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}