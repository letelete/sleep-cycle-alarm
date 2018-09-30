package com.gmail.brunokawka.poland.sleepcyclealarm;

import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.accessalarm.alarms.AlarmsFragment;
import com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.sleepnow.SleepNowFragment;
import com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm.wakeupat.WakeUpAtFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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
        MockitoAnnotations.initMocks(this);
        presenter = new MainPresenter(view);
    }

    @Test
    public void testIfVariablesInitialized() {
        assertThat(presenter, notNullValue());
    }

    @Test
    public void testIfCanHandleBottomNavigationTabClickAndReturnCorrectFragment() {
        ArgumentCaptor fragmentCaptor = ArgumentCaptor.forClass(Fragment.class);
        doNothing().when(view).replaceFragment((Fragment) fragmentCaptor.capture());

        presenter.handleBottomNavigationTabClick(R.id.action_sleepnow);
        assertThat(SleepNowFragment.class.getSimpleName(), is(fragmentCaptor.getValue().getClass().getSimpleName()));

        presenter.handleBottomNavigationTabClick(R.id.action_wakeupat);
        assertThat(WakeUpAtFragment.class.getSimpleName(), is(fragmentCaptor.getValue().getClass().getSimpleName()));

        presenter.handleBottomNavigationTabClick(R.id.action_alarms);
        assertThat(AlarmsFragment.class.getSimpleName(), is(fragmentCaptor.getValue().getClass().getSimpleName()));

        presenter.handleBottomNavigationTabClick(-9999);
        Fragment defaultFragment = new SleepNowFragment();
        assertThat(defaultFragment.getClass().getSimpleName(), is(fragmentCaptor.getValue().getClass().getSimpleName()));
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
    public void testIfCanHandleSingleAndDoubleBackPressClick() {
        presenter.handleBackPress();
        verify(view).showToastWithDoubleBackMessage();
        verify(view).countDownInMillisecondsAndEmitSignalBackAtTheEnd(any(Integer.class));
        presenter.handleBackPress();
        verify(view).moveAppToBack();
    }
}