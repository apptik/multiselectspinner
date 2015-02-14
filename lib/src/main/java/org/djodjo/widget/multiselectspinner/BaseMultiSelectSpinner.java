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
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Spinner;

import java.util.List;

public abstract class BaseMultiSelectSpinner extends Spinner implements
        OnMultiChoiceClickListener, DialogInterface.OnCancelListener {

    protected String allCheckedText = "";
    protected String allUncheckedText = "";
    protected MultiSpinnerListener listener;
    protected ListAdapter listAdapter;
    protected boolean selectAll;
    protected int minSelectedItems =0;
    protected int maxSelectedItems = Integer.MAX_VALUE;
    protected String title = null;
    protected boolean[] selected;
    protected List<String> items;
    protected int spinnerItemLayout = android.R.layout.simple_spinner_item;


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

    public int getSpinnerItemLayout() {
        return spinnerItemLayout;
    }

    public <T extends BaseMultiSelectSpinner> T setSpinnerItemLayout(int spinnerItemLayout) {
        this.spinnerItemLayout = spinnerItemLayout;
        return (T)this;
    }


    public String getAllCheckedText() {
        return allCheckedText;
    }

    public <T extends BaseMultiSelectSpinner> T setAllCheckedText(String allCheckedText) {
        this.allCheckedText = allCheckedText;
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(),
                spinnerItemLayout , new String[]{(isSelectAll()) ? allCheckedText : allUncheckedText});
        setAdapter(spinnerAdapter);
        return (T)this;
    }

    public String getAllUncheckedText() {
        return allUncheckedText;
    }

    public <T extends BaseMultiSelectSpinner> T  setAllUncheckedText(String allUncheckedText) {
        this.allUncheckedText = allUncheckedText;
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(),
                spinnerItemLayout, new String[]{(isSelectAll()) ? allCheckedText : allUncheckedText});
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



    public boolean[] getSelected() {
        return selected;
    }

    public boolean isSelectAll() {
        for(boolean si :selected) {
            if(!si) return false;
        }
        return true;
    }

    public boolean isSelectNone() {
        for(boolean si :selected) {
            if(si) return false;
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
                        spinnerItemLayout, new String[]{(isSelectAll()) ? allCheckedText : allUncheckedText});
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
    public void onCancel(DialogInterface dialog) {
        refreshSpinnerText(getSpinnerText());
        if(listener!=null) {
            listener.onItemsSelected(selected);
        }
    }

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

        refreshSpinnerText(getSpinnerText());
        return  (T)this;
    }

    public String getSpinnerText() {
        String spinnerText;
        if(isSelectAll()) {
            spinnerText = allCheckedText;
        } else if(isSelectNone()) {
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

        return spinnerText;
    }

    public void refreshSpinnerText(String text) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                spinnerItemLayout,
                new String[] { text });
        setAdapter(adapter);
    }


    @Override
    public Parcelable onSaveInstanceState() {
        final SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.selected = selected;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        selected = ss.selected;
        refreshSpinnerText(getSpinnerText());
    }

    static class SavedState extends BaseSavedState{
        boolean[] selected;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            in.readBooleanArray(selected);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeBooleanArray(selected);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

}
