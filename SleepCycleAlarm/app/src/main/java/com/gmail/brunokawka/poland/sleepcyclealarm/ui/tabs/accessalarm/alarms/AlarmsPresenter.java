package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.accessalarm.alarms;

import android.content.Context;
import android.util.Log;

import com.gmail.brunokawka.poland.sleepcyclealarm.application.RealmManager;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.Alarm.Alarm;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.Alarm.MyAlarmManager;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.Item;

import io.realm.Realm;

public class AlarmsPresenter implements AlarmsContract.AlarmsPresenter {

    public static AlarmsPresenter getService(Context context) {
        return AlarmsFragment.getAlarmsPresenter();
    }

    public static final String TAG = "AlarmsPresenterLog";

    private AlarmsContract.AlarmsView view;
    private MyAlarmManager myAlarmManager;
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
        myAlarmManager = new MyAlarmManager();
    }

    @Override
    public void unbindView() {
        this.view = null;
        myAlarmManager.cleanUp();
    }


    @Override
    public void handleRealmChange() {
        if (hasView()) {
            if (isRealmEmpty()) {
                hideUiElements();
            } else {
                showUiElements();
            }
        }
    }

    @Override
    public void setUpUIDependingOnDatabaseItemAmount() {
        view.setUpRecycler();
        if (!isRealmEmpty()) {
            showUiElements();
            Log.d(TAG, "Realm is NOT empty. Showing UI elements and setting up adapter...");
            view.setUpAdapter();
        } else {
            hideUiElements();
            Log.d(TAG, "Realm is empty. Hiding UI elements...");
        }
    }

    private boolean isRealmEmpty() {
        return RealmManager.getRealm().isEmpty();
    }

    private void showUiElements() {
        view.hideEmptyListHint();
        view.showList();
        view.showInfoCard();
    }

    private void hideUiElements() {
        view.hideList();
        view.hideInfoCard();
        view.showEmptyListHint();
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
            myAlarmManager.generateAlarmAndSaveItToRealm(item, dialogContract.getRingtone());
        }
    }

    @Override
    public void deleteAlarmByIdWithDialog(final String id) {
        // TODO: if user swipes list item confirmation dialog will be displayed
    }

    @Override
    public void deleteAlarmById(final String id) {
        myAlarmManager.removeFromRealmById(id);
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
