package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.accessalarm.alarms;

import android.util.Log;

import com.gmail.brunokawka.poland.sleepcyclealarm.application.RealmManager;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.AlarmDAO;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Alarm;

public class AlarmsPresenter implements AlarmsContract.AlarmsPresenter {

    private AlarmsContract.AlarmsView view;
    private AlarmDAO alarmDAO;

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
        updateUi();
    }

    @Override
    public void setUpUi() {
        view.setUpRecycler();
        updateUi();
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
        Alarm editedAlarm = getEditedAlarm(dialogContract, alarm);
        alarmDAO.saveEvenIfDuplicate(editedAlarm);
    }

    private boolean hasView() {
        return view != null;
    }

    private void updateUi() {
        if (!isRealmEmpty()) {
            Log.d(getClass().getName(), "Realm is NOT empty. Showing UI elements and setting up adapter...");
            view.setUpAdapter();
            showUiElements();
        } else {
            Log.d(getClass().getName(), "Realm is empty. Hiding UI elements...");
            hideUiElements();
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

    private Alarm getEditedAlarm(AlarmsContract.AlarmsView.DialogContract dialog, final Alarm previousAlarm) {
        String newRingtone = dialog.getRingtone();

        Alarm editedAlarm = new Alarm();
        editedAlarm.setId(previousAlarm.getId());
        editedAlarm.setTitle(previousAlarm.getTitle());
        editedAlarm.setSummary(previousAlarm.getSummary());
        editedAlarm.setCurrentDate(previousAlarm.getCurrentDate());
        editedAlarm.setExecutionDate(previousAlarm.getExecutionDate());
        editedAlarm.setRingtone(newRingtone);
        return editedAlarm;
    }
}
