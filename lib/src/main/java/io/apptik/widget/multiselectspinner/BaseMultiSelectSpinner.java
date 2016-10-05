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
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
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


    protected int choiceDialogTheme;
    protected Drawable titleDividerDrawable;
    protected int titleDividerColor;

    /**
     * Construct a new spinner with the given context's theme.
     *
     * @param context The Context the view is running in, through which it can
     *        access the current theme, resources, etc.
     */
    public BaseMultiSelectSpinner(Context context) {
        this(context, null);
    }

    /**
     * Construct a new spinner with the given context's theme and the supplied
     * mode of displaying choices. <code>mode</code> may be one of
     * {@link #MODE_DIALOG} or {@link #MODE_DROPDOWN}.
     *
     * @param context The Context the view is running in, through which it can
     *        access the current theme, resources, etc.
     * @param mode Constant describing how the user will select choices from the spinner.
     *
     * @see #MODE_DIALOG
     * @see #MODE_DROPDOWN
     */
    public BaseMultiSelectSpinner(Context context, int mode) {
        this(context, null, R.attr.multiSelectSpinnerStyle, mode);
    }

    /**
     * Construct a new spinner with the given context's theme and the supplied attribute set.
     *
     * @param context The Context the view is running in, through which it can
     *        access the current theme, resources, etc.
     * @param attrs The attributes of the XML tag that is inflating the view.
     */
    public BaseMultiSelectSpinner(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.multiSelectSpinnerStyle);
    }

    /**
     * Construct a new spinner with the given context's theme, the supplied attribute set,
     * and default style attribute.
     *
     * @param context The Context the view is running in, through which it can
     *        access the current theme, resources, etc.
     * @param attrs The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a
     *        reference to a style resource that supplies default values for
     *        the view. Can be 0 to not look for defaults.
     */
    public BaseMultiSelectSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0, -1);
    }

    /**
     * Construct a new spinner with the given context's theme, the supplied attribute set,
     * and default style. <code>mode</code> may be one of {@link #MODE_DIALOG} or
     * {@link #MODE_DROPDOWN} and determines how the user will select choices from the spinner.
     *
     * @param context The Context the view is running in, through which it can
     *        access the current theme, resources, etc.
     * @param attrs The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a
     *        reference to a style resource that supplies default values for
     *        the view. Can be 0 to not look for defaults.
     * @param mode Constant describing how the user will select choices from the spinner.
     *
     * @see #MODE_DIALOG
     * @see #MODE_DROPDOWN
     */
    public BaseMultiSelectSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        this(context, attrs, defStyleAttr, 0, mode);
    }

    /**
     * Construct a new spinner with the given context's theme, the supplied attribute set,
     * and default style. <code>mode</code> may be one of {@link #MODE_DIALOG} or
     * {@link #MODE_DROPDOWN} and determines how the user will select choices from the spinner.
     *
     * @param context The Context the view is running in, through which it can
     *        access the current theme, resources, etc.
     * @param attrs The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a
     *        reference to a style resource that supplies default values for
     *        the view. Can be 0 to not look for defaults.
     * @param defStyleRes A resource identifier of a style resource that
     *        supplies default values for the view, used only if
     *        defStyleAttr is 0 or can not be found in the theme. Can be 0
     *        to not look for defaults.
     * @param mode Constant describing how the user will select choices from the spinner.
     *
     * @see #MODE_DIALOG
     * @see #MODE_DROPDOWN
     */
    public BaseMultiSelectSpinner(
            Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, int mode) {
        super(context, attrs);

        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.MultiSelectSpinner, defStyleAttr, defStyleRes);

        choiceDialogTheme = a.getResourceId(R.styleable.MultiSelectSpinner_choiceDialogTheme, 0);

        titleDividerDrawable = a.getDrawable(R.styleable.MultiSelectSpinner_titleDividerDrawable);

        titleDividerColor = a.getColor(R.styleable.MultiSelectSpinner_titleDividerColor, 0);
    }

    public int getTitleDividerColor() {
        return titleDividerColor;
    }

    public void setTitleDividerColor(int titleDividerColor) {
        this.titleDividerColor = titleDividerColor;
    }

    public int getChoiceDialogTheme() {
        return choiceDialogTheme;
    }

    public void setChoiceDialogTheme(int choiceDialogTheme) {
        this.choiceDialogTheme = choiceDialogTheme;
    }

    public Drawable getTitleDividerDrawable() {
        return titleDividerDrawable;
    }

    public void setTitleDividerDrawable(Drawable titleDividerDrawable) {
        this.titleDividerDrawable = titleDividerDrawable;
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
        ss.items = items;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        selected = ss.selected;
        items = ss.items;
        refreshSpinnerText(getSpinnerText());
    }

    static class SavedState extends BaseSavedState{
        boolean[] selected;
        List<String> items;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            selected = in.createBooleanArray();
            items = in.createStringArrayList();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeBooleanArray(selected);
            out.writeStringList(items);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        SavedState ss = new SavedState(in);
                        return ss;
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

}
