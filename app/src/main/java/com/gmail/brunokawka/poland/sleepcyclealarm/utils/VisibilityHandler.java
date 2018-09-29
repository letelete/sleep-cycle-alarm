package com.gmail.brunokawka.poland.sleepcyclealarm.utils;

import android.view.View;

public class VisibilityHandler {

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
