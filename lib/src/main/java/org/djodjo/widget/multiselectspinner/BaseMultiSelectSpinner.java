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
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Spinner;

import java.util.List;

public abstract class BaseMultiSelectSpinner extends Spinner implements
        OnMultiChoiceClickListener, DialogInterface.OnCancelListener {

    protected boolean[] selected;
    protected List<String> items;

    public String getAllCheckedText() {
        return allCheckedText;
    }

    public <T extends BaseMultiSelectSpinner> T setAllCheckedText(String allCheckedText) {
        this.allCheckedText = allCheckedText;
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, new String[]{(isSelectAll()) ? allCheckedText : allUncheckedText});
        setAdapter(spinnerAdapter);
        return (T)this;
    }

    public String getAllUncheckedText() {
        return allUncheckedText;
    }

    public <T extends BaseMultiSelectSpinner> T  setAllUncheckedText(String allUncheckedText) {
        this.allUncheckedText = allUncheckedText;
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, new String[]{(isSelectAll()) ? allCheckedText : allUncheckedText});
        setAdapter(spinnerAdapter);
        return (T)this;
    }

    public MultiSpinnerListener getListener() {
        return listener;
    }

    public <T extends BaseMultiSelectSpinner> T  setListener(MultiSpinnerListener listener) {
        this.listener = listener;
        return (T)this;
    }

    protected String allCheckedText = "";
    protected String allUncheckedText = "";
    protected MultiSpinnerListener listener;
    protected ListAdapter listAdapter;
    protected boolean selectAll;
    protected int minSelectedItems =0;
    protected int maxSelectedItems = Integer.MAX_VALUE;
    protected String title = null;


    public BaseMultiSelectSpinner(Context context) {
        super(context);
    }

    public BaseMultiSelectSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseMultiSelectSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BaseMultiSelectSpinner(Context context, AttributeSet attrs, int defStyle, int styleRes) {
        super(context, attrs, defStyle, styleRes);
    }

    public boolean[] getSelected() {
        return selected;
    }

    public boolean isSelectAll() {
        for(boolean si :selected) {
            if(!si) return false;
        }
        return true;
    }

    public <T extends BaseMultiSelectSpinner> T setSelectAll(boolean selectAll) {
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
        return (T)this;
    }

    public int getMinSelectedItems() {
        return minSelectedItems;
    }

    public <T extends BaseMultiSelectSpinner> T setMinSelectedItems(int minSelectedItems) {
        this.minSelectedItems = minSelectedItems;
        return (T)this;
    }

    public int getMaxSelectedItems() {
        return maxSelectedItems;
    }

    public <T extends BaseMultiSelectSpinner> T setMaxSelectedItems(int maxSelectedItems) {
        this.maxSelectedItems = maxSelectedItems;
        return (T)this;
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
    public abstract void onCancel(DialogInterface dialog);

    public abstract boolean performClick();

    public <T extends BaseMultiSelectSpinner> T setTitle(int stringResource) {
        this.title = getResources().getString(stringResource);
        return (T)this;
    }

    public <T extends BaseMultiSelectSpinner> T setTitle(String title) {
        this.title = title;
        return (T)this;
    }


    public interface MultiSpinnerListener {
        public void onItemsSelected(boolean[] selected);
    }


    public <T extends BaseMultiSelectSpinner> T selectItem(int item, boolean set) {
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
        return  (T)this;
    }

}
