package github.com.letelete.sleepcyclealarm.model.preferences;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.MissingResourceException;

import github.com.letelete.sleepcyclealarm.R;

public class MenuActivity extends AppCompatActivity {

    private final static int WRONG_KEY_ERROR_CODE = -1;

    private Fragment fragment;
    private PreferenceFragmentCompat preferenceFragment;

    private TextView activityTitle;

    @Override
    protected void onCreate(Bundle savedStateInstance) {
        super.onCreate(savedStateInstance);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        int ID_KEY = intent.getIntExtra(getResources().getString(R.string.MENU_ITEM_ID_KEY), WRONG_KEY_ERROR_CODE);
        String ITEM_TITLE = intent.getStringExtra(getResources().getString(R.string.MENU_ITEM_TITLE_KEY));

        activityTitle = findViewById(R.id.activityTitleTextView);
        setActivityTitle(ITEM_TITLE);

        switch (ID_KEY) {
            case -1:
                Log.wtf("MenuActivityLog", "Default value assigned to the key");
                showErrorAndFinish(R.string.error_menu_activity_key_use_default_value);

            case R.id.menu_settings:
                preferenceFragment = new SettingsFragment();
                break;

            default:
                Log.wtf("MenuActivityLog", "Default case in switch terminated. Key value: " + ID_KEY);
                showErrorAndFinish(R.string.error_menu_activity_default_case_in_switch);
        }

        if(isAnyFragmentDeclared()) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if(preferenceFragment != null) {
                ft.add(R.id.menu_activity_container, preferenceFragment);
            } else if (fragment != null) {
                ft.add(R.id.menu_activity_container, fragment);
            }

            ft.commit();
        }
    }

    private void setActivityTitle(String title) {
        if(!TextUtils.isEmpty(title)) {
            activityTitle.setText(title);
        } else {
            Log.wtf("MenuActivityLog",  "ActivityTitle is empty or null");
        }
    }

    private boolean isAnyFragmentDeclared() {
        return preferenceFragment != null || fragment != null;
    }

    private void showErrorAndFinish(int resourceMsgReference) {
        try {
            String errorMsg = getResources().getString(resourceMsgReference);
            Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
        } catch (MissingResourceException e) {
            e.fillInStackTrace();
        } finally {
            closeActivity();
        }
    }

    public void onCloseActivityButtonClick(View view) {
        Log.wtf("MenuActivityLog", "User close an activity");
        closeActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("MenuActivityLog", "Activity destroyed");
        closeActivity();
    }

    private void closeActivity() {
        finish();
    }
}
