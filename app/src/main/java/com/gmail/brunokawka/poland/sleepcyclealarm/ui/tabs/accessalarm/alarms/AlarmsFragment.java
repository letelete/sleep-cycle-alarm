package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.accessalarm.alarms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.application.RealmManager;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Alarm;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Item;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.VisibilityHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;

public class AlarmsFragment extends Fragment
    implements AlarmsContract.AlarmsView {
    private static final String TAG = "AlarmsFragmentLog";

    private Item item;
    private AlarmScopeListener alarmScopeListener;
    static AlarmsPresenter alarmsPresenter;
    AlertDialog dialog;

    @BindView(R.id.alarms_root)
    ViewGroup root;

    @BindView(R.id.alarmsList)
    RecyclerView recycler;

    @BindView(R.id.alarmsListCardView)
    CardView listCardView;

    @BindView(R.id.alarmsEmptyListPlaceHolder)
    View emptyListPlaceHolder;

    @BindView(R.id.alarmsInfoCardView)
    CardView infoCard;

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = layoutInflater.inflate(R.layout.fragment_alarms, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RealmManager.initializeRealmConfig();

        addScopeListener();
        alarmsPresenter = alarmScopeListener.getPresenter();
        alarmsPresenter.bindView(this);

        alarmsPresenter.setUpUIDependingOnDatabaseItemAmount();
    }

    public void addScopeListener() {
        alarmScopeListener = (AlarmScopeListener) getFragmentManager().findFragmentByTag("SCOPE_LISTENER");
        if (alarmScopeListener == null) {
            alarmScopeListener = new AlarmScopeListener();
            getFragmentManager().beginTransaction().add(alarmScopeListener, "SCOPE_LISTENER").commit();
        }
    }

    @Override
    public void setUpRecycler() {
        recycler.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);

    }

    @Override
    public void setUpAdapter() {
        Realm realm = RealmManager.getRealm();
        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
                onRealmChangeEvent();
            }
        });
        recycler.setAdapter(new AlarmsAdapter(realm.where(Alarm.class).findAllAsync()));
    }

    @Override
    public void showAddAlarmDialog() {
        final View content = getLayoutInflater().inflate(R.layout.dialog_edit_item, root, false);
        final DialogContract dialogContract = (DialogContract) content;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(content)
                .setTitle(getString(R.string.dialog_add_new_alarm))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alarmsPresenter.saveAlarm(dialogContract, item);
                        alarmsPresenter.dismissAddDialog();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alarmsPresenter.saveAlarm(dialogContract, item);
                        alarmsPresenter.dismissAddDialog();
                    }
                });
    }

    @Override
    public void showEditAlarmDialog(Alarm alarm) {
        if(!alarm.isValid()) {
            return;
        }
        final View content = getLayoutInflater().inflate(R.layout.dialog_edit_item, root, false);
        final AlarmsContract.AlarmsView.DialogContract dialogContract = (AlarmsContract.AlarmsView.DialogContract) content;
        dialogContract.bind(alarm);

        final String id = alarm.getId();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(content)
                .setTitle(getString(R.string.dialog_edit_alarm))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alarmsPresenter.editAlarm(dialogContract, id);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void showList() {
        VisibilityHandler.showIfNotVisible(listCardView);
    }

    @Override
    public void hideList() {
        VisibilityHandler.hideIfNotGone(listCardView);
    }

    @Override
    public void showInfoCard() {
        VisibilityHandler.showIfNotVisible(infoCard);
    }

    @Override
    public void hideInfoCard() {
        VisibilityHandler.hideIfNotGone(infoCard);
    }

    @Override
    public void showEmptyListHint() {
        VisibilityHandler.showIfNotVisible(emptyListPlaceHolder);
    }

    @Override
    public void hideEmptyListHint() {
        VisibilityHandler.hideIfNotGone(emptyListPlaceHolder);
    }

    public static AlarmsPresenter getAlarmsPresenter() {
        return alarmsPresenter;
    }

    @Override
    public void onDestroyView() {
        if(alarmsPresenter != null) {
            alarmsPresenter.unbindView();
        }
        if(dialog != null) {
            dialog.dismiss();
        }
        super.onDestroyView();
    }

    private void onRealmChangeEvent() {
        Log.d(TAG, "Realm change event received.");
        if (alarmsPresenter != null) {
            Log.d(TAG, "Handling realm change...");
            alarmsPresenter.handleRealmChange();
        } else {
            Log.d(TAG, "Couldn't handle realm change. AlarmsPresenter is null.");
        }
    }
}