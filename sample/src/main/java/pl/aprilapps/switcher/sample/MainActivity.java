package pl.aprilapps.switcher.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.aprilapps.switcher.Switcher;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.recyclerView)
    protected RecyclerView recyclerView;

    private Switcher switcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        switcher = new Switcher.Builder(this)
                .addContentView(findViewById(R.id.recyclerView)) //content member
                .addContentView(findViewById(R.id.fab)) //content member
                .addErrorView(findViewById(R.id.errorView)) //error view member
                .addProgressView(findViewById(R.id.progressView)) //progress view member
                .setErrorLabel((TextView) findViewById(R.id.errorLabel)) // TextView within your error member group that you want to change
                .setProgressLabel((TextView) findViewById(R.id.progressLabel)) // TextView within your progress member group that you want to change
                .addEmptyView(findViewById(R.id.emptyView)) //empty placeholder member
                .setAnimDuration(600)
                .setLogsEnabled(true)
                .build();

        ColorAdapter adapter = new ColorAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public void onProgress() {
        switcher.showProgressView();
        Observable.just(new Object())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .delaySubscription(300, TimeUnit.MILLISECONDS)
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

    public void onError() {
        switcher.showProgressView();
        Observable.just(new Object())
                .subscribeOn(Schedulers.newThread())
                .delaySubscription(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        switcher.showErrorView();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });
    }

    @OnClick(R.id.dismissErrorButton)
    public void onDismissErrorClicked() {
        switcher.showContentView();
    }

    @OnClick(R.id.dismissEmptyButton)
    public void onDismissEmptyClicked() {
        switcher.showContentView();
    }

    public void onEmpty() {
        switcher.showEmptyView();
    }
}
