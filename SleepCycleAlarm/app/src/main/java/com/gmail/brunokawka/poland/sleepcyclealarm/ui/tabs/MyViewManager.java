package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs;

import android.view.View;

public class MyViewManager {

    public static void showIfNotVisible(View view) {
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void hideIfNotGone(View view) {
        if (view.getVisibility() != View.GONE) {
            view.setVisibility(View.GONE);
        }
    }
}
