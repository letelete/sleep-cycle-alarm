package github.com.letelete.sleepcyclealarm.ui.menu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import github.com.letelete.sleepcyclealarm.MainContract;

import static org.junit.Assert.*;

/*
    Naming convention:
    methodName_StateUnderTest_ExpectedBehavior
 */


public class MenuPresenterTest {

    @Mock
    private MenuContract.MenuView view;
    private MenuContract.MenuPresenter presenter;
    private Intent intent;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new MenuPresenter(view);
    }

    @Test
    public void handleCloseActivityButton() {
        presenter.handleCloseActivityButton();
        Mockito.verify(view).closeActivity();
    }
}