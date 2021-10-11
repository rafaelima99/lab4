package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button startButton;
    private TextView progressReport;
    private volatile boolean stopThread = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = findViewById(R.id.startButton);
        progressReport = findViewById(R.id.progressReport);
    }

    public void mockFileDownloader() {
        runOnUiThread(new Runnable() {
            @Override
                public void run() {
                    startButton.setText("Downloading...");
                }
        });

        for (int downloadProgress = 0; downloadProgress <=100; downloadProgress+=10){
            if (stopThread) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startButton.setText("Start");
                    }
                });
                return;
            }

            Log.d(TAG, "Download progress: " + downloadProgress + "%");
            int finalDownloadProgress = downloadProgress;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressReport.setText("Download progress: " + finalDownloadProgress + "%");
                }
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startButton.setText("Start");
            }
        });
    }

    class ExampleRunnable implements Runnable {
        @Override
        public void run(){
            mockFileDownloader();
        }
    }

    public void startDownload(View view) {
        ExampleRunnable runnable = new ExampleRunnable();
        new Thread(runnable).start();
        stopThread = false;
    }

    public void StopDownload(View view){
        stopThread = true;
    }
}