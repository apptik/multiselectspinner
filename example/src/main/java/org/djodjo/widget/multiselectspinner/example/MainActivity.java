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

package org.djodjo.widget.multiselectspinner.example;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.djodjo.widget.multiselectspinner.MultiSelectSpinner;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> options = new ArrayList<>();
        options.add("1");
        options.add("2");
        options.add("3");
        options.add("A");
        options.add("B");
        options.add("C");


        MultiSelectSpinner multiSelectSpinner1 = (MultiSelectSpinner) findViewById(R.id.multiselectSpinner1);
        multiSelectSpinner1.setItems(options, "All types", "none selected", new MultiSelectSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {

            }
        }).setSelectAll(false);

        MultiSelectSpinner multiSelectSpinner2 = (MultiSelectSpinner) findViewById(R.id.multiselectSpinner2);
        MultiSelectSpinner multiSelectSpinner3 = (MultiSelectSpinner) findViewById(R.id.multiselectSpinner3);
        MultiSelectSpinner multiSelectSpinner4 = (MultiSelectSpinner) findViewById(R.id.multiselectSpinner4);
        MultiSelectSpinner multiSelectSpinner5 = (MultiSelectSpinner) findViewById(R.id.multiselectSpinner5);
        ArrayAdapter<String> adapter2 = new ArrayAdapter <String>(this, android.R.layout.simple_list_item_multiple_choice, options);
        ArrayAdapter<String> adapter3 = new ArrayAdapter <String>(this, android.R.layout.simple_list_item_checked, options);
        ArrayAdapter<String> adapter4 = new ArrayAdapter <String>(this, android.R.layout.simple_list_item_activated_1, options);
        ArrayAdapter<String> adapter5 = new ArrayAdapter <String>(this, R.layout.custom_item, options);

        Spinner nSpinner = (Spinner) findViewById(R.id.normalSpinner);
        nSpinner.setAdapter(adapter5);


        multiSelectSpinner2
                .setListAdapter(adapter2, "All Types", "none selected", new MultiSelectSpinner.MultiSpinnerListener() {
                    @Override
                    public void onItemsSelected(boolean[] checkedItems) {
                    }
                })
                .setSelectAll(true)
                .setMinSelectedItems(1);

        multiSelectSpinner3
                .setListAdapter(adapter3, "All Types", "none selected", new MultiSelectSpinner.MultiSpinnerListener() {
                    @Override
                    public void onItemsSelected(boolean[] checkedItems) {
                    }
                })
                .setSelectAll(true)
                .setMinSelectedItems(1);

        multiSelectSpinner4
                .setListAdapter(adapter4, "All Types", "none selected", new MultiSelectSpinner.MultiSpinnerListener() {
                    @Override
                    public void onItemsSelected(boolean[] checkedItems) {
                    }
                })
                .setSelectAll(true)
                .setMinSelectedItems(1);

        multiSelectSpinner5
                .setListAdapter(adapter5, "All Types", "none selected", new MultiSelectSpinner.MultiSpinnerListener() {
                    @Override
                    public void onItemsSelected(boolean[] checkedItems) {
                    }
                })
                .setSelectAll(true)
                .setMinSelectedItems(1);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
