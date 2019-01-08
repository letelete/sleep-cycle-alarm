package com.gmail.brunokawka.poland.sleepcyclealarm.tabs.activealarms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
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
import com.gmail.brunokawka.poland.sleepcyclealarm.app.RealmManager;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Alarm;
import com.gmail.brunokawka.poland.sleepcyclealarm.events.RealmChangeEvent;
import com.gmail.brunokawka.poland.sleepcyclealarm.tabs.adapters.ActiveAlarmsAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class AlarmsFragment extends Fragment
    implements AlarmsContract.AlarmsView {

    private static final int RINGTONE_INTENT_REQUEST_CODE = 2137;

    @BindView(R.id.alarmsList) protected RecyclerView recycler;
    @BindView(R.id.alarmsListCardView) protected CardView listCardView;
    @BindView(R.id.alarmsEmptyListPlaceHolder) protected View emptyListPlaceHolder;
    @BindView(R.id.alarmsInfoCardView) protected CardView infoCard;

    private AlarmScopeListener alarmScopeListener;
    private static AlarmsPresenter alarmsPresenter;
    private AlertDialog dialog;
    private AlarmsContract.AlarmsView.DialogContract dialogContract;


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRealmChanged(RealmChangeEvent realmChangeEvent) {
        Log.d(getClass().getName(), "Realm change event received.");
        if (alarmsPresenter != null) {
            Log.d(getClass().getName(), "Handling realm change...");
            alarmsPresenter.handleRealmChange();
        } else {
            Log.d(getClass().getName(),
                    "Couldn't handle realm change. AlarmsPresenter is null.");
        }
    }

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
        RealmManager.incrementCount();

        addScopeListener();
        alarmsPresenter = alarmScopeListener.getPresenter();
        alarmsPresenter.bindView(this);

        alarmsPresenter.setUpUi();
    }

    @Override
    public void setUpRecycler() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);

    }

    @Override
    public void setUpAdapter() {
        Realm realm = RealmManager.getRealm();
        recycler.setAdapter(new ActiveAlarmsAdapter(realm
                .where(Alarm.class).findAllAsync().sort("executionDate")));
    }

    @Override
    public void showEditAlarmDialog(final Alarm alarm) {
        if(!alarm.isValid()) {
            return;
        }

        final View content = getLayoutInflater().inflate(R.layout.dialog_edit_item, null);
        dialogContract = (AlarmsContract.AlarmsView.DialogContract) content;
        dialogContract.bind(alarm);
        dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.setView(content);

        content.findViewById(R.id.alarmsEditRingtoneClickable)
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
                alarmsPresenter.updateEditedAlarm(dialogContract, alarm);
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

    private void startRingtonePickerActivityForResult() {
        Uri selectedRingtoneUri = Uri.parse(dialogContract.getRingtone());
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, getString(R.string.pref_ringtone_select_title));
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
                Log.d(getClass().getName(), "onActivityResult ringtone:" + chosenRingtone);
            }
        }
    }

    @Override
    public void showList() {
        if (listCardView.getVisibility() != View.VISIBLE) {
            listCardView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideList() {
        if (listCardView.getVisibility() != View.GONE) {
            listCardView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showInfoCard() {
        if (infoCard.getVisibility() != View.VISIBLE) {
            infoCard.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideInfoCard() {
        if (infoCard.getVisibility() != View.GONE) {
            infoCard.setVisibility(View.GONE);
        }
    }

    @Override
    public void showEmptyListHint() {
        if (emptyListPlaceHolder.getVisibility() != View.VISIBLE) {
            emptyListPlaceHolder.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideEmptyListHint() {
        if (emptyListPlaceHolder.getVisibility() != View.GONE) {
            emptyListPlaceHolder.setVisibility(View.GONE);
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