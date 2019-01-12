package com.gmail.brunokawka.poland.sleepcyclealarm.tabs.addalarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Item;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.Const;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddAlarmDialog extends LinearLayout implements AddAlarmContract {

    private String ringtone;
    private Context ctx;
    private SharedPreferences pref;
    private boolean alarmsHaveTheSameRingtone;

    @BindView(R.id.tv_alarm_execution_hour)
    TextView tvAlarmExecutionHour;

    @BindView(R.id.tv_alarm_health_status)
    TextView tvAlarmHealthStatus;

    @BindView(R.id.tv_ringtone_subtitle)
    TextView tvRingtoneSubtitle;

    @BindView(R.id.ll_alarm_ringtone)
    LinearLayout llAlarmRingtone;

    public AddAlarmDialog(Context context) {
        super(context);
    }

    public AddAlarmDialog(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AddAlarmDialog(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AddAlarmDialog(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @Override
    public void bind(Item item) {
        ctx = getContext();
        pref = PreferenceManager.getDefaultSharedPreferences(ctx);

        alarmsHaveTheSameRingtone = pref.
                getBoolean(ctx.getString(R.string.key_alarms_has_same_ringtone),
                        Const.DEFAULTS.ALARMS_HAVE_SAME_RINGTONE);

        final String ringtone = pref.getString(ctx.getString(R.string.key_ringtone_select),
                Const.DEFAULTS.ALARM_SOUND);

        setRingtone(ringtone);
        setRingtoneSubtitle();
        setAlarmExecutionHour(item.getTitle());
        setAlarmHealthStatus(item.getSummary());
        handleRingtoneSelectionVisibility();
    }

    private void handleRingtoneSelectionVisibility() {
        if (alarmsHaveTheSameRingtone) {
            llAlarmRingtone.setVisibility(View.GONE);
        } else {
            llAlarmRingtone.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public String getRingtone() {
        return ringtone;
    }

    @Override
    public void setRingtone(String ringtone) {
        this.ringtone = ringtone;
        setRingtoneSubtitle();
    }

    private void setRingtoneSubtitle() {
        Context ctx = getContext();
        String summary = ctx.getString(R.string.pref_ringtone_select_default_summary);
        try {
            Uri uri = Uri.parse(ringtone);
            Ringtone ringtone = RingtoneManager.getRingtone(getContext(), uri);
            String title = ringtone.getTitle(getContext());
            String stringToParse = ctx.getString(R.string.pref_ringtone_select_selected_summary);
            summary = String.format(stringToParse, title);
        } catch (Exception e) {
            Log.e(getClass().getName(), "setRingtoneSubtitle(): " + e.getMessage());
        } finally {
            tvRingtoneSubtitle.setText(summary);
        }
    }

    @Override
    public void setAlarmExecutionHour(String alarmExecutionHour) {
        String alarmTitleBase = getContext().getString(R.string.alarm_will_ring_at);
        String alarmTitle = String.format(alarmTitleBase, alarmExecutionHour);
        tvAlarmExecutionHour.setText(alarmTitle);
    }

    @Override
    public void setAlarmHealthStatus(String alarmHealthStatus) {
        tvAlarmHealthStatus.setText(alarmHealthStatus);
    }
}
