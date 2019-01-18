package com.gmail.brunokawka.poland.sleepcyclealarm.tabs.activealarms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.app.RealmManager;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.AlarmDAO;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Alarm;
import com.gmail.brunokawka.poland.sleepcyclealarm.schedule.AlarmController;
import com.gmail.brunokawka.poland.sleepcyclealarm.tabs.adapters.ActiveAlarmsAdapter;
import com.gmail.brunokawka.poland.sleepcyclealarm.tabs.ui.EmptyStateRecyclerView;
import com.gmail.brunokawka.poland.sleepcyclealarm.tabs.ui.dialog.ItemDialogContract;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class AlarmsFragment extends Fragment
    implements AlarmsContract.AlarmsView {

    private static final int RINGTONE_INTENT_REQUEST_CODE = 2137;

    private static AlarmsPresenter alarmsPresenter;

    private AlarmScopeListener alarmScopeListener;
    private AlertDialog dialog;
    private AlarmController alarmController;
    private AlarmDAO alarmDAO;
    private ItemDialogContract dialogContract;

    @BindView(R.id.rv_alarms)
    EmptyStateRecyclerView recycler;

    @BindView(R.id.i_alarms_empty_state)
    View vEmptyView;

    @BindView(R.id.tv_alarms_list_hint)
    TextView tvListHint;

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = layoutInflater.inflate(R.layout.fragment_alarms, container, false);
        ButterKnife.bind(this, view);
        alarmController = new AlarmController(view.getContext());
        alarmDAO = new AlarmDAO();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RealmManager.incrementCount();

        addScopeListener();
        alarmsPresenter = alarmScopeListener.getPresenter();
        alarmsPresenter.bindView(this);

        setupUi();
    }

    private void setupUi() {
        setupRecycler();
    }

    public void setupRecycler() {
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupAdapter();
        recycler.setEmptyView(vEmptyView,
                R.drawable.ic_empty_alarms_list,
                R.string.alarms_empty_list_title,
                R.string.alarms_empty_list_summary);
        recycler.addViewToHideIfListEmpty(tvListHint);
    }

    @Override
    public void setupAdapter() {
        Realm realm = RealmManager.getRealm();
        recycler.setAdapter(new ActiveAlarmsAdapter(realm
                .where(Alarm.class).findAllAsync().sort("executionDate")));
    }

    @Override
    public void showEditAlarmDialog(final Alarm alarm) {
        if(!alarm.isValid()) {
            return;
        }

        String positiveButtonText = getString(R.string.save);
        String negativeButtonText = getString(R.string.cancel);
        final View content = getLayoutInflater().inflate(R.layout.dialog_item_ringtone, null);

        dialogContract = (ItemDialogContract) content;
        dialogContract.bind(alarm);
        AlertDialog.Builder builder
                = new AlertDialog.Builder(getActivity(), R.style.Theme_SleepCycleAlarm_Dialog);
        builder.setView(content)
            .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    createNewAlarm();
                    dialog.dismiss();
                }

                private void createNewAlarm() {
                    alarmController.cancelAlarm(alarm);
                    String newRingtone = dialogContract.getRingtone();

                    Alarm newAlarm = new Alarm();
                    newAlarm.setId(alarm.getId());
                    newAlarm.setTitle(alarm.getTitle());
                    newAlarm.setSummary(alarm.getSummary());
                    newAlarm.setCurrentDate(alarm.getCurrentDate());
                    newAlarm.setExecutionDate(alarm.getExecutionDate());
                    newAlarm.setRingtone(newRingtone);
                    alarmDAO.saveEvenIfDuplicate(newAlarm);
                    alarmController.setAlarm(newAlarm);
                }
            })
            .setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialog.dismiss();
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

    @Override
    public void onDestroyView() {
        if(alarmsPresenter != null) {
            alarmsPresenter.unbindView();
        }

        if(dialog != null) {
            dialog.dismiss();
        }

        alarmDAO.cleanUp();
        RealmManager.decrementCount();
        super.onDestroyView();
    }

    public void addScopeListener() {
        alarmScopeListener = (AlarmScopeListener) getFragmentManager()
                .findFragmentByTag("SCOPE_LISTENER");
        if (alarmScopeListener == null) {
            alarmScopeListener = new AlarmScopeListener();
            getFragmentManager()
                    .beginTransaction().add(alarmScopeListener, "SCOPE_LISTENER").commit();
        }
    }

    public static AlarmsPresenter getAlarmsPresenter() {
        return alarmsPresenter;
    }
}