package com.gmail.brunokawka.poland.sleepcyclealarm.tabs.addalarm;

import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Item;

public interface AddAlarmContract {

    public void bind(Item item);

    String getRingtone();

    void setRingtone(String ringtone);

    void setAlarmExecutionHour(String alarmExecutionHour);

    void setAlarmHealthStatus(String alarmHealthStatus);

}
