package github.com.letelete.sleepcyclealarm.sleepnow;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.utils.v4.Bundler;

import github.com.letelete.sleepcyclealarm.R;

public class SleepNowFragment extends android.support.v4.app.Fragment {

    private static final String KEY_PARAM = "page_param";

    public static SleepNowFragment newInstance(String param) {
        SleepNowFragment f = new SleepNowFragment();
        f.setArguments(arguments(param));
        return f;
    }

    public static Bundle arguments(String param) {
        return new Bundler()
                .putString(KEY_PARAM, param)
                .get();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sleep_now_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String param = getArguments().getString(KEY_PARAM);
        Log.i("Switched to", param);
    }
}
