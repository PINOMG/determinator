package com.pinomg.determinator.helpers;

import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pinomg.determinator.R;
import com.pinomg.determinator.model.Poll;

/*
 * An adapter used in listactivity. Lists Polls under separate headers,
 * depending on their statuses.
 */
public class PollListViewAdapter extends BaseAdapter {

    public static final int TYPE_POLL = 0;
    public static final int TYPE_HEADER = 1;

    private Map<Integer,List<Poll>> items; // The sorted list
    private List<Poll> polls; // The list of all polls, unsorted
    private LayoutInflater mInflater;

    public PollListViewAdapter(Context context, List<Poll> polls) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        items = new HashMap<>();
        this.polls = polls;

        populateList();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        populateList();
    }

    // Populate datastructure with polls, sorted by status
    private void populateList() {
        items.clear();
        for(Poll poll : polls) {
            Integer status = poll.getStatus();

            if(!items.containsKey(status)) {
                List<Poll> pollList = new ArrayList<>();
                items.put(status, pollList);
            }
            items.get(status).add(poll);
        }
    }

    // Returns type of list item, poll or header
    @Override
    public int getItemViewType(int position) {
        try {
            Poll poll = (Poll) getItem(position);
            return TYPE_POLL;
        } catch(Exception e) {
            return TYPE_HEADER;
        }
    }

    // Returns number of view types in list
    @Override
    public int getViewTypeCount() {
        return Math.max(1, items.size());
    }

    // Returns size of list, which is number of polls + headers
    @Override
    public int getCount() {
        int count = 0;

        for(List<Poll> list : items.values()) {
            count += list.size();
        }

        return count + items.size();
    }

    // Returns item in given position
    @Override
    public Object getItem(int position) {

        // Order in which the polls are sorted
        int[] order = {
                Poll.STATUS_FINISHED,
                Poll.STATUS_PENDING,
                Poll.STATUS_ANSWERED,
                Poll.STATUS_ARCHIVED
            };

        int i = 0;
        List<Poll> list = items.get(order[i]);

        // Loops over the specified order, and inserts headers where needed
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

    // Method that says that not all items are enabled (headers should not be clickable)
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    // Specifies that only items of type poll will be clickable
    @Override
    public boolean isEnabled(int position) {
        return (getItemViewType(position) == TYPE_POLL);
    }

    // Connects item type with correct layout
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
