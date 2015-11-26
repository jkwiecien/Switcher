[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Switcher-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/2603)

# What is it?
Library that allows you to easily switch between your content, progress, empty placeholder view. It does it with smooth crossfade animation. It also lets you add on click listener to the error view.
  You only show content, progress or error view. The switcher finds out what's currently visible and hides it. You can hide/show multiple views at once.
  
  
<img src="http://g.recordit.co/Gav0lfPXMV.gif" height="340" />
  
#How to use it?
1)  It's important that all parent views of the layout are registered with switcher as progress, error or empty view.

Here is example layout:
```xml
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <include layout="@layout/view_progress" />

    <include layout="@layout/view_error" />

    <include layout="@layout/view_empty" />

    <android.support.design.widget.CoordinatorLayout
        android:id="@+id/coordinator_layout"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <android.support.design.widget.AppBarLayout
            android:id="@+id/appbar"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar">

            <android.support.v7.widget.Toolbar
                android:id="@+id/toolbar"
                android:layout_width="match_parent"
                android:layout_height="?attr/actionBarSize"
                app:layout_scrollFlags="scroll|enterAlways"
                app:popupTheme="@style/ThemeOverlay.AppCompat.Light" />

        </android.support.design.widget.AppBarLayout>

        <android.support.v7.widget.RecyclerView
            android:id="@+id/recycler_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:layout_behavior="@string/appbar_scrolling_view_behavior" />

        <android.support.design.widget.FloatingActionButton
            android:id="@+id/fab"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="bottom|end"
            android:layout_margin="14dp"
            android:src="@drawable/ic_message_white_24dp"
            app:layout_anchor="@id/recycler_view"
            app:layout_anchorGravity="bottom|right|end" />

    </android.support.design.widget.CoordinatorLayout>
</FrameLayout>
```

2) Build your switcher (if you use it from activity, ```onCreate()``` will be the good place, if it's a fragment, use ```onCreateView()```)
```java
        switcher = new Switcher.Builder(this)
                .addContentView(findViewById(R.id.recycler_view)) //content member
                .addContentView(findViewById(R.id.fab)) //content member
                .addErrorView(findViewById(R.id.error_view)) //error view member
                .addProgressView(findViewById(R.id.progress_view)) //progress view member
                .setErrorLabel((TextView) findViewById(R.id.error_label)) // TextView within your error member group that you want to change
                .setProgressLabel((TextView) findViewById(R.id.progress_label)) // TextView within your progress member group that you want to change
                .addEmptyView(findViewById(R.id.empty_view)) //empty placeholder member
                .build();
```

3) Use the Switcher object to switch between your views. This will use the crossfade animation.
```java
switcher.showContentView();
switcher.showProgressView();
switcher.showErrorView();
switcher.showEmptyView();
```

If you rather switch views immediately without animation use ```showProgressViewImmediately()``` and corresponding for other states.

#Setup
```groovy
repositories {
    maven { url "https://jitpack.io" }
}
    
dependencies {
    compile 'com.github.jkwiecien:Switcher:2.0.5'
}
```


License
=======

    Copyright 2014 Jacek Kwiecień.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
