package com.gmail.brunokawka.poland.sleepcyclealarm.tabs.activealarms;

import android.util.Log;

import com.gmail.brunokawka.poland.sleepcyclealarm.app.RealmManager;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.AlarmDAO;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Alarm;

public class AlarmsPresenter implements AlarmsContract.AlarmsPresenter {

    private AlarmsContract.AlarmsView view;
    private AlarmDAO alarmDAO;

    public static AlarmsPresenter getService() {
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
        if (view != null) {
            updateUi();
        }
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

    private boolean hasView() {
        return view != null;
    }

    private void updateUi() {
        if (!isRealmEmpty()) {
            Log.d(getClass().getName(),
                    "Realm is NOT empty. Showing UI elements and setting up adapter...");
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
}
