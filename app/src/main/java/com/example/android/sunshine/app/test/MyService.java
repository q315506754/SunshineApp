package com.example.android.sunshine.app.test;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    private static final String TAG = MyService.class.getSimpleName();
    private Thread thread;


    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        super.onCreate();

//        while (true) {
//        Log.i(TAG, "create loop...");
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Log.i(TAG, "create loop in thread...");

                            Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.e(TAG, "create loop stopped...",e);
                }
            }
        });

        thread.start();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        if (thread != null) {
            thread.interrupt();
        }
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.i(TAG, "onStart");
        super.onStart(intent, startId);
    }
}
