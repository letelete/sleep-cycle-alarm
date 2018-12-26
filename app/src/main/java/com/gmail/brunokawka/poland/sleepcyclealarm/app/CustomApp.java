package com.gmail.brunokawka.poland.sleepcyclealarm.app;

import android.app.Application;
import android.content.Context;

import java.lang.ref.WeakReference;

import io.realm.Realm;

public class CustomApp extends Application {

    private static WeakReference<Context> contextWeakReference;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        contextWeakReference = new WeakReference<>(getApplicationContext());
    }

    public static Context getContext() {
        return contextWeakReference.get();
    }
}
