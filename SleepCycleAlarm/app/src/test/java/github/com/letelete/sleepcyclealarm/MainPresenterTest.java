package github.com.letelete.sleepcyclealarm;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

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

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new MainPresenter(view);
    }

    @Test
    public void handleBottomNavigationTabClick_NewPosition1PreviousPosition0_WakeUpAtTabAndAnimPair() {
        presenter.handleBottomNavigationTabClick(1, 0);
        ArgumentCaptor<WakeUpAtFragment> argument = ArgumentCaptor.forClass(WakeUpAtFragment.class);
        int[] correctAnimationPair = {R.animator.swipe_left_enter, R.animator.swipe_left_exit};

        Mockito.verify(view).navigateToSpecificFragmentWithAnimation(argument.capture(), Mockito.eq(correctAnimationPair));
    }

    @Test
    public void handleBottomNavigationTabClick_NewPosition0PreviousPosition1_SleepNowTabAndAnimPair() {
        presenter.handleBottomNavigationTabClick(0, 1);
        ArgumentCaptor<SleepNowFragment> argument = ArgumentCaptor.forClass(SleepNowFragment.class);
        int[] correctAnimationPair = {R.animator.swipe_right_enter, R.animator.swipe_right_exit};

        Mockito.verify(view).navigateToSpecificFragmentWithAnimation(argument.capture(), Mockito.eq(correctAnimationPair));
    }

    @Test
    public void handleBottomNavigationTabClick_NewPosition2PreviousPosition0_AlarmsTabAndAnimPair() {
        presenter.handleBottomNavigationTabClick(2, 0);
        ArgumentCaptor<AlarmsFragment> argument = ArgumentCaptor.forClass(AlarmsFragment.class);
        int[] correctAnimationPair = {R.animator.swipe_left_enter, R.animator.swipe_left_exit};

        Mockito.verify(view).navigateToSpecificFragmentWithAnimation(argument.capture(), Mockito.eq(correctAnimationPair));
    }
}