package github.com.letelete.sleepcyclealarm.sleepnow;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.utils.v4.Bundler;

import github.com.letelete.sleepcyclealarm.R;

public class SleepNowFragment extends Fragment {

    private final static String KEY_PARAM = "sleep_now";

    public static SleepNowFragment newInstance(String params) {
        SleepNowFragment f = new SleepNowFragment();
        f.setArguments(arguments(params));
        return f;
    }

    public static Bundle arguments(String params) {
        return new Bundler()
                .putString(KEY_PARAM, params)
                .get();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return layoutInflater.inflate(R.layout.fragment_sleep_now_tab, container, false);
    }
}
