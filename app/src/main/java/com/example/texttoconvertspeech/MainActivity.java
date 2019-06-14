package com.example.texttoconvertspeech;

import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    Button button;
    TextView textView;
    private String text;
    private String[] paragraphList;
    private int paragraphCount = 0;
    private int paragraphListLength = 0;
    private TextToSpeech textToSpeech;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.btn);
        textView = findViewById(R.id.tv);
        button.setOnClickListener(this);

        text = getString(R.string.short_article);
        paragraphList = text.split("\n\n");
        paragraphListLength = paragraphList.length;

        button.setText("play");
        textView.setText(text);
        initializeTextToSpeech();
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        if (!textToSpeech.isSpeaking()){
            button.setText("Stop");

            HashMap<String, String> map = new HashMap<>();
            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"speak");

            for(int i = 0; i<paragraphList.length; i++){

                textToSpeech.speak(paragraphList[i], TextToSpeech.QUEUE_ADD,map);
                textToSpeech.playSilentUtterance(250,TextToSpeech.QUEUE_ADD, null);
            }
        }

        else {
            textToSpeech.stop();
            button.setText("play");
        }


    }
    private void initializeTextToSpeech() {
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS){
                    textToSpeech.setLanguage(Locale.ENGLISH);
                    textToSpeech.setSpeechRate((float)1.25);

                    textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String utteranceId) {

                        }

                        @Override
                        public void onDone(String utteranceId) {

                            paragraphCount++;

                            if (paragraphCount == paragraphListLength){
                                paragraphCount = 0;
                                runOnUiThread(new Runnable() { // This method called UI related work  
                                    @Override
                                    public void run() {
                                        button.setText("Play");
                                    }
                                });
                            }
                        }

                        @Override
                        public void onError(String utteranceId) {

                        }
                    });
                }
            }
        });
    }


    protected void onDestroy() {
        super.onDestroy();

        if(textToSpeech!= null){
            textToSpeech.stop();
            textToSpeech.shutdown();

        }
    }
}
