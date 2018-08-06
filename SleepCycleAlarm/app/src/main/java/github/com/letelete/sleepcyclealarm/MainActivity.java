package github.com.letelete.sleepcyclealarm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import github.com.letelete.sleepcyclealarm.model.preferences.MenuActivity;

public class MainActivity extends AppCompatActivity
    implements BottomNavigationBar.OnTabSelectedListener,
        Toolbar.OnMenuItemClickListener {

    private SharedPreferences sharedPreferences;

    private String menuItemIdKey;
    private String menuItemTitleKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        setAppTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuItemIdKey = getResources().getString(R.string.MENU_ITEM_ID_KEY);
        menuItemTitleKey = getResources().getString(R.string.MENU_ITEM_TITLE_KEY);

        setupToolbar();
        setupBottomNavigationBar();
    }

    private void setAppTheme() {
        int themeId = isDarkThemeOn()
                ? R.style.Theme_DarkTheme
                : R.style.Theme_LightTheme;
        setTheme(themeId);
    }

    private void setupToolbar() {
        Toolbar appToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(appToolbar);
        appToolbar.setOnMenuItemClickListener(this);
    }

    private void setupBottomNavigationBar() {
        BottomNavigationBar bottomNavigationBar = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_home, getResources().getString(R.string.sleep_now_tab)))
                .addItem(new BottomNavigationItem(R.drawable.ic_watch, getResources().getString(R.string.wake_up_at_tab)))
                .addItem(new BottomNavigationItem(R.drawable.ic_access_alarm, getResources().getString(R.string.alarms_tab)))
                .setBarBackgroundColor(getBottomBarBackgroundColor())
                .initialise();
        bottomNavigationBar.setTabSelectedListener(this);
    }

    private int getBottomBarBackgroundColor() {
        return isDarkThemeOn()
                ? R.color.dark_theme_color_primary
                : R.color.light_theme_color_primary;
    }

    @Override
    public void onTabSelected(int position) {

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.three_dot_menu, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        String itemTittle = item.getTitle().toString();

        openMenuActivityWithArguments(id, itemTittle);

        return true;
    }

    private void openMenuActivityWithArguments(int itemId, String itemTitle) {
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        intent.putExtra(menuItemIdKey, itemId);
        intent.putExtra(menuItemTitleKey, itemTitle);
        startActivity(intent);
    }

    private boolean isDarkThemeOn() {
        return sharedPreferences.getBoolean(getStringByResource(R.string.key_change_theme), false);
    }

    private String getStringByResource(int resource) {
        return getResources().getString(resource);
    }
}

