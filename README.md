multiselectspinner
==================

Multi functional and selectable spinner for Android


Example
---

* specify in your layout
```xml
    <org.djodjo.widget.MultiSelectSpinner
        android:id="@+id/multiselectSpinner"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        /> 
```
* add in your code
```java
 ArrayList<String> options = new ArrayList<>();
 options.add("1");
 options.add("2");
 options.add("3");
 options.add("A");
 options.add("B");
 options.add("C");
 MultiSelectSpinner multiSelectSpinner = (MultiSelectSpinner) v.findViewById(R.id.multiselectSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter <String>(getActivity(), android.R.layout.simple_list_item_multiple_choice, options);

        multiSelectSpinner
                .setListAdapter(adapter, "All " +
                        " Types", "none", new MultiSelectSpinner.MultiSpinnerListener() {
                    @Override
                    public void onItemsSelected(boolean[] checkedItems) {
                    }
                })
                .setSelectAll(true)
                .setMinSelectedItems(1);
```
