package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.wakeupat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.Item;
import com.gmail.brunokawka.poland.sleepcyclealarm.events.ItemsAmountChangedEvent;
import com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.ListAdapter;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemContentBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WakeUpAtFragment extends Fragment
    implements WakeUpAtPresenter.ViewContract {
    private static final String TAG = "WakeUpAtFragmentLog";

    private DateTime lastExecutionDate;
    private DateTime currentDate;

    ArrayList<Item> items;
    AlertDialog dialog;

    static WakeUpAtPresenter wakeUpAtPresenter;

    @BindView(R.id.wakeUpAtRoot)
    ViewGroup root;

    @BindView(R.id.wakeUpAtListCardView)
    CardView listCardView;

    @BindView(R.id.wakeUpAtListHelper)
    TextView listHelper;

    @BindView(R.id.wakeUpAtFragmentRecycler)
    RecyclerView recycler;

    @BindView(R.id.wakeUpAtFloatingActionButtonExtended)
    Button floatingActionButtonExtended;

    @BindView(R.id.wakeUpAtCardInfoTitle)
    TextView cardInfoTitle;

    @BindView(R.id.wakeUpAtCardInfoSummary)
    TextView cardInfoSummary;

    @BindView(R.id.wakeUpAtEmptyListPlaceHolder)
    ImageView emptyListPlaceHolder;

    @BindView(R.id.wakeUpAtInfoCardView)
    CardView cardInfo;

    @OnClick(R.id.wakeUpAtFloatingActionButtonExtended)
    public void onFloatingActionButtonClick() {
        if (wakeUpAtPresenter != null) {
            updateCurrentDate();
            wakeUpAtPresenter.showTimeDialog();
        }
    }

    private void updateCurrentDate() {
        currentDate = DateTime.now();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onItemsAmountChangedEvent(ItemsAmountChangedEvent itemsAmountChangedEvent) {
        int amount = itemsAmountChangedEvent.getItemsAmount();
        if (amount <= 0) {
            wakeUpAtPresenter.hideWakeUpAtElements();
        } else {
            wakeUpAtPresenter.showWakeUpAtElements();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_wake_up_at, container, false);
        ButterKnife.bind(this, view);

        wakeUpAtPresenter = new WakeUpAtPresenter();
        wakeUpAtPresenter.bindView(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        updateCurrentDate();
        setLastExecutionDateFromPreferences();
        setUpRecycler();

        if (lastExecutionDate == null) {
            wakeUpAtPresenter.hideWakeUpAtElements();
        } else {
            setUpAdapterAndCheckForContentUpdate();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (wakeUpAtPresenter != null) {
            wakeUpAtPresenter.unbindView();
        }

        if (dialog != null) {
            dialog.dismiss();
        }

        if (lastExecutionDate != null) {
            lastExecutionDate = null;
        }
    }

    private void setUpRecycler() {
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void showSetTimeDialog() {
        final View content = getLayoutInflater().inflate(R.layout.dialog_set_hour_to_wake_up_at, root, false);
        final DialogContract dialogContract = (DialogContract) content;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(content)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        wakeUpAtPresenter.generateList(dialogContract);
                        wakeUpAtPresenter.dismissTimeDialog();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        wakeUpAtPresenter.dismissTimeDialog();
                        dialog.dismiss();
                    }
                });
        dialog = dialogBuilder.create();
        dialog.show();
    }

    @Override
    public void generateList(DateTime executionDate) {
        if (executionDate != null) {
            if (isPossibleToCreateAlarm(executionDate)) {
                wakeUpAtPresenter.showWakeUpAtElements();
                updateLastExecutionDate(executionDate);
                setUpAdapterAndCheckForContentUpdate();
            } else {
                Log.e(TAG, "Its not possible to create a next alarm");
                // TODO:
                // THIS SOLUTION IS JUST TEMPORARY
                wakeUpAtPresenter.hideWakeUpAtElements();
                // SOLUTION BELOW IS A CORRECT ONE (see issue #6 - github.com/letelete/Sleep-Cycle-Alarm/issues/6)
                // wakeUpAtPresenter.showTheClosestAlarmToDefinedHour(executionDate);
            }
        } else {
            Log.e(TAG, "executionDate is null");
        }
    }

    private void updateLastExecutionDate(DateTime newDate) {
        if (lastExecutionDate == null || lastExecutionDate != newDate) {
            lastExecutionDate = newDate;
            saveExecutionDateToPreferencesAsString();
        }
    }

    private boolean isPossibleToCreateAlarm(DateTime executionDate) {

        return WakeUpAtItemsBuilder.isPossibleToCreateNextItem(currentDate, executionDate);
    }

    @Override
    public void updateCardInfoContent() {
        if (lastExecutionDate != null) {
            updateCardInfoTitle();
            updateCardInfoSummary();
        } else {
            Log.d(TAG, "lastExecutionDate is null");
        }
    }

    private void updateCardInfoTitle() {
        String title = getString(R.string.wake_up_at_card_info_title_when_user_defined_hour);
        String titleFormatted = String.format(title, ItemContentBuilder.getTitle(lastExecutionDate));
        cardInfoTitle.setText(titleFormatted);
    }

    private void updateCardInfoSummary() {
        String summary = getString(R.string.wake_up_at_card_info_summary_when_user_defined_hour);
        cardInfoSummary.setText(summary);
    }

    private void setUpAdapterAndCheckForContentUpdate() {
        if (lastExecutionDate != null) {
            items = WakeUpAtItemsBuilder.getItemsForExecutionDate(currentDate, lastExecutionDate);
            recycler.setAdapter(new ListAdapter(items, recycler));
        } else {
            Log.d(TAG, "lastExecutionDate is null. At setUpAdapterAndCheckForContentUpdate()");
        }
    }

    private void saveExecutionDateToPreferencesAsString() {
        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit()
                .putString(getString(R.string.key_last_execution_date), lastExecutionDate.toString())
                .apply();
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
    public void showCardInfo() {
        if (cardInfo.getVisibility() != View.VISIBLE) {
            cardInfo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideCardInfo() {
        if (cardInfo.getVisibility() != View.GONE) {
            cardInfo.setVisibility(View.GONE);
        }
    }

    @Override
    public void showEmptyListHint() {
        // TODO: show some fancy image (issue #3) - github.com/letelete/Sleep-Cycle-Alarm/issues/3 (I've created some temporary image for now)
        if (emptyListPlaceHolder.getVisibility() != View.VISIBLE) {
            emptyListPlaceHolder.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideEmptyListHint() {
        // TODO: show some fancy image (issue #3) - github.com/letelete/Sleep-Cycle-Alarm/issues/3 (I've created some temporary image for now)
        if (emptyListPlaceHolder.getVisibility() != View.GONE) {
            emptyListPlaceHolder.setVisibility(View.GONE);
        }
    }

    private void setLastExecutionDateFromPreferences() {
        lastExecutionDate = getExecutionDateFromPreferences();
    }

    private DateTime getExecutionDateFromPreferences() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (pref.contains(getString(R.string.key_last_execution_date))) {
            return DateTime.parse(pref.getString(getString(R.string.key_last_execution_date), null));
        }

        return null;
    }


}
