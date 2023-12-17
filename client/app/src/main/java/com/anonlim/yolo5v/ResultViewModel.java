package com.anonlim.yolo5v;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ResultViewModel extends ViewModel {



    private final MutableLiveData<Result> mResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mDetecting = new MutableLiveData<>();

    public LiveData<Result> getResult() {
        return mResult;
    }
    public LiveData<Boolean> getDetecting(){return mDetecting;}
    public void setData(Result result){
        this.mResult.setValue(result);
    }
    public void setDetectingState(boolean isDetecting){
        mDetecting.setValue(isDetecting);
    }
    public boolean isDetecting(){
        return Boolean.TRUE.equals(mDetecting.getValue());
    }

}
