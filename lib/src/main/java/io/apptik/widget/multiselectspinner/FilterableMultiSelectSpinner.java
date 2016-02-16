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

package io.apptik.widget.multiselectspinner;


import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

public class FilterableMultiSelectSpinner extends MultiSelectSpinner implements
        OnMultiChoiceClickListener, DialogInterface.OnCancelListener {

    public FilterableMultiSelectSpinner(Context context) {
        super(context);
    }

    public FilterableMultiSelectSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FilterableMultiSelectSpinner(Context context, AttributeSet attrs, int defStyle, int styleRes) {
        super(context, attrs, defStyle, styleRes);
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
        final EditText input = new EditText(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);

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
            dialog.setView(input);
            return true;
        } else if(items!=null) {
            final AlertDialog dialog =  builder
                    .setMultiChoiceItems(items.toArray(new CharSequence[items.size()]), selected, this)
                    .create();

            dialog.show();
            dialog.setView(input);
            return true;

        }
        return false;
    }



}
