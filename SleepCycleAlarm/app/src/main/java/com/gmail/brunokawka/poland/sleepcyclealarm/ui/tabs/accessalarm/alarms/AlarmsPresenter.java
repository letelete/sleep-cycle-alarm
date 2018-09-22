package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.accessalarm.alarms;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.application.CustomApp;
import com.gmail.brunokawka.poland.sleepcyclealarm.application.RealmManager;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.Alarm;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.Item;

import java.util.UUID;

import io.realm.Realm;

public class AlarmsPresenter implements AlarmsContract.AlarmsPresenter {

    public static AlarmsPresenter getService(Context context) {
        return AlarmsFragment.getAlarmsPresenter();
    }

    public static final String TAG = "AlarmsPresenterLog";

    private AlarmsContract.AlarmsView view;

    private boolean isDialogShowing;

    private boolean hasView() {
        return view != null;
    }

    @Override
    public void bindView(AlarmsContract.AlarmsView view) {
        this.view = view;
        if(isDialogShowing) {
            showAddDialog();
        }
    }

    @Override
    public void unbindView() {
        this.view = null;
    }

    @Override
    public void showAddDialog() {
        if(hasView()) {
            isDialogShowing = true;
            view.showAddAlarmDialog();
        }
    }

    @Override
    public void dismissAddDialog() {
        isDialogShowing = false;
    }

    @Override
    public void showEditDialog(Alarm alarm) {
        if(hasView()) {
            view.showEditAlarmDialog(alarm);
        }
    }

    @Override
    public void saveAlarm(AlarmsContract.AlarmsView.DialogContract dialogContract, final Item item) {
        if (hasView()) {
            Realm realm = RealmManager.getRealm();
            final Alarm alarm = getAlarmDependingOnGivenItemAndContract(item, dialogContract);

            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insertOrUpdate(alarm);
                }
            });
        }
    }

    private Alarm getAlarmDependingOnGivenItemAndContract(Item item, AlarmsContract.AlarmsView.DialogContract dialogContract) {
        Context ctx = CustomApp.getContext();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);

        final String id = UUID.randomUUID().toString();

        final String title = item.getTitle();

        final String summary = item.getSummary();

        final String ringtoneTitle = dialogContract.getRingtone(); // TODO : RINGTONE (Currently create some string for entry testing)

        final boolean isRingingInSilentMode = pref.getBoolean(ctx.getString(R.string.key_alarm_in_silent_mode), true);

        final int snoozeDuration = pref.getInt(ctx.getString(R.string.key_alarms_intervals), 5);

        final int ringDuration = pref.getInt(ctx.getString(R.string.key_ring_duration), 5);

        final int numberOfRepetitions = pref.getInt(ctx.getString(R.string.key_auto_silence), 3);

        Alarm alarm = new Alarm();
        alarm.setId(id);
        alarm.setTitle(title);
        alarm.setSummary(summary);
        alarm.setSnoozeDurationInMinutes(snoozeDuration);
        alarm.setRingingInSilentMode(isRingingInSilentMode);
        alarm.setRingtone(ringtoneTitle);
        alarm.setRingDurationInMinutes(ringDuration);
        alarm.setNumberOfRepetitionsBeforeAutoSilence(numberOfRepetitions);

        return alarm;
    }

    @Override
    public void deleteAlarmByIdWithDialog(final String id) {
        // TODO: if user swipes list item confirmation dialog will be displayed
    }

    @Override
    public void deleteAlarmById(final String id) {
        Realm realm = RealmManager.getRealm();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Alarm alarm = realm.where(Alarm.class).equalTo("id", id).findFirst();
                if(alarm != null) {
                    alarm.deleteFromRealm();
                }
            }
        });
    }

    @Override
    public void editAlarm(final AlarmsContract.AlarmsView.DialogContract dialogContract, final String id) {
        Realm realm = RealmManager.getRealm();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Alarm alarm = realm.where(Alarm.class).equalTo("id", id).findFirst();
                if(alarm != null) {
                    // TODO: RINGTONE
                    // Currently create some string for entry testing
                    alarm.setRingtone(dialogContract.getRingtone());
                }
            }
        });
    }
}
