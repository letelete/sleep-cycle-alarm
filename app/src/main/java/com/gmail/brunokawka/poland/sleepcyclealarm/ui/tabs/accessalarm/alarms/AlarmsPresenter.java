package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.accessalarm.alarms;

import android.support.annotation.NonNull;
import android.util.Log;

import com.gmail.brunokawka.poland.sleepcyclealarm.application.RealmManager;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.AlarmDAO;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Alarm;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Item;

import io.realm.Realm;

public class AlarmsPresenter implements AlarmsContract.AlarmsPresenter {

    private AlarmsContract.AlarmsView view;
    private AlarmDAO alarmDAO;

    private boolean hasView() {
        return view != null;
    }

    protected static AlarmsPresenter getService() {
        return AlarmsFragment.getAlarmsPresenter();
    }

    @Override
    public void bindView(AlarmsContract.AlarmsView view) {
        this.view = view;
        alarmDAO = new AlarmDAO();
    }

    @Override
    public void unbindView() {
        this.view = null;
        alarmDAO.cleanUp();
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
            Log.d(getClass().getName(), "Realm is NOT empty. Showing UI elements and setting up adapter...");
            view.setUpAdapter();
        } else {
            hideUiElements();
            Log.d(getClass().getName(), "Realm is empty. Hiding UI elements...");
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
    public void showEditDialog(Alarm alarm) {
        if(hasView()) {
            view.showEditAlarmDialog(alarm);
        }
    }

    @Override
    public void deleteAlarmById(final String id) {
        alarmDAO.removeFromRealmById(id);
    }

    @Override
    public void updateEditedAlarm(final AlarmsContract.AlarmsView.DialogContract dialogContract, final Alarm alarm) {
        String newRingtone = dialogContract.getRingtone();
        alarm.setRingtone(newRingtone);
        alarmDAO.saveAlarm(alarm);
        view.setUpAdapter();
    }
}
