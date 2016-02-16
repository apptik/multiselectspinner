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


import android.app.AlertDialog;
import android.content.Context;
import android.view.View;

public class MDialogBuilder extends AlertDialog.Builder {

    private View mDivider;

    public MDialogBuilder(Context context) {
        super(context);
    }

    public MDialogBuilder(Context context, int theme) {
        super(context, theme);
        //mDivider = mDialogView.findViewById(R.id.titleDivider);
    }
}
