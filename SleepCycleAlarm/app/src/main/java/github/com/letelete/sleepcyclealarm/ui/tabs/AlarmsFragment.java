package github.com.letelete.sleepcyclealarm.ui.tabs;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import github.com.letelete.sleepcyclealarm.R;

public class AlarmsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return layoutInflater.inflate(R.layout.fragment_alarms, container, false);
    }
}
