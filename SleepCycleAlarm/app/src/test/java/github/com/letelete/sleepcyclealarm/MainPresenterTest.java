package github.com.letelete.sleepcyclealarm;

import android.app.Fragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import github.com.letelete.sleepcyclealarm.ui.tabs.AlarmsFragment;
import github.com.letelete.sleepcyclealarm.ui.tabs.SleepNowFragment;
import github.com.letelete.sleepcyclealarm.ui.tabs.WakeUpAtFragment;

/*
    Naming convention:
    methodName_StateUnderTest_ExpectedBehavior
 */

public class MainPresenterTest {

    @Mock
    private MainContract.MvpView view;
    private MainPresenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new MainPresenter(view);
    }

    @Test
    public void handleBottomNavigationTabClick_Position0_SleepNowTab() {
        int tabPosition = 0;
        presenter.handleBottomNavigationTabClick(tabPosition);
        Mockito.verify(view).navigateToSleepNowTab();
    }

    @Test
    public void handleBottomNavigationTabClick_Position1_WakeUpAtTab() {
        int tabPosition = 1;
        presenter.handleBottomNavigationTabClick(tabPosition);
        Mockito.verify(view).navigateToWakeUpAtTab();
    }

    @Test
    public void handleBottomNavigationTabClick_Position2_AlarmsTab() {
        int tabPosition = 2;
        presenter.handleBottomNavigationTabClick(tabPosition);
        Mockito.verify(view).navigateToAlarmsTab();
    }

    @Test
    public void handleBottomNavigationTabClick_PositionOutOfSwitchRange_SleepNowTab() {
        int tabPosition = 999;
        presenter.handleBottomNavigationTabClick(tabPosition);
        Mockito.verify(view).navigateToSleepNowTab();
    }
}