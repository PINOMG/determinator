package com.pinomg.determinator;

import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ebbamannheimer on 2015-05-11.
 */
public class CustomAdapter extends BaseAdapter {

    public static final int TYPE_POLL = 0;
    public static final int TYPE_HEADER = 1;

    private Map<Integer,List<Poll>> items;
    private List<Poll> polls;
    private LayoutInflater mInflater;

    public CustomAdapter(Context context, List<Poll> polls) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        items = new HashMap<Integer, List<Poll>>();
        this.polls = polls;
        populateList();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        populateList();
    }

    private void populateList() {
        items.clear();
        for(Poll poll : polls) {
            Integer status = poll.getStatus();

            if(!items.containsKey(status)) {
                List<Poll> pollList = new ArrayList<Poll>();
                items.put(status, pollList);
            }
            items.get(status).add(poll);
        }
    }

    @Override
    public int getItemViewType(int position) {
        try {
            Poll poll = (Poll) getItem(position);
            return TYPE_POLL;
        } catch(Exception e) {
            return TYPE_HEADER;
        }
    }

    @Override
    public int getViewTypeCount() {
        return Math.max(1,items.size());
    }

    @Override
    public int getCount() {
        int count = 0;

        for(List<Poll> list : items.values()) {
            count += list.size();
        }

        return count + items.size();
    }

    @Override
    public Object getItem(int position) {

        int[] order = {
                Poll.STATUS_FINISHED,
                Poll.STATUS_PENDING,
                Poll.STATUS_ANSWERED,
                Poll.STATUS_ARCHIVED
            };

        int i = 0;
        List<Poll> list = items.get(order[i]);

        while((list == null && getCount() > 0) || position >= list.size() + 1) {

            if(list != null)
                position -= list.size() + 1;

            list = items.get(order[++i]);
        }

        if(position == 0) {
            return Poll.getStatusText(order[i]);
        } else {
            return list.get(position - 1);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return (getItemViewType(position) == TYPE_POLL);
    }

    public View getView(int position, View convertView, ViewGroup parent) {


        TextView textView;
        switch (getItemViewType(position)) {
            case TYPE_POLL:
                convertView   = mInflater.inflate(android.R.layout.simple_list_item_1, null);
                textView = (TextView) convertView.findViewById(android.R.id.text1);

                Poll poll = (Poll) getItem(position);
                textView.setText(poll.toString());
                break;
            case TYPE_HEADER:
                convertView   = mInflater.inflate(R.layout.list_separator, null);
                textView = (TextView) convertView.findViewById(R.id.textSeparator);
                textView.setText((String) getItem(position));
                break;
        }

        return convertView;
    }


}
