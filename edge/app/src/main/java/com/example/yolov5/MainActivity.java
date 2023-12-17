package com.example.yolov5;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yolov5.databinding.ActivityMainBinding;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private String displayedImage;
    private ActivityMainBinding mBinding;
    private TextToSpeech mTTS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mTTS = new TextToSpeech(this, this);
        OkHttpClient client = new OkHttpClient();
        ImageView imageView = mBinding.mainImageView;
        TextView textView = mBinding.imageDescrView;
        Request request = new Request.Builder()
                .url("http://10.0.2.2:8000/api_root/Post/")
                .build();
        Timer timer = new Timer();
        ObjectMapper objectMapper = new ObjectMapper();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful()){
                            JsonNode data = objectMapper.readTree(response.body().string());
                            if (data != null){
                                if (data.size()>0){
                                    JsonNode last = data.get(data.size()-1);
                                    Result result = objectMapper.treeToValue(last, Result.class);
                                    String imageUrl = result.getImageUrl();
                                    if (imageUrl != null){
                                        if (!imageUrl.equals(displayedImage)){
                                            displayedImage = imageUrl;
                                            String finalImageUrl = imageUrl;
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Glide.with(imageView.getContext())
                                                            .load(finalImageUrl)
                                                            .error(androidx.appcompat.R.drawable.test_level_drawable)
                                                            .placeholder(androidx.appcompat.R.drawable.test_level_drawable)
                                                            .into(imageView);
                                                    String message = result.getText();
                                                    String pattern = "\\b\\d+\\s+person(s)?,?\\s*";
                                                    Pattern regex = Pattern.compile(pattern);
                                                    Matcher matcher = regex.matcher(message);
                                                    String finalStr = matcher.replaceAll("");
                                                    if (finalStr.length()>2){
                                                        if (finalStr.endsWith(",")){
                                                            finalStr = finalStr.replace(",", "");
                                                        }
                                                        String resultText = finalStr
                                                                + "가 감지되었습니다.";
                                                        textView.setText(resultText);
                                                        speakOut();
                                                    }
                                                    else {
                                                        textView.setText("물체가 감지되지 않았습니다");
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
            }
        };
        timer.schedule(timerTask, 3000, 3000);


    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void speakOut() {
        CharSequence text = mBinding.imageDescrView.getText();
        mTTS.setPitch((float) 0.6);
        mTTS.setSpeechRate((float) 0.1);
        mTTS.speak(text,TextToSpeech.QUEUE_FLUSH,null,"id1");
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