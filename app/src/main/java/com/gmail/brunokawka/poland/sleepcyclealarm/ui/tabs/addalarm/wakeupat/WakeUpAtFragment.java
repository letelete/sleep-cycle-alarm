package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.wakeupat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.alarm.AlarmController;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.AlarmDAO;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Item;
import com.gmail.brunokawka.poland.sleepcyclealarm.events.AmountOfItemsChangedEvent;
import com.gmail.brunokawka.poland.sleepcyclealarm.events.ItemInListClickedEvent;
import com.gmail.brunokawka.poland.sleepcyclealarm.events.SetHourButtonClickedEvent;
import com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.ListAdapter;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemContentBuilder;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.VisibilityHandler;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.itemsbuilder.ItemsBuilder;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.itemsbuilder.WakeUpAtBuildingStrategy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WakeUpAtFragment extends Fragment
    implements WakeUpAtContract.WakeUpAtView {

    private ItemsBuilder itemsBuilder;
    private AlarmDAO alarmDAO;
    private static WakeUpAtPresenter wakeUpAtPresenter;
    private AlertDialog dialog;
    private DateTime lastExecutionDate;
    private DateTime currentDate;

    @BindView(R.id.wakeUpAtRoot)
    protected ViewGroup root;

    @BindView(R.id.wakeUpAtListCardView)
    protected CardView listCardView;

    @BindView(R.id.wakeUpAtListHelper)
    protected TextView listHelper;

    @BindView(R.id.wakeUpAtFragmentRecycler)
    protected RecyclerView recycler;

    @BindView(R.id.wakeUpAtCardInfoTitle)
    protected TextView cardInfoTitle;

    @BindView(R.id.wakeUpAtCardInfoSummary)
    protected TextView cardInfoSummary;

    @BindView(R.id.wakeUpAtEmptyListImage)
    protected View emptyListPlaceHolder;

    @BindView(R.id.wakeUpAtInfoCardView)
    protected CardView cardInfo;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onItemsAmountChangedEvent(AmountOfItemsChangedEvent amountOfItemsChangedEvent) {
        if (wakeUpAtPresenter != null) {
            int amount = amountOfItemsChangedEvent.getItemsAmount();
            wakeUpAtPresenter.showOrHideElementsDependingOnAmountOfListItems(amount, lastExecutionDate);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWakeUpAtActivityButtonClicked(SetHourButtonClickedEvent setHourButtonClickedEvent) {
        if (wakeUpAtPresenter != null) {
            wakeUpAtPresenter.handleFloatingActionButtonClicked();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSetAlarmEvent(ItemInListClickedEvent itemInListClickedEvent) {
        if (alarmDAO != null) {
            alarmDAO.generateAlarmAndSaveItToRealm(itemInListClickedEvent.getItem());
            new AlarmController(getActivity()).rescheduleAlarms();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_wake_up_at, container, false);
        ButterKnife.bind(this, view);

        wakeUpAtPresenter = new WakeUpAtPresenter();
        wakeUpAtPresenter.bindView(this);

        itemsBuilder = new ItemsBuilder();
        itemsBuilder.setBuildingStrategy(new WakeUpAtBuildingStrategy());

        alarmDAO = new AlarmDAO();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        wakeUpAtPresenter.setUpEnvironment();
        wakeUpAtPresenter.setUpUIElements(lastExecutionDate);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void updateCurrentDate() {
        currentDate = DateTime.now();
    }

    @Override
    public void setUpRecycler() {
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void showSetTimeDialog() {
        final View content = getLayoutInflater().inflate(R.layout.dialog_set_hour_to_wake_up_at, root, false);
        final WakeUpAtContract.WakeUpAtView.DialogContract dialogContract = (WakeUpAtContract.WakeUpAtView.DialogContract) content;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(content)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        DateTime newChosenExecutionDate = dialogContract.getDateTime();
                        wakeUpAtPresenter.tryToGenerateAListWithGivenValues(newChosenExecutionDate, currentDate, lastExecutionDate);
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
    public void setLastExecutionDate(DateTime newDate) {
        lastExecutionDate = newDate;
    }

    @Override
    public void saveExecutionDateToPreferencesAsString() {
        Log.d(getClass().getName(), "Saving execution date to preferences");
        SharedPreferences pref = getActivity().getSharedPreferences(getString(R.string.wakeupat_preferences_name), Context.MODE_PRIVATE);
        pref.edit()
                .putString(getString(R.string.key_last_execution_date), lastExecutionDate.toString())
                .apply();
    }

    @Override
    public void setLastExecutionDateFromPreferences() {
        SharedPreferences pref = getActivity().getSharedPreferences(getString(R.string.wakeupat_preferences_name), Context.MODE_PRIVATE);
        if (pref.contains(getString(R.string.key_last_execution_date))) {
            String notFormattedDate = pref.getString(getString(R.string.key_last_execution_date), null);
            if (!TextUtils.isEmpty(notFormattedDate)) {
                lastExecutionDate = DateTime.parse(notFormattedDate);
                wakeUpAtPresenter.showWakeUpAtElements(lastExecutionDate);
            }
        }
    }

    @Override
    public void updateCardInfoTitle() {
        String title = getString(R.string.wake_up_at_card_info_title_when_user_defined_hour);
        String titleFormatted = String.format(title, ItemContentBuilder.getTitle(lastExecutionDate));
        cardInfoTitle.setText(titleFormatted);
    }

    @Override
    public void updateCardInfoSummary() {
        String summary = getString(R.string.wake_up_at_card_info_summary_when_user_defined_hour);
        cardInfoSummary.setText(summary);
    }

    @Override
    public void setUpAdapterAndCheckForContentUpdate() {
        List<Item> items = itemsBuilder.getItems(currentDate, lastExecutionDate);
        recycler.setAdapter(new ListAdapter(items));
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
    public void showCardInfo() {
        VisibilityHandler.showIfNotVisible(cardInfo);
    }

    @Override
    public void hideCardInfo() {
        VisibilityHandler.hideIfNotGone(cardInfo);
    }

    @Override
    public void showEmptyListHint() {
        // TODO: show some fancy image (issue #3) - github.com/letelete/Sleep-Cycle-Alarm/issues/3 (I've created some temporary image for now)
        VisibilityHandler.showIfNotVisible(emptyListPlaceHolder);
    }

    @Override
    public void hideEmptyListHint() {
        // TODO: show some fancy image (issue #3) - github.com/letelete/Sleep-Cycle-Alarm/issues/3 (I've created some temporary image for now)
        VisibilityHandler.hideIfNotGone(emptyListPlaceHolder);
    }

    @Override
    public void showToast(DateTime definedHour) {
        Toast.makeText(getActivity(), "Couldn't generate a list with given hour("+ ItemContentBuilder.getTitle(definedHour) + ") There will be displayed the nearest hour to defined...", Toast.LENGTH_LONG).show();
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

        if (alarmDAO != null) {
            alarmDAO = null;
        }
    }
}
