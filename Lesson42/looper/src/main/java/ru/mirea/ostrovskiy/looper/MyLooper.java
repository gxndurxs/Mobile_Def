package ru.mirea.ostrovskiy.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class MyLooper extends Thread {
    public Handler mHandler;
    private Handler mainHandler;

    public MyLooper(Handler mainThreadHandler) {
        mainHandler = mainThreadHandler;
    }

    public void run() {
        Log.d("MyLooper", "run");
        Looper.prepare();

        mHandler = new Handler(Looper.myLooper()) {
            public void handleMessage(Message msg) {
                String data = msg.getData().getString("KEY");
                Log.d("MyLooper get message: ", data);

                int age = msg.getData().getInt("AGE");
                String job = msg.getData().getString("JOB");

                try {
                    Thread.sleep(age * 100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("result", String.format("Результат: работа '%s' заняла %d секунд", job, age));
                message.setData(bundle);

                mainHandler.sendMessage(message);
            }
        };
        Looper.loop();
    }
}