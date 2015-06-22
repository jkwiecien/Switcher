package pl.aprilapps.switchersample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import pl.aprilapps.switcher.OnErrorViewListener;
import pl.aprilapps.switcher.Switcher;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends ActionBarActivity implements OnErrorViewListener {

    private Switcher switcher;
    @InjectView(R.id.content)
    View content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        switcher = new Switcher.Builder()
                .withContentView(findViewById(R.id.content)) //ViewGroup holding your main content
                .withErrorView(findViewById(R.id.error_view)) //ViewGroup holding your error view
                .withProgressView(findViewById(R.id.progress_view)) //ViewGroup holding your progress view
                .withBlurView(findViewById(R.id.blur_view)) //View overlaying another view, that you'd like to blur
                .withErrorLabel((TextView) findViewById(R.id.error_label)) // TextView within your error ViewGroup that you want to change
                .withProgressLabel((TextView) findViewById(R.id.progress_label)) // TextView within your progress ViewGroup that you want to change
                .build();
    }

    @OnClick(R.id.progress_button)
    protected void onProgress() {
        switcher.showProgressView("Loading data. Please wait.");
        Observable.just(new Object())
                .delay(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        switcher.showContentView();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });

    }

    @OnClick(R.id.error_button)
    protected void onError() {
        switcher.showProgressView("Loading data. Please wait.");
        Observable.just(new Object())
                .delay(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        switcher.showErrorView("Error. Click this to make it dissapear.", MainActivity.this);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });
    }

    @OnClick(R.id.blur_button)
    protected void onBlur() {
        switcher.showBlurView(content);
        Observable.just(new Object())
                .delay(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        switcher.showContentView();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });
    }


    @Override
    public void onErrorViewClicked() {
        switcher.showContentView();
    }
}
