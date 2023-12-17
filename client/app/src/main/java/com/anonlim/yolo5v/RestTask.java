package com.anonlim.yolo5v;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

public class RestTask extends AsyncTask<Void, Void, Result> {

    private DataListener mListener;
    private String mUrl;

    public RestTask(DataListener mListener, String mUrl) {
        this.mListener = mListener;
        this.mUrl = mUrl;
    }

    public interface DataListener{
        void onReceive(Result result);
    }

    @Override
    protected Result doInBackground(Void... voids) {
        return YOLO.getResult(mUrl);
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        if (result != null) {
            mListener.onReceive(result);
        }
    }
}
