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

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;

public class AlarmsFragment extends Fragment
    implements AlarmsContract.AlarmsView {

    private Item item;
    private AlarmScopeListener alarmScopeListener;
    private static AlarmsPresenter alarmsPresenter;
    private AlertDialog dialog;

    @BindView(R.id.alarms_root)
    protected ViewGroup root;

    @BindView(R.id.alarmsList)
    protected RecyclerView recycler;

    @BindView(R.id.alarmsListCardView)
    protected CardView listCardView;

    @BindView(R.id.alarmsEmptyListPlaceHolder)
    protected View emptyListPlaceHolder;

    @BindView(R.id.alarmsInfoCardView)
    protected CardView infoCard;

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

        alarmsPresenter.setUpUi();
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
        recycler.setAdapter(new AlarmsAdapter(realm.where(Alarm.class).findAllAsync().sort("executionDate")));
    }

    @Override
    public void showEditAlarmDialog(final Alarm alarm) {
        if(!alarm.isValid()) {
            return;
        }

        final View content = getLayoutInflater().inflate(R.layout.dialog_edit_item, root, false);
        final AlarmsContract.AlarmsView.DialogContract dialogContract = (AlarmsContract.AlarmsView.DialogContract) content;
        dialogContract.bind(alarm);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(content)
                .setTitle(getString(R.string.dialog_edit_alarm))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alarmsPresenter.updateEditedAlarm(dialogContract, alarm);
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
        Log.d(getClass().getName(), "Realm change event received.");
        if (alarmsPresenter != null) {
            Log.d(getClass().getName(), "Handling realm change...");
            alarmsPresenter.handleRealmChange();
        } else {
            Log.d(getClass().getName(), "Couldn't handle realm change. AlarmsPresenter is null.");
        }
    }
}