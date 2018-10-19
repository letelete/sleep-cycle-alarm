package com.gmail.brunokawka.poland.sleepcyclealarm.ui.tabs.addalarm;

import com.gmail.brunokawka.poland.sleepcyclealarm.data.pojo.Item;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ListAdapterTest {

    @Test
    public void testIfCanReturnItemCount() {
        Item item = new Item();

        ListAdapter listAdapter = new ListAdapter(asList(item, item, item));
        assertThat(listAdapter.getItemCount(), equalTo(3));

        listAdapter = new ListAdapter(Collections.singletonList(item));
        assertThat(listAdapter.getItemCount(), equalTo(1));

        List<Item> emptyList = new ArrayList<>();
        listAdapter = new ListAdapter(emptyList);
        assertThat(listAdapter.getItemCount(), equalTo(0));
    }

}