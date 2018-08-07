package github.com.letelete.sleepcyclealarm.ui.menu;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import github.com.letelete.sleepcyclealarm.R;
import github.com.letelete.sleepcyclealarm.model.preferences.SettingsFragment;

public class MenuActivity extends AppCompatActivity {

    private final static String TAG = "MenuActivity";
    private final static int WRONG_KEY_ERROR_CODE = -1;

    private Fragment fragment = null;
    private PreferenceFragmentCompat preferenceFragment = null;

    private SharedPreferences sharedPreferences;
    private TextView activityTitle;

    private int MENU_ITEM_KEY;
    private String MENU_ITEM_TITLE;

    @Override
    protected void onCreate(Bundle savedStateInstance) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        setTheme(getThemeForActivity());

        super.onCreate(savedStateInstance);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();

        this.activityTitle = findViewById(R.id.activityTitleTextView);
        this.MENU_ITEM_KEY = intent.getIntExtra(getResources().getString(R.string.key_menu_item_id), WRONG_KEY_ERROR_CODE);
        this.MENU_ITEM_TITLE = intent.getStringExtra(getResources().getString(R.string.key_menu_item_title));

        setActivityTitle(MENU_ITEM_TITLE);
        performActionDependingOnKey(savedStateInstance);
    }

    private int getThemeForActivity() {
        return sharedPreferences.getBoolean(getResources().getString(R.string.key_change_theme), false)
                ? R.style.Theme_DarkTheme
                : R.style.Theme_LightTheme;
    }

    private void setActivityTitle(String title) {
        if(!TextUtils.isEmpty(title)) {
            activityTitle.setText(title);
        } else {
            Log.wtf(TAG,"ActivityTitle is empty or null");
        }
    }

    private void performActionDependingOnKey(Bundle savedStateInstance) {
        switch (MENU_ITEM_KEY) {
            case WRONG_KEY_ERROR_CODE:
                Log.wtf(TAG, "Default value assigned to the key");
                showErrorAndFinish(R.string.error_menu_activity_key_use_default_value);

            case R.id.menu_settings:
                String settingsTag = getResources().getString(R.string.menu_settings);
                if(savedStateInstance != null) {
                    preferenceFragment = (SettingsFragment) getSupportFragmentManager().findFragmentByTag(settingsTag);
                } else {
                    preferenceFragment = new SettingsFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.menu_activity_container, preferenceFragment, settingsTag).commit();
                }
                break;

            default:
                Log.wtf(TAG, "Default case in switch terminated. Key value: " + MENU_ITEM_KEY);
                showErrorAndFinish(R.string.error_menu_activity_default_case_in_switch);
        }
    }



    private void showErrorAndFinish(int resourceMsgReference) {
        String errorMsg = getResources().getString(resourceMsgReference);
        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
        finish();
    }

    public void onCloseActivityButtonClick(View view) {
        Log.i(TAG, "User close an activity");
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Activity destroyed");
    }
}
