package com.anonlim.yolo5v;

import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.anonlim.yolo5v.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private ActivityMainBinding binding;
    private ResultViewModel mViewModel;
    private final String mLocal = "http://10.0.2.2:8000/api_root/Post/";
    private List<String> mList = new ArrayList<>();
    private OptionsDialog mDialog;
    private Thread backgroundThread;
    private RestTask mTask;
    private TextToSpeech mTTS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mTTS = new TextToSpeech(this, this);
        Toolbar toolbar = binding.appBar;
        mList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.arrays)));
        setSupportActionBar(toolbar);
        mViewModel = new ViewModelProvider(this).get(ResultViewModel.class);
        binding.setViewmodel(mViewModel);
        binding.optionsButton.setOnClickListener(v -> showOptions());
        binding.sourceButton.setOnClickListener(v -> {
            if (mViewModel.isDetecting()){
                stopTask();
            }
            else {
                dataHandler.post(getDataRunnable);
            }
        });
    }


    private final List<RestTask> taskList = new ArrayList<>();

    private void getData(){
        mViewModel.setDetectingState(true);
        binding.invalidateAll();
        mTask = new RestTask(this::setData, mLocal);
        taskList.add(mTask);
        mTask.execute();
    }


    private void setData(Result result){
        List<String> filter = getFilter();
        List<String> filteredObjects = result.getObjects(filter);
        StringBuilder filtered = new StringBuilder();
        for (String s:filteredObjects
             ) {
            filtered.append(s);
        }
        result.setText(filtered.toString());
        runOnUiThread(() -> {
            mViewModel.setData(result);
            binding.invalidateAll();
            if (!filteredObjects.isEmpty()){
                stopTask();
            }
        });
    }

    private List<String> getFilter(){
        List<String> list= new ArrayList<>();
        if (mDialog != null){
            OptionsAdapter adapter = mDialog.getAdapter();
            if (adapter != null){
                list = adapter.getCheckedItems();
            }
        }
        if (list.isEmpty() || !list.contains("person")){
            list.add("person");
        }
        return list;
    }

    private void showOptions(){
        if (mDialog == null){
            mDialog = new OptionsDialog(this);
        }
        mDialog.setItems(mList);
        mDialog.show();
    }

    private void stopTask(){
        dataHandler.removeCallbacks(getDataRunnable);
        dataHandler.removeCallbacksAndMessages(null);
        for (int i=0; i<taskList.size(); i++){
            RestTask task = taskList.get(i);
            task.cancel(true);
        }
        mViewModel.setDetectingState(false);
        binding.invalidateAll();
        speakOut();
    }

    private final Handler dataHandler = new Handler();

    private final Runnable getDataRunnable = new Runnable() {
        @Override
        public void run() {
            getData();
            dataHandler.postDelayed(getDataRunnable, 500);
        }
    };
    private void speakOut() {
        Result result = mViewModel.getResult().getValue();
        if (result != null){
            List<String> detectedList = result.getObjects(getFilter());
            StringBuilder str = new StringBuilder();
            for (String s: detectedList
            ) {
                str.append(s).append(", ");
            };
            if (str.length()>3){
                CharSequence text = str + "가 감지되었습니다.";
                mTTS.setPitch((float) 0.6);
                mTTS.setSpeechRate((float) 0.1);
                mTTS.speak(text,TextToSpeech.QUEUE_FLUSH,null,"id1");
            }

        }
    }
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS)  {
            int result = mTTS.setLanguage(Locale.KOREA);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                speakOut();
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }
}