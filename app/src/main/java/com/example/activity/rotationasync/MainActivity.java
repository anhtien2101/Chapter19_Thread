package com.example.activity.rotationasync;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    private ProgressBar bar;
    private RotationAwareTask task = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bar = (ProgressBar) findViewById(R.id.progressbar);
        bar.setIndeterminate(true);
        task = (RotationAwareTask) getLastCustomNonConfigurationInstance();
        if (task == null) {
            task = new RotationAwareTask(this);
            task.execute();
        } else {
            task.attach(this);
            updateProgress(task.getProgress());
            if (task.getProgress() >= 100) {
                markAsDone();
            }
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
            task.detach();
            return task;
    }

    private void updateProgress(int progress){
        bar.setProgress(progress);
    }

    void markAsDone(){
        findViewById(R.id.tvDone).setVisibility(View.VISIBLE);
    }

    class RotationAwareTask extends AsyncTask<Void, Void, Void> {
        MainActivity activity = null;
        int progress = 0;

        public RotationAwareTask(MainActivity activity) {
            attach(activity);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0;i < 20;i++) {
                SystemClock.sleep(500);
                publishProgress();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            if (activity == null) {
                Log.d("Main", "no activity");
            } else {
                progress = progress + 5;
                updateProgress(progress);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (activity == null) {
                Log.d("Main", "no activity");
            } else {
                markAsDone();
            }
        }

        void attach(MainActivity activity){
            this.activity = activity;
        }

        void detach(){
            activity = null;
        }

        int getProgress(){
            return progress;
        }
    }
}
