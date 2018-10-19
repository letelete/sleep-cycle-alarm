package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.accessalarm.alarms;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Alarm;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlarmsEditAlarmView extends LinearLayout implements AlarmsFragment.DialogContract {

    private String ringtone;

    @BindView(R.id.alarmsEditRingtoneSummary) protected TextView textRingtoneSummary;

    public AlarmsEditAlarmView(Context context) {
        super(context);
    }

    public AlarmsEditAlarmView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AlarmsEditAlarmView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public AlarmsEditAlarmView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @Override
    public String getRingtone() {
        return ringtone;
    }

    @Override
    public void setRingtone(String ringtone) {
        this.ringtone = ringtone;
        setTextRingtoneSummary();
    }

    @Override
    public void bind(Alarm alarm) {
        this.ringtone = alarm.getRingtone();
        setTextRingtoneSummary();
    }

    private void setTextRingtoneSummary() {
        Context context = getContext();
        String summary = context.getString(R.string.pref_ringtone_select_default_summary);
        try {
            Uri uri = Uri.parse(ringtone);
            Ringtone ringtone = RingtoneManager.getRingtone(getContext(), uri);
            String title = ringtone.getTitle(getContext());
            String stringToParse = context.getString(R.string.pref_ringtone_select_selected_summary);
            summary = String.format(stringToParse, title);
        } catch (Exception e) {
            Log.e(getClass().getName(), "Error while setting ringtone summary! Error msg: " + e.getMessage());
        } finally {
            textRingtoneSummary.setText(summary);
        }
    }
}
