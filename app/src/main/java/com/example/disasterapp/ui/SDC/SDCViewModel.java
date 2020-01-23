package com.example.disasterapp.ui.SDC;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SDCViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SDCViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is SDC fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}