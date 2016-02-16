/*
 * Copyright (C) 2015 Kalin Maldzhanski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.apptik.widget.multiselectspinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.List;

public class HashMapListAdapter<T> extends BaseExpandableListAdapter {

    LinkedHashMap<String, List<T>> items =  new LinkedHashMap<>();
    private int expandableItemLayout;
    private int choiceItemLayout;

    public int getExpandableItemLayout() {
        return expandableItemLayout;
    }

    public HashMapListAdapter setExpandableItemLayout(int expandableItemLayout) {
        this.expandableItemLayout = expandableItemLayout;
        return this;
    }

    public int getChoiceItemLayout() {
        return choiceItemLayout;
    }

    public HashMapListAdapter setChoiceItemLayout(int choiceItemLayout) {
        this.choiceItemLayout = choiceItemLayout;
        return this;
    }

    public HashMapListAdapter(LinkedHashMap<String, List<T>> items, int expandableItemLayout, int choiceItemLayout) {
        this.items = items;
        this.expandableItemLayout = expandableItemLayout;
        this.choiceItemLayout = choiceItemLayout;
    }

    @Override
    public int getGroupCount() {
        return items.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return items.values().toArray(new List[0])[groupPosition].size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return items.keySet().toArray(new String[0])[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return items.values().toArray(new List[0])[groupPosition].get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition+groupPosition*100000;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(expandableItemLayout, parent, false);
        }

        TextView groupName = (TextView) v.findViewById(android.R.id.text1);
        groupName.setText((String) getGroup(groupPosition));

        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(choiceItemLayout, parent, false);
        }

        TextView groupName = (TextView) v.findViewById(android.R.id.text1);
        groupName.setText(getChild(groupPosition, childPosition).toString());

        return v;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
