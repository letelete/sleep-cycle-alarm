package com.gmail.brunokawka.poland.sleepcyclealarm.tabs.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.Button;

public class WakeUpAtSetHourButton extends AnimatorListenerAdapter {

    private final int ANIM_TIME_IN_MS = 400;
    private final float POS_TO_MAKE_BUTTON_GONE = 200f;
    private final float POS_TO_MAKE_BUTTON_VISIBLE = -150f;

    private int wakeUpAtButtonVisibilityAfterStartedAnimation = View.GONE;
    private ObjectAnimator wakeUpAtButtonAnimator;
    private Button wakeUpAtButton;
    private int finalViewId;

    public WakeUpAtSetHourButton(Button wakeUpAtButton) {
        this.wakeUpAtButton = wakeUpAtButton;
    }

    public void show() {
        this.finalViewId = View.VISIBLE;
        animateWakeUpAtButton(POS_TO_MAKE_BUTTON_GONE, POS_TO_MAKE_BUTTON_VISIBLE);
    }

    public void hide() {
        this.finalViewId = View.GONE;
        animateWakeUpAtButton(POS_TO_MAKE_BUTTON_VISIBLE, POS_TO_MAKE_BUTTON_GONE);
    }

    private void animateWakeUpAtButton(final float startPosY, final float endPosY) {
        if (wakeUpAtButtonVisibilityAfterStartedAnimation != finalViewId) {
            cancelAnimationIfNeeded();

            wakeUpAtButtonVisibilityAfterStartedAnimation = finalViewId;
            wakeUpAtButtonAnimator = ObjectAnimator.ofFloat(wakeUpAtButton,
                    "translationY", startPosY, endPosY);
            wakeUpAtButtonAnimator.setDuration(ANIM_TIME_IN_MS);
            wakeUpAtButtonAnimator.addListener(this);
            wakeUpAtButtonAnimator.start();
        }
    }

    private void cancelAnimationIfNeeded() {
        if (wakeUpAtButtonAnimator !=null && wakeUpAtButtonAnimator.isRunning()){
            wakeUpAtButtonAnimator.cancel();
        }
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (finalViewId != View.VISIBLE) {
            wakeUpAtButton.setVisibility(View.GONE);
        }
    }
    @Override
    public void onAnimationStart(Animator animation) {
        if (wakeUpAtButton.getVisibility() != View.VISIBLE) {
            wakeUpAtButton.setVisibility(View.VISIBLE);
        }
    }
}
