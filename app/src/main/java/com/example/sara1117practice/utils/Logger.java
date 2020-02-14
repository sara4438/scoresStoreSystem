package com.example.sara1117practice.utils;

import android.util.Log;

public class Logger {
    public static void d(String tag, String msg){
        if(Global.DEBUG) {
            Log.d(tag, msg);
        }
    }
}