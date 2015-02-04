/*
 * Copyright (C) 2014 Kalin Maldzhanski
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
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class MultiSelectSpinner extends Spinner implements
        OnMultiChoiceClickListener, DialogInterface.OnCancelListener {

    protected List<String> items;

    protected boolean[] selected;
    protected String allCheckedText;
    protected String allUncheckedText;
    protected MultiSpinnerListener listener;
    protected ListAdapter listAdapter;
    protected boolean selectAll;
    protected int minSelectedItems =0;
    protected int maxSelectedItems = Integer.MAX_VALUE;


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

    public boolean[] getSelected() {
        return selected;
    }
    public boolean isSelectAll() {
        return selectAll;
    }

    public MultiSelectSpinner setSelectAll(boolean selectAll) {
        if(this.selectAll != selectAll) {
            this.selectAll = selectAll;
            if (selected != null) {
                if (selectAll) {
                    for (int i = 0; i < selected.length; i++) {
                        selected[i] = true;
                    }
                } else {
                    for (int i = 0; i < selected.length; i++) {
                        selected[i] = false;
                    }
                }
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, new String[]{(isSelectAll()) ? allCheckedText : allUncheckedText});
                setAdapter(spinnerAdapter);
            }
        }
        return this;
    }

    public int getMinSelectedItems() {
        return minSelectedItems;
    }

    public MultiSelectSpinner setMinSelectedItems(int minSelectedItems) {
        this.minSelectedItems = minSelectedItems;
        return this;
    }

    public int getMaxSelectedItems() {
        return maxSelectedItems;
    }

    public MultiSelectSpinner setMaxSelectedItems(int maxSelectedItems) {
        this.maxSelectedItems = maxSelectedItems;
        return this;
    }

//    public int getCurrCheckedItems() {
//        return currCheckedItems;
//    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if (isChecked) {
            if(((AlertDialog)dialog).getListView().getCheckedItemCount() > maxSelectedItems) {
                ((AlertDialog)dialog).getListView().setItemChecked(which, false);
            } else {
                selected[which] = true;
            }
        }
        else {
            if(((AlertDialog)dialog).getListView().getCheckedItemCount() < minSelectedItems) {
                ((AlertDialog)dialog).getListView().setItemChecked(which, true);
            } else {
                selected[which] = false;
            }
        }

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // refresh text on spinner
        StringBuffer spinnerBuffer = new StringBuffer();
        for (int i = 0; i < items.size(); i++) {
            if (selected[i] == true) {
                spinnerBuffer.append(items.get(i));
                spinnerBuffer.append(", ");
            }
        }
        String spinnerText;
        if(((AlertDialog)dialog).getListView().getCheckedItemCount()==selected.length) {
            spinnerText = allCheckedText;
        } else if(((AlertDialog)dialog).getListView().getCheckedItemCount()==0) {
            spinnerText = allUncheckedText;
        } else {
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

    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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


    public MultiSelectSpinner setItems(List<String> items, String allCheckedText, String allUncheckedText,
                                       MultiSpinnerListener listener) {
        this.items = items;
        this.allCheckedText = allCheckedText;
        this.allUncheckedText = allUncheckedText;
        this.listener = listener;

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

    public interface MultiSpinnerListener {
        public void onItemsSelected(boolean[] selected);
    }


    public MultiSelectSpinner setListAdapter(ListAdapter listAdapter, String allCheckedText, String allUncheckedText, MultiSpinnerListener listener) {
        this.listener = listener;
        this.allCheckedText = allCheckedText;
        this.allUncheckedText = allUncheckedText;
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

    public MultiSelectSpinner selectItem(int item, boolean set) {
        if(item>=selected.length) {
            throw new ArrayIndexOutOfBoundsException("Item number is more than available items");
        }
        selected[item] = set;
        // refresh text on spinner
        StringBuffer spinnerBuffer = new StringBuffer();
        for (int i = 0; i < items.size(); i++) {
            if (selected[i] == true) {
                spinnerBuffer.append(items.get(i));
                spinnerBuffer.append(", ");
            }
        }
        String spinnerText;

        spinnerText = spinnerBuffer.toString();
        if (spinnerText.length() > 2)
            spinnerText = spinnerText.substring(0, spinnerText.length() - 2);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,
                new String[] { spinnerText });
        setAdapter(adapter);
        return  this;
    }

}
