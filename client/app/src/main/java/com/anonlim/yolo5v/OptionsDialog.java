package com.anonlim.yolo5v;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class OptionsDialog {
    private final Context mContext;
    private final Dialog mDialog;
    private OptionsAdapter mAdapter;
    private RecyclerView mListView;

    public OptionsDialog(Context context){
        mContext =context;
        mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCancelable(true);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setContentView(R.layout.options_dialog);
        WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
        params.width = context.getResources().getDimensionPixelSize(R.dimen.dialog_width);
        mDialog.getWindow().setAttributes(params);
    }



    public void setItems(List<String> list){
        mListView = mDialog.findViewById(R.id.options_recycler);
        mListView.setLayoutManager(new LinearLayoutManager(mContext));
        if (mAdapter == null){
            mAdapter = new OptionsAdapter(list);
        }
        mListView.setAdapter(mAdapter);
    }

    public void setCanceledListener(DialogInterface.OnCancelListener listener){
        mDialog.setOnCancelListener(listener);
    }
    public void show(){
        mDialog.show();
    }

    public OptionsAdapter getAdapter() {
        return mAdapter;
    }
}
