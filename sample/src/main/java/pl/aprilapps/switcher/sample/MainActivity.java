package pl.aprilapps.switcher.sample;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;
import pl.aprilapps.switcher.Switcher;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private Switcher switcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(findViewById(R.id.toolbar));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        switcher = new Switcher.Builder(this)
                .addContentView(findViewById(R.id.recyclerView)) //content member
                .addContentView(findViewById(R.id.fab)) //content member
                .addErrorView(findViewById(R.id.errorView)) //error view member
                .addProgressView(findViewById(R.id.progressView)) //progress view member
                .setErrorLabel((TextView) findViewById(R.id.errorLabel)) // TextView within your error member group that you want to change
                .setProgressLabel((TextView) findViewById(R.id.progressLabel)) // TextView within your progress member group that you want to change
                .addEmptyView(findViewById(R.id.emptyView)) //empty placeholder member
                .setAnimDuration(400)
                .setLogsEnabled(true)
                .build();

        ColorAdapter adapter = new ColorAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        findViewById(R.id.dismissErrorButton).setOnClickListener(view -> {
            switcher.showContentView();
        });

    }

    public void onProgress() {
        switcher.showProgressViewImmediately();
        Observable.just(new Object())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .delaySubscription(800, TimeUnit.MILLISECONDS)
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

    public void onEmpty() {
        switcher.showEmptyView();
    }
}
