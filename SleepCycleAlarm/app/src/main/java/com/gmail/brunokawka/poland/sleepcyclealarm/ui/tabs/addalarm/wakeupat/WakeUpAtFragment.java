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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.brunokawka.poland.sleepcyclealarm.R;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.Item;
import com.gmail.brunokawka.poland.sleepcyclealarm.events.ItemsAmountChangedEvent;
import com.gmail.brunokawka.poland.sleepcyclealarm.events.WakeUpAtActionButtonClickedEvent;
import com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.ListAdapter;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemContentBuilder;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemsBuilder.ItemsBuilder;
import com.gmail.brunokawka.poland.sleepcyclealarm.utils.ItemsBuilder.WakeUpAtBuildingStrategy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WakeUpAtFragment extends Fragment
    implements WakeUpAtContract.WakeUpAtView {
    private static final String TAG = "WakeUpAtFragmentLog";

    private ItemsBuilder itemsBuilder;
    static WakeUpAtPresenter wakeUpAtPresenter;
    ArrayList<Item> items;
    AlertDialog dialog;

    private DateTime lastExecutionDate;
    private DateTime currentDate;

    @BindView(R.id.wakeUpAtRoot)
    ViewGroup root;

    @BindView(R.id.wakeUpAtListCardView)
    CardView listCardView;

    @BindView(R.id.wakeUpAtListHelper)
    TextView listHelper;

    @BindView(R.id.wakeUpAtFragmentRecycler)
    RecyclerView recycler;

    @BindView(R.id.wakeUpAtCardInfoTitle)
    TextView cardInfoTitle;

    @BindView(R.id.wakeUpAtCardInfoSummary)
    TextView cardInfoSummary;

    @BindView(R.id.wakeUpAtEmptyListPlaceHolder)
    ImageView emptyListPlaceHolder;

    @BindView(R.id.wakeUpAtInfoCardView)
    CardView cardInfo;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onItemsAmountChangedEvent(ItemsAmountChangedEvent itemsAmountChangedEvent) {
        int amount = itemsAmountChangedEvent.getItemsAmount();
        wakeUpAtPresenter.showOrHideElementsDependingOnAmountOfListItems(amount, lastExecutionDate);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWakeUpAtActivityButtonClicked(WakeUpAtActionButtonClickedEvent wakeUpAtActionButtonClickedEvent) {
        if (wakeUpAtPresenter != null) {
            wakeUpAtPresenter.handleFloatingActionButtonClicked();
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
                        wakeUpAtPresenter.tryToGenerateAListWithGivenValues(dialogContract, currentDate, lastExecutionDate);
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
        Log.d(TAG, "Saving execution date to preferences");
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
        items = itemsBuilder.getItems(currentDate, lastExecutionDate);
        recycler.setAdapter(new ListAdapter(items, recycler));
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
    }
}
