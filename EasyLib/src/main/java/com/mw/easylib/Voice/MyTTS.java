package com.mw.easylib.Voice;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;


/**
 * Created by wang on 2019/7/20.
 */

public class MyTTS extends UtteranceProgressListener {
    private TextToSpeech tts;
    private boolean isSupportCN = true;
    private static MyTTS instance;
    private callBackThemeString startPlay;
    private callBackThemeString endPlay;
    private HashMap<String, Integer> voiceCount=new HashMap<>();

    public static MyTTS getInstance(Context context) {
        if (instance == null) {
            synchronized (MyTTS.class) {
                instance = new MyTTS(context.getApplicationContext());
            }
        }
        return instance;
    }

    private MyTTS(final Context context) {
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.CHINA);
                    tts.setPitch(1.0f);
                    tts.setSpeechRate(1.0f);
                    tts.setOnUtteranceProgressListener(MyTTS.this);
                    if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA) {
                        isSupportCN = false;
                        Toast.makeText(context, "抱歉，不支持中文播放", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onStart(String utteranceId) {
        if (voiceCount.containsKey(utteranceId)) {
            if (voiceCount.get(utteranceId) != 3) {
                voiceCount.put(utteranceId, voiceCount.get(utteranceId) + 1);
            }
        } else { Log.d("xulc", "onstart---utteranceId--->" + utteranceId);
            startPlay.run(utteranceId);
            voiceCount.put(utteranceId, 1);

        }


    }

    @Override
    public void onDone(String utteranceId) {
        if (voiceCount.containsKey(utteranceId)) {
            if (voiceCount.get(utteranceId) == 3) {
                Log.d("xulc", "onend---utteranceId--->" + utteranceId);
                endPlay.run(utteranceId);
                voiceCount.remove(utteranceId);
            }
        }
    }

    @Override
    public void onError(String utteranceId) {
        endPlay.run(utteranceId);
        Log.d("xulc", "onError---utteranceId--->" + utteranceId);
    }


    public TextToSpeech getTts() {
        Log.i("asdfasdf23asd",tts+"");
        return tts;
    }

    public boolean isSupportCN() {
        return isSupportCN;
    }

    public callBackThemeString getStartPlay() {
        return startPlay;
    }

    public void setStartPlay(callBackThemeString startPlay) {
        this.startPlay = startPlay;
    }

    public callBackThemeString getEndPlay() {
        return endPlay;
    }

    public void setEndPlay(callBackThemeString endPlay) {
        this.endPlay = endPlay;
    }
    public interface callBackThemeString {
        void run(String string);
    }

}
