package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.accessalarm.alarms;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.Alarm;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

public class AlarmsAddAlarmView extends LinearLayout implements AlarmsFragment.DialogContract {

    public AlarmsAddAlarmView(Context context) {
        super(context);
    }

    public AlarmsAddAlarmView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AlarmsAddAlarmView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public AlarmsAddAlarmView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    // TODO: not sure how ringtone will be passed like so setted up String for entry testing
    String ringtone;

    @BindView(R.id.ringtone_select)
    EditText textRingtoneSelect;

    @OnTextChanged(R.id.ringtone_select)
    public void ringtoneChanged(CharSequence ringtone) {
        this.ringtone = ringtone.toString();
    }

    @Override
    public String getRingtone() {
        return ringtone;
    }

    @Override
    public void bind(Alarm alarm) {
        String ringtone = alarm.getRingtone();
        textRingtoneSelect.setText(ringtone);
        this.ringtone = ringtone;
    }


}
