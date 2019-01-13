package com.gmail.brunokawka.poland.sleepcyclealarm.tabs.addalarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.AlarmDAO;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Alarm;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Item;
import com.gmail.brunokawka.poland.sleepcyclealarm.events.AddAlarmEvent;
import com.gmail.brunokawka.poland.sleepcyclealarm.schedule.AlarmController;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.Const;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.UUID;

public abstract class AddAlarmAbstractFragment extends Fragment {

    private static final int RINGTONE_INTENT_REQUEST_CODE = 1;
    private AlertDialog dialog;
    private AddAlarmContract dialogContract;

    @Subscribe
    public void onAddAlarmEvent(AddAlarmEvent addAlarmEvent) {
        showAddAlarmDialog(addAlarmEvent.getItem());
    }

    public void showAddAlarmDialog(final Item item) {

        final View content = getLayoutInflater().inflate(R.layout.dialog_add_alarm, null);
        dialogContract = (AddAlarmContract) content;
        dialogContract.bind(item);
        dialog = new AlertDialog.Builder(getActivity()).create();
            dialog.setView(content);

            content.findViewById(R.id.ll_alarm_ringtone)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRingtonePickerActivityForResult();
            }
        });
            content.findViewById(R.id.alarmsEditOkButton)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAlarmAddClicked(item);
                dialog.dismiss();
            }
        });
            content.findViewById(R.id.alarmsEditCancelButton)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
            dialog.show();
    }

    public void onAlarmAddClicked(Item item) {
        Context ctx = getContext();
        if (ctx != null) {
            Alarm alarm = getAlarmFromItem(item);
            new AlarmController(ctx).setAlarm(alarm);
            new AlarmDAO().saveIfNotDuplicate(alarm);
        } else {
            Log.e(getClass().getName(), "onAlarmAddClicked(): ctx == null");
        }
    }

    private void startRingtonePickerActivityForResult() {
        Uri selectedRingtoneUri = Uri.parse(dialogContract.getRingtone());
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE,
                getString(R.string.pref_ringtone_select_title));
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, selectedRingtoneUri);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI, selectedRingtoneUri);
        startActivityForResult(intent, RINGTONE_INTENT_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == RINGTONE_INTENT_REQUEST_CODE) {
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

            if (uri != null && dialogContract != null) {
                String chosenRingtone = uri.toString();
                dialogContract.setRingtone(chosenRingtone);
            }
        }
    }

    public Alarm getAlarmFromItem(Item item) {
        Context ctx = getContext();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);

        final String id = UUID.randomUUID().toString();
        final String title = item.getTitle();
        final String summary = item.getSummary();
        final String ringtone = dialogContract != null
                ? dialogContract.getRingtone()
                : pref.getString(ctx.getString(R.string.key_ringtone_select),
                    Const.DEFAULTS.ALARM_SOUND);
        final String currentDate = item.getCurrentDate().toString();
        final String executionDate = item.getExecutionDate().toString();

        Alarm alarm = new Alarm();
        alarm.setId(id);
        alarm.setTitle(title);
        alarm.setSummary(summary);
        alarm.setRingtone(ringtone);
        alarm.setCurrentDate(currentDate);
        alarm.setExecutionDate(executionDate);

        return alarm;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!isEventBusRegistered()) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isEventBusRegistered()) {
            EventBus.getDefault().unregister(this);
        }
    }

    private boolean isEventBusRegistered() {
       return EventBus.getDefault().isRegistered(this);
    }
}
