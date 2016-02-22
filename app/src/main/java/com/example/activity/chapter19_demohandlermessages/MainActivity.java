package com.example.activity.chapter19_demohandlermessages;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import java.util.concurrent.atomic.AtomicBoolean;


public class MainActivity extends AppCompatActivity {
    ProgressBar bar;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            bar.incrementProgressBy(5);
        }
    };
    AtomicBoolean isRunning = new AtomicBoolean(false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bar = (ProgressBar) findViewById(R.id.progressbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bar.setProgress(0);
        Thread background = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 20 && isRunning.get(); i++) {
                        Thread.sleep(1000);
                        handler.sendMessage(handler.obtainMessage());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        isRunning.set(true);
        background.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isRunning.set(false);
    }
}
