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


import android.annotation.SuppressLint;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ExpandableMultiSelectSpinner extends BaseMultiSelectSpinner {

    private LinkedHashMap<String, List<String>> mapItems =  new LinkedHashMap<>();
    private ExpandableListView myList;
    private int expandableItemLayout=android.R.layout.simple_expandable_list_item_1;
    private int choiceItemLayout=android.R.layout.simple_list_item_multiple_choice;

    public int getExpandableItemLayout() {
        return expandableItemLayout;
    }

    public ExpandableMultiSelectSpinner setExpandableItemLayout(int expandableItemLayout) {
        this.expandableItemLayout = expandableItemLayout;
        return this;
    }

    public int getChoiceItemLayout() {
        return choiceItemLayout;
    }

    public ExpandableMultiSelectSpinner setChoiceItemLayout(int choiceItemLayout) {
        this.choiceItemLayout = choiceItemLayout;
        return this;
    }

    public ExpandableMultiSelectSpinner(Context context) {
        super(context);
    }

    public ExpandableMultiSelectSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandableMultiSelectSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ExpandableMultiSelectSpinner(Context context, AttributeSet attrs, int defStyle, int styleRes) {
        super(context, attrs, defStyle, styleRes);
    }


    public ExpandableMultiSelectSpinner setItems(LinkedHashMap<String, List<String>> items) {
        this.mapItems = items;
        this.items = new ArrayList<>();
        for(List<String> its:mapItems.values()) {
            for(String it:its) {
                this.items.add(it);
            }
        }

        // all selected by default
        selected = new boolean[this.items.size()];
        if(selectAll) {
            for (int i = 0; i < selected.length; i++) {
                selected[i] = true;
            }
        }

        // all text on the spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, new String[] { (isSelectAll())?allCheckedText:allUncheckedText });
        setAdapter(spinnerAdapter);

        return this;
    }

    @SuppressLint("NewApi")
    @Override
    public boolean performClick() {
        AlertDialog.Builder builder;
        final AlertDialog dialog;
        if(choiceDialogTheme >0) {
            builder = new AlertDialog.Builder(getContext(), choiceDialogTheme);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }
        builder.setTitle(title);

        builder.setOnCancelListener(this);
        builder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }
        );

        myList  = new ExpandableListView(getContext());
        myList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);



        //must check item ourselves
        myList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                // int position = groupPosition*(items.size() + 1) + childPosition + 1;
                int position = myList.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
                //Log.d("jtest", "jtest - pos: " + position+", gp: " + groupPosition + ", chp: " + childPosition + ", id: " + id);

                myList.setItemChecked(position, !myList.isItemChecked(position));
                selected[getFlatArrPos(groupPosition, childPosition)] = !selected[getFlatArrPos(groupPosition, childPosition)];
                return true;
            }
        });

        //must enable selection when group expands again -- baaad "expandable" listview
        myList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                List<String> gItems = mapItems.values().toArray(new List[0])[groupPosition];
                for(int i=0;i<gItems.size();i++) {
                    int position1 = myList.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, i));
                    int position2 =  getFlatArrPos(groupPosition, i);
                    myList.setItemChecked(position1, selected[position2]);
                }
            }
        });



        myList.setAdapter(new HashMapListAdapter<String>(mapItems, expandableItemLayout, choiceItemLayout));



        builder.setView(myList);
        dialog = builder.create();
        dialog.show();
        if(titleDividerDrawable !=null && dialog!=null) {
            int dividerId = dialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
            View divider = dialog.findViewById(dividerId);
            if((Build.VERSION.SDK_INT > 15)) {
                divider.setBackground(titleDividerDrawable);
            } else {
                divider.setBackgroundDrawable(titleDividerDrawable);
            }
        }

        if(titleDividerColor != 0 && dialog!=null) {
            int dividerId = dialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
            View divider = dialog.findViewById(dividerId);
            divider.setBackgroundColor(titleDividerColor);
        }

        int id = -1;
        int gid = -1;
        for(List<String> its:mapItems.values()) {
            gid++;
            //group must be expanded to set the value
            myList.expandGroup(gid);
            int chid = -1;
            for(String it:its) {
                id++;
                chid++;
                int position = myList.getFlatListPosition(ExpandableListView.getPackedPositionForChild(gid, chid));
                //Log.d("jtest", "jtest - pos: " + position + ", id: " + id + ", val: " + selected[id]);
                myList.setItemChecked(position, selected[id]);
            }
        }
        expandSelected();
        return true;
    }



    private int getFlatArrPos(int group, int child) {
        int id = -1;
        int gid = -1;

        for(List<String> its:mapItems.values()) {
            if(gid==group-1) break;
            id+=its.size();
            gid++;
        }

        id +=child + 1;
        return id;
    }


    private void expandSelected() {
        int id = -1;
        int gid = -1;
        for(List<String> its:mapItems.values()) {
            gid++;
            //group must be expanded to set the value
            myList.expandGroup(gid);

            boolean hasSelected = false;
            for(String it:its) {
                id++;
                if(selected[id]) {
                    hasSelected = true;
                };
            }
            if(!hasSelected) {
                myList.collapseGroup(gid);
            }

        }
    }

}