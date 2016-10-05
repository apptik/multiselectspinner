multiselectspinner
==================

[![Build Status](https://travis-ci.org/apptik/multiselectspinner.svg?branch=master)](https://travis-ci.org/apptik/multiselectspinner)

Multi functional and selectable spinner for Android

##Download

Find [the latest JARs][mvn] or grab via Maven:
```xml
<dependency>
  <groupId>io.apptik.widget</groupId>
  <artifactId>multiselectspinner</artifactId>
  <version>1.0.12</version>
</dependency>
```
or Gradle:
```groovy
compile 'io.apptik.widget:multiselectspinner:1.0.12'
```

Downloads of the released versions are available in [Sonatype's `releases` repository][release].

Snapshots of the development versions are available in [Sonatype's `snapshots` repository][snap].

[![Maven Central](https://img.shields.io/maven-central/v/io.apptik.widget/multiselectspinner.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/io.apptik.widget/multiselectspinner)

multiselectspinner requires at minimum Java 7 or Android SDK 15.

Example
---

* specify in your layout
```xml
    <io.apptik.widget.multiselectspinner.MultiSelectSpinner
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


[mvn]: http://search.maven.org/#search|ga|1|io.apptik.widget.multiselectspinner
[release]: https://oss.sonatype.org/content/repositories/releases/io/apptik/widget/multiselectspinner/
[snap]: https://oss.sonatype.org/content/repositories/snapshots/io/apptik/widget/multiselectspinner/