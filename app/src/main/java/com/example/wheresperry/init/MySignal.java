package com.example.wheresperry.init;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

public class MySignal {


    private static MySignal mySignal = null;
    private Context context;


    private MySignal(Context context){this.context = context;}

    public static void init(Context context){
        if (mySignal == null) {
            mySignal = new MySignal(context);
        }
    }
    public static MySignal getInstance(){return mySignal;}

    public void toast(String text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }

    public void vibrate() {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 400 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(400);
        }

    }

}
