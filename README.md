# What is it?
Library that allows you to eaisly switch between your content, progress, error and blur view. It does it with smooth crossfade animation. It also lets you add on click listener to the error view.
  You only show content, progress or error view. The switcher finds out what's currently visible and hides it.
  
  
<img src="http://g.recordit.co/eMdCTknEmO.gif" height="340" />
  
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
                .build();
```

3) Use the Switcher object to switch between your views
```java
switcher.showContentView();
switcher.showProgressView();
switcher.showErrorView();
```

#Setup
```
repositories {
    maven { url "https://jitpack.io" }
}
    
dependencies {
    compile 'com.github.jkwiecien:Switcher:1.0.4'

}
```

If you wan't to use blur view, you also need to modify your app ```defaultConfig```:

```
    defaultConfig {
        renderscriptTargetApi 22
        renderscriptSupportModeEnabled true
    }
```
