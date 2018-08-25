package com.gmail.brunokawka.poland.sleepcyclealarm.alarms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.application.RealmManager;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.Alarm;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class AlarmsFragment extends Fragment
    implements AlarmsPresenter.ViewContract {

    private static final String TAG = "AlarmsFragmentLog";

    @BindView(R.id.alarms_root)
    ViewGroup root;

    @BindView(R.id.alarmsList)
    RecyclerView recycler;

    static AlarmsPresenter alarmsPresenter;
    AlertDialog dialog;

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

        AlarmScopeListener fragment = (AlarmScopeListener) getFragmentManager().findFragmentByTag("SCOPE_LISTENER");
        if(fragment == null) {
            fragment = new AlarmScopeListener();
            getFragmentManager().beginTransaction().add(fragment, "SCOPE_LISTENER").commit();
        }

        Realm realm = RealmManager.getRealm();

        alarmsPresenter = fragment.getPresenter();

        recycler.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);

        recycler.setAdapter(new AlarmsAdapter(realm.where(Alarm.class).findAllAsync()));

        alarmsPresenter.bindView(this);
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

    @Override
    public void showAddAlarmDialog() {
        final View content = getLayoutInflater().inflate(R.layout.edit_item, root, false);
        final DialogContract dialogContract = (DialogContract) content;
        final SharedPreferences preferencesToPass = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //TODO: passing data from 'Sleep now' or "Wake up at' fragment
        final int hour = 6;
        final int minute = 30;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(content)
                .setTitle(getString(R.string.dialog_add_new_alarm))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alarmsPresenter.saveAlarm(dialogContract, preferencesToPass, hour, minute);
                        alarmsPresenter.dismissAddDialog();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alarmsPresenter.saveAlarm(dialogContract, preferencesToPass, hour, minute);
                        alarmsPresenter.dismissAddDialog();
                    }
                });
    }

    @Override
    public void showEditAlarmDialog(Alarm alarm) {
        if(!alarm.isValid()) {
            return;
        }
        final View content = getLayoutInflater().inflate(R.layout.edit_item, root, false);
        final AlarmsPresenter.ViewContract.DialogContract dialogContract = (AlarmsPresenter.ViewContract.DialogContract) content;
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

    public static AlarmsPresenter getAlarmsPresenter() {
        return alarmsPresenter;
    }
}