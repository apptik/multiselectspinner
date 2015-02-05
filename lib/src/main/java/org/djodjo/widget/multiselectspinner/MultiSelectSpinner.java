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

package org.djodjo.widget.multiselectspinner;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MultiSelectSpinner extends BaseMultiSelectSpinner {



    public MultiSelectSpinner(Context context) {
        super(context);
    }

    public MultiSelectSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiSelectSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MultiSelectSpinner(Context context, AttributeSet attrs, int defStyle, int styleRes) {
        super(context, attrs, defStyle, styleRes);
    }

    public MultiSelectSpinner setItems(List<String> items) {
        this.items = items;

        // all selected by default
        selected = new boolean[items.size()];
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

    public BaseMultiSelectSpinner setListAdapter(ListAdapter listAdapter) {
        this.listAdapter = listAdapter;
        this.items = new ArrayList<String>();
        selected = new boolean[listAdapter.getCount()];
        for(int i=0;i<listAdapter.getCount();i++) {
            items.add(String.valueOf(listAdapter.getItem(i)));
            if(selectAll) {
                selected[i] = true;
            }

        }

        // all text on the spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, new String[] { (isSelectAll())?allCheckedText:allUncheckedText });
        setAdapter(spinnerAdapter);

        return this;
    }

    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

        if(listAdapter!=null) {
            builder.setAdapter(this.listAdapter, null);
            final AlertDialog dialog = builder.create();
            dialog.getListView().setItemsCanFocus(false);
            dialog.getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            dialog.show();
            for(int i=0;i<listAdapter.getCount();i++) {
                dialog.getListView().setItemChecked(i,selected[i]);
            }
            dialog.getListView().setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if ( !selected[position]) {
                        if(((AlertDialog)dialog).getListView().getCheckedItemCount() > maxSelectedItems) {
                            dialog.getListView().setItemChecked(position, false);
                        } else {
                            selected[position] = !selected[position];
                        }
                    }
                    else {
                        if(((AlertDialog)dialog).getListView().getCheckedItemCount() < minSelectedItems) {
                            dialog.getListView().setItemChecked(position, true);
                        }  else {
                            selected[position] = !selected[position];
                        }
                    }


                }
            });
            return true;
        } else if(items!=null) {
            builder.setMultiChoiceItems(items.toArray(new CharSequence[items.size()]), selected, this).show();
            return true;
        }
        return false;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // refresh text on spinner

        String spinnerText;
        if(((AlertDialog)dialog).getListView().getCheckedItemCount()==selected.length) {
            spinnerText = allCheckedText;
        } else if(((AlertDialog)dialog).getListView().getCheckedItemCount()==0) {
            spinnerText = allUncheckedText;
        } else {
            StringBuffer spinnerBuffer = new StringBuffer();
            for (int i = 0; i < items.size(); i++) {
                if (selected[i] == true) {
                    spinnerBuffer.append(items.get(i));
                    spinnerBuffer.append(", ");
                }
            }
            spinnerText = spinnerBuffer.toString();
            if (spinnerText.length() > 2)
                spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,
                new String[] { spinnerText });
        setAdapter(adapter);
        if(listener!=null) {
            listener.onItemsSelected(selected);
        }
    }
}