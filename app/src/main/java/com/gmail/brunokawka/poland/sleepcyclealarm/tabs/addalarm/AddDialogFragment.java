package com.gmail.brunokawka.poland.sleepcyclealarm.tabs.addalarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.gmail.brunokawka.poland.sleepcyclealarm.tabs.ui.dialog.ItemDialogContract;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.Const;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.UUID;

public abstract class AddDialogFragment extends Fragment {

    private static final int RINGTONE_INTENT_REQUEST_CODE = 1;
    private AlertDialog dialog;
    private ItemDialogContract dialogContract;

    @Subscribe
    public void handleAddAlarmEvent(AddAlarmEvent addAlarmEvent) {
        boolean isDialogHidden = dialog == null || !dialog.isShowing();
        if (isDialogHidden) {
            final Item item = addAlarmEvent.getItem();
            showDialog(item);
        }
    }

    public void showDialog(final Item item) {
        String positiveButtonText = getString(R.string.add);
        String negativeButtonText = getString(R.string.cancel);
        final View content = getLayoutInflater().inflate(R.layout.dialog_item_ringtone, null);
        dialogContract = (ItemDialogContract) content;
        dialogContract.bind(item);
        AlertDialog.Builder builder
                = new AlertDialog.Builder(getActivity(), R.style.Theme_SleepCycleAlarm_Dialog);
        builder.setView(content)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Alarm alarm = getAlarmFromItem(item);
                        addAlarm(alarm);
                    }
                })
                .setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.cancel();
                    }
                });
        content.findViewById(R.id.i_dialog_item_ringtone)
            .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startRingtonePickerActivityForResult();
                }
            });
        dialog = builder.create();
        dialog.show();
    }

    public void addAlarm(Alarm alarm) {
        Context ctx = getContext();
        if (ctx != null) {
            new AlarmController(ctx).setAlarm(alarm);
            new AlarmDAO().saveIfNotDuplicate(alarm);
        } else {
            Log.e(getClass().getName(), "addAlarm(): ctx == null");
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
