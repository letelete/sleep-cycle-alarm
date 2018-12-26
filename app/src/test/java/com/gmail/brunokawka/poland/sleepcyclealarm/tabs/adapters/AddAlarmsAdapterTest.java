package com.gmail.brunokawka.poland.sleepcyclealarm.tabs.adapters;

import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Item;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class AddAlarmsAdapterTest {

    @Test
    public void testIfCanReturnItemCount() {
        Item item = new Item();

        AddAlarmsAdapter addAlarmsAdapter = new AddAlarmsAdapter(asList(item, item, item));
        assertThat(addAlarmsAdapter.getItemCount(), equalTo(3));

        addAlarmsAdapter = new AddAlarmsAdapter(Collections.singletonList(item));
        assertThat(addAlarmsAdapter.getItemCount(), equalTo(1));

        List<Item> emptyList = new ArrayList<>();
        addAlarmsAdapter = new AddAlarmsAdapter(emptyList);
        assertThat(addAlarmsAdapter.getItemCount(), equalTo(0));
    }

}