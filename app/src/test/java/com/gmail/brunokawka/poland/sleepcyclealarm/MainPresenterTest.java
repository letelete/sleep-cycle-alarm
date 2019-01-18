package com.gmail.brunokawka.poland.sleepcyclealarm;

import android.os.Bundle;

import com.gmail.brunokawka.poland.sleepcyclealarm.app.base.MainContract;
import com.gmail.brunokawka.poland.sleepcyclealarm.app.base.MainPresenter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MainPresenterTest {

    @Mock
    private MainContract.MainView view;
    private MainContract.MainPresenter presenter;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        presenter = new MainPresenter(view);
    }

    @Test
    public void testIfVariablesInitialized() {
        assertThat(presenter, notNullValue());
    }

    @Test
    public void openSleepNowFragment() {
        int fragmentId = R.id.action_sleepnow;
        presenter.handleBottomNavigationTabClick(fragmentId);
        verify(view).openSleepNowFragment();
    }

    @Test
    public void openSleepNowFragmentAsDefault() {
        int unexpectedId = -666;
        presenter.handleBottomNavigationTabClick(unexpectedId);
        verify(view).openSleepNowFragment();
    }

    @Test
    public void openWakeUpAtFragment() {
        int fragmentId = R.id.action_wakeupat;
        presenter.handleBottomNavigationTabClick(fragmentId);
        verify(view).openWakeUpAtFragment();
    }

    @Test
    public void openAlarmsFragment() {
        int fragmentId = R.id.action_alarms;
        presenter.handleBottomNavigationTabClick(fragmentId);
        verify(view).openAlarmsFragment();
    }


    @Test
    public void testIfCanHandleButtonVisibility() {
        doNothing().when(view).hideWakeUpAtActionButton();

        presenter.handleBottomNavigationTabClick(R.id.action_wakeupat);
        verify(view, times(1)).showWakeUpAtActionButton();

        presenter.handleBottomNavigationTabClick(R.id.action_sleepnow);
        presenter.handleBottomNavigationTabClick(R.id.action_alarms);
        verify(view, times(2)).hideWakeUpAtActionButton();
    }

    @Test
    public void handleSingleBackPress() {
        presenter.handleBackPress();
        verify(view).showToastWithDoubleBackMessage();
        verify(view).countDownInMilliseconds(any(Integer.class));
        verify(view, never()).moveAppToBack();
    }

    @Test
    public void handleDoubleBackPress() {
        doubleTapBackButton();
        verify(view).moveAppToBack();
    }

    private void doubleTapBackButton() {
        presenter.handleBackPress();
        presenter.handleBackPress();
    }

    @Test
    public void showDefaultFragmentIfBundleIsNull() {
        Bundle savedInstanceState = null;
        presenter.setUpUi(savedInstanceState);
        verify(view).openDefaultFragment();
    }

    @Test
    public void showLatestFragmentIfBundleIsNotNull() {
        Bundle savedInstanceState = new Bundle();
        presenter.setUpUi(savedInstanceState);
        verify(view).openLatestFragment();
    }

    @Test
    public void openSettings() {
        int menuItemId = R.id.menu_settings;
        presenter.handleMenuItemClick(menuItemId);
        verify(view).openSettingsActivity();
    }
}