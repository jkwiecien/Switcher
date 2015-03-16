package pl.aprilapps.switchersample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends ActionBarActivity implements Switcher.LabeledProgressViewOwner, Switcher.ContentViewOwner, Switcher.ClickableErrorViewOwner {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.progress_button)
    protected void onProgress() {
        Switcher.showProgressView(this, "Loading data. Please wait.");
        Observable.just(new Object())
                .delay(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        Switcher.showContentView(MainActivity.this);
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
        Switcher.showProgressView(this, "Loading data. Please wait.");
        Observable.just(new Object())
                .delay(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        Switcher.showErrorView(MainActivity.this, "Error. Click this to make it dissapear.", 0);
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
    public TextView getProgressViewLabel() {
        return (TextView) findViewById(R.id.error_label);
    }

    @Override
    public View getProgressView() {
        return findViewById(R.id.progress_view);
    }

    @Override
    public View getContentView() {
        return findViewById(R.id.content);
    }

    @Override
    public TextView getErrorLabel() {
        return (TextView) findViewById(R.id.error_label);
    }

    @Override
    public void onErrorViewClicked(int errorCode) {
        Switcher.showContentView(this);
    }

    @Override
    public View getErrorView() {
        return findViewById(R.id.error_view);
    }
}
