package com.gmail.brunokawka.poland.sleepcyclealarm.tabs.ui.dialog;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Alarm;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Item;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.AlarmContentUtils;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.Const;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemDialog extends LinearLayout implements ItemDialogContract {

    private String itemRingtone;
    private String itemExecutionDate;
    private String itemSummary;
    private String dialogTitle;
    private boolean alarmsHaveTheSameRingtone;
    private Context ctx;

    @BindView(R.id.tv_dialog_item_title)
    TextView tvDialogTitle;

    @BindView(R.id.tv_dialog_item_description)
    TextView tvDialogDescription;

    @BindView(R.id.i_dialog_item_ringtone)
    View vItemRingtone;

    public ItemDialog(Context context) {
        super(context);
    }

    public ItemDialog(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemDialog(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ItemDialog(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        dialogTitle = ctx.getString(R.string.add_alarm);
        itemSummary = item.getSummary();
        itemExecutionDate = AlarmContentUtils.getTitle(item.getExecutionDate());

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        alarmsHaveTheSameRingtone = pref.
               getBoolean(ctx.getString(R.string.key_alarms_has_same_ringtone),
                        Const.DEFAULTS.ALARMS_HAVE_SAME_RINGTONE);

        String ringtone = pref.getString(ctx.getString(R.string.key_ringtone_select),
                Const.DEFAULTS.ALARM_SOUND);
        setRingtone(ringtone);
        bind();
        handleRingtoneSelectionVisibility();
    }

    @Override
    public void bind(Alarm alarm) {
        dialogTitle = getContext().getString(R.string.edit_alarm);
        itemSummary = alarm.getSummary();
        itemExecutionDate = AlarmContentUtils.getTitle(alarm.getExecutionDate());
        String ringtone = alarm.getRingtone();
        setRingtone(ringtone);
        bind();
    }

    private void bind() {
        if (ctx == null) {
            ctx = getContext();
        }
        setDialogTitle();
        setRingtoneIcon();
        setRingtoneTitle();
        setDialogDescription();
    }

    public void setDialogTitle() {
        tvDialogTitle.setText(dialogTitle);
    }

    private void setRingtoneIcon() {
        ((ImageView) vItemRingtone.findViewById(R.id.iv_item_alarm_icon))
                .setImageResource(R.drawable.ic_list_ringtone_full_shape);
    }

    private void setRingtoneTitle() {
        String title = ctx.getString(R.string.alarm_ringtone);
        ((TextView) vItemRingtone.findViewById(R.id.tv_item_alarm_title)).setText(title);
    }

    public void setDialogDescription() {
        String alarmTitleBase = getContext().getString(R.string.dialog_alarm_description);
        String description = String.format(
                alarmTitleBase,
                itemExecutionDate,
                itemSummary);
        tvDialogDescription.setText(description);
    }

    @Override
    public void setRingtone(String itemRingtone) {
        this.itemRingtone = itemRingtone;
        updateRingtoneItem();
    }

    @Override
    public String getRingtone() {
        return itemRingtone;
    }

    private void updateRingtoneItem() {
        setRingtoneSubtitle();
    }

    private void setRingtoneSubtitle() {
        Context ctx = getContext();
        String summary = ctx.getString(R.string.pref_ringtone_select_default_summary);
        try {
            Uri uri = Uri.parse(itemRingtone);
            Ringtone ringtone = RingtoneManager.getRingtone(getContext(), uri);
            String title = ringtone.getTitle(getContext());
            String stringToParse = ctx.getString(R.string.pref_ringtone_select_selected_summary);
            summary = String.format(stringToParse, title);
        } catch (Exception e) {
            Log.e(getClass().getName(), "setRingtoneSubtitle(): " + e.getMessage());
        } finally {
            ((TextView) vItemRingtone.findViewById(R.id.tv_item_alarm_subtitle))
                    .setText(summary);
        }
    }

    private void handleRingtoneSelectionVisibility() {
        vItemRingtone.setVisibility(alarmsHaveTheSameRingtone
                ? View.GONE
                : View.VISIBLE);
    }
}
