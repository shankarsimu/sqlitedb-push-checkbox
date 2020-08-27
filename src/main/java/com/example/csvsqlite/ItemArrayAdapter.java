package com.example.csvsqlite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ItemArrayAdapter extends ArrayAdapter {
    private List scoreList = new ArrayList();

    static class ItemViewHolder {
        TextView name;
        TextView score;
        TextView language;
        TextView quality;
    }

    public ItemArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public void add(Object object) {
        scoreList.add(object);
        super.add(object);
    }

    @Override
    public void clear() {
        scoreList.clear();
        super.clear();
    }

    @Override
    public int getCount() {
        return this.scoreList.size();
    }

    @Override
    public String[] getItem(int index) {
        return (String[]) this.scoreList.get(index);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ItemViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.item_layout, parent, false);
            viewHolder = new ItemViewHolder();
            viewHolder.name = (TextView) row.findViewById(R.id.name);
            viewHolder.score = (TextView) row.findViewById(R.id.score);
            viewHolder.language = (TextView) row.findViewById(R.id.language);
            viewHolder.quality = (TextView) row.findViewById(R.id.quality);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ItemViewHolder) row.getTag();
        }
        String[] stat = getItem(position);
        int k = stat.length;
        while (k > 0) {
            if (k == 1)
                viewHolder.name.setText(stat[0]);
            if (k == 2)
                viewHolder.score.setText(stat[1]);
            if (k == 3)
                viewHolder.language.setText(stat[2]);
            if (k == 4)
                viewHolder.quality.setText(stat[3]);
            k--;
        }

        return row;
    }
}