package com.c3labs.androidservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Random;

public class MyService extends Service {
    private static final String TAG = "demo";
    int number;
    boolean generatorOn;
    int min = 0;
    int max = 100;
    class MyServiceBinder extends Binder{
        public MyService getService(){
            return MyService.this;
        }
    }
    private IBinder mBinder = new MyServiceBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        generatorOn = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                startRandomNumberGenerator();
            }
        }).start();

        return START_STICKY;
    }
    void startRandomNumberGenerator(){
        while (generatorOn){
            try {
                Thread.sleep(1000);
                if (generatorOn){
                    number = new Random().nextInt(max)+min;
                    Log.d(TAG, "startRandomNumberGenerator: "+Thread.currentThread().getId()+"    "+number);

                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
    void stopRandomNumberGenerator(){
        generatorOn = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRandomNumberGenerator();
    }
    public int getRandomNumber(){
        return number;
    }
}
