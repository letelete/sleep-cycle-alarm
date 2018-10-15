package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.accessalarm.alarms;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Alarm;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class AlarmsEditAlarmView extends LinearLayout implements AlarmsFragment.DialogContract {

    private String ringtone;
    private Alarm alarm;

    @BindView(R.id.alarmsEditRingtoneSummary) protected TextView textRingtoneSummary;

    @OnClick(R.id.alarmsEditRingtoneClickable)
    public void selectRingtone() {
        Toast.makeText(getContext(), "Ziup", Toast.LENGTH_SHORT).show();
    }

    @OnTextChanged(R.id.alarmsEditRingtoneSummary)
    public void ringtoneChanged(CharSequence ringtone) {
        this.ringtone = ringtone.toString();
    }

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
    public void bind(Alarm alarm) {
        String ringtone = alarm.getRingtone();
        setTextRingtoneSummary();
        this.ringtone = ringtone;
        this.alarm = alarm;
    }

    private void setTextRingtoneSummary() {
        Context context = getContext();
        String summary = context.getString(R.string.pref_ringtone_select_default_summary);

        try {
            Uri ringtoneUri = Uri.parse(alarm.getRingtone());
            Ringtone ringtone = RingtoneManager.getRingtone(context, ringtoneUri);
            String ringtoneTitle = ringtone.getTitle(context);
            String stringToParse = context.getString(R.string.pref_ringtone_select_selected_summary);
            summary = String.format(stringToParse, ringtoneTitle);
        } catch (Exception e) {
            Log.e(getClass().getName(), "Error while setting ringtone summary: " + e.getMessage());
        } finally {
            textRingtoneSummary.setText(summary);
        }
    }
}
