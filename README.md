[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Switcher-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/2603)

# What is it?
Library that allows you to eaisly switch between your content, progress, error and blur view. It does it with smooth crossfade animation. It also lets you add on click listener to the error view.
  You only show content, progress or error view. The switcher finds out what's currently visible and hides it.
  
  
<img src="http://g.recordit.co/LAshoSUNxi.gif" height="340" />
  
#How to use it?
1)  Build your layout like this. It's important to pack your content, progress and error view into the same ```FrameLayout```. It won't work otherwise.

```xml
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">
        
        <LinearLayout
            android:id="@+id/empty_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:gravity="center"
            android:orientation="vertical"
            android:padding="20dp"
            android:visibility="invisible">

            <-- your empty placeholder subviews goes here -->

        </LinearLayout>

        <LinearLayout
            android:id="@+id/progress_view"
            android:orientation="vertical"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:visibility="invisible">

            <-- your progress subviews goes here -->

        </LinearLayout>

        <LinearLayout
            android:id="@+id/error_view"
            android:orientation="vertical"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:visibility="invisible">

            <-- your error subviews goes here -->

        </LinearLayout>

        <LinearLayout
            android:id="@+id/content"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:gravity="center"
            android:orientation="vertical">

            <-- your content goes here -->

        </LinearLayout>
    </FrameLayout>

</RelativeLayout>
```

2) Build your switcher (if you use it from activity, ```onCreate()``` will be the good place, if it's a fragment, use ```onCreateView()```)
```java
switcher = new Switcher.Builder()
                .withContentView(findViewById(R.id.content)) //ViewGroup holding your main content
                .withErrorView(findViewById(R.id.error_view)) //ViewGroup holding your error view
                .withProgressView(findViewById(R.id.progress_view)) //ViewGroup holding your progress view
                .withBlurView(findViewById(R.id.blur_view)) //View overlaying another view, that you'd like to blur
                .withErrorLabel((TextView) findViewById(R.id.error_label)) // TextView within your error ViewGroup that you want to change
                .withProgressLabel((TextView) findViewById(R.id.progress_label)) // TextView within your progress ViewGroup that you want to change
                .withEmptyView(findViewById(R.id.empty_view)) //SOme empty placeholder we display on lists for example if there are no results
                .build();
```

3) Use the Switcher object to switch between your views. This will use the crossfade animation.
```java
switcher.showContentView();
switcher.showProgressView();
switcher.showErrorView();
switcher.showEmptyView();
switcher.showBlurView(View viewToBlur);
```

If you rather switch views immediately without animation use ```showProgressViewImmediately()``` and corresponding for other states.

#Setup
```
repositories {
    maven { url "https://jitpack.io" }
}
    
dependencies {
    compile ('com.github.jkwiecien:Switcher:1.1.3'){
        exclude module: 'appcompat-v7'
    }
}
```

If you wan't to use blur view, you also need to modify your app ```defaultConfig```:

```
    defaultConfig {
        renderscriptTargetApi 22
        renderscriptSupportModeEnabled true
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
