package com.example.activity.chapter19_threadlifecycle;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static ProgressDialog progressDialog;
    private static Bitmap downloadBitmap;
    private static ImageView image;
    private static Thread downloadThread;
    private static Handler handler;
    private Button btnDown;
    private Button btnReset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (ImageView) findViewById(R.id.image);
        btnDown = (Button) findViewById(R.id.btnLoad);
        btnReset = (Button) findViewById(R.id.btnReset);

        btnDown.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        handler = new Handler();

        if (downloadBitmap != null) {
            image.setImageBitmap(downloadBitmap);
        }
        downloadThread = (Thread) getLastCustomNonConfigurationInstance();
        if (downloadThread != null && downloadThread.isAlive() ) {
            progressDialog = ProgressDialog.show(this, "Download", "downloading...");
        }
    }

   // save thread

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return downloadThread;
    }

    @Override
    protected void onDestroy() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        super.onDestroy();
    }

    // method download image
    static private Bitmap downloadImage(String url){
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnLoad:
                //load image
                progressDialog = ProgressDialog.show(this, "Download", "downloading...");
                downloadThread = new MyThread();
                downloadThread.start();
                break;
            case R.id.btnReset:
                // reset image
                if (downloadBitmap != null) {
                    downloadBitmap = null;
                }
                image.setImageResource(R.drawable.icon_android);
                break;
        }
    }

    public static class MyThread extends Thread {
        @Override
        public void run() {
            try {
                new Thread().sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            downloadBitmap = downloadImage("http://www.vogella.com/img/lars/LarsVogelArticle7.png");
            handler.post(new MyRunnale());
        }
    }

    public static class MyRunnale implements Runnable {

        @Override
        public void run() {
            image.setImageBitmap(downloadBitmap);
            progressDialog.dismiss();
        }
    }
}
