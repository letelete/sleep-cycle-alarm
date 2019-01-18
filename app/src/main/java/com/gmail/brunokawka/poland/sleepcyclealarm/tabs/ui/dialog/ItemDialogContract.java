package com.gmail.brunokawka.poland.sleepcyclealarm.tabs.ui.dialog;

import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Alarm;
import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Item;

public interface ItemDialogContract {

    void bind(Item item);

    void bind(Alarm alarm);

    void setRingtone(String itemRingtone);

    String getRingtone();
}
