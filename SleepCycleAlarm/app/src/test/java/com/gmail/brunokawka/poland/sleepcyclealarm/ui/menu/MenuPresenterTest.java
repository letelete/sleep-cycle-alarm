package com.gmail.brunokawka.poland.sleepcyclealarm.ui.menu;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/*
    Naming convention:
    methodName_StateUnderTest_ExpectedBehavior
 */


public class MenuPresenterTest {

    @Mock
    private MenuContract.MenuView view;
    private MenuContract.MenuPresenter presenter;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new MenuPresenter(view);
    }

    @Test
    public void handleCloseActivityButton_buttonPressed_closeActivity() {
        presenter.handleCloseActivityButton();
        Mockito.verify(view).closeActivity();
    }
}