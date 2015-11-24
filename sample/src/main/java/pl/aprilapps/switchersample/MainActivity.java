package pl.aprilapps.switchersample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.aprilapps.switcher.OnErrorViewListener;
import pl.aprilapps.switcher.Switcher;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.recycler_view)
    protected RecyclerView recyclerView;

    private Switcher switcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        switcher = new Switcher.Builder(this)
                .addContentView(findViewById(R.id.recycler_view)) //content member
                .addContentView(findViewById(R.id.fab)) //content member
                .addErrorView(findViewById(R.id.error_view)) //error view member
                .addProgressView(findViewById(R.id.progress_view)) //progress view member
                .setErrorLabel((TextView) findViewById(R.id.error_label)) // TextView within your error member group that you want to change
                .setProgressLabel((TextView) findViewById(R.id.progress_label)) // TextView within your progress member group that you want to change
                .addEmptyView(findViewById(R.id.empty_view)) //empty placeholder member
                .build();

        ColorAdapter adapter = new ColorAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public void onProgress() {
        switcher.showProgressView("Loading data. Please wait.");
        Observable.just(new Object())
                .delay(1, TimeUnit.SECONDS)
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

    public void onError() {
        switcher.showProgressView("Loading data. Please wait.");
        Observable.just(new Object())
                .delay(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        switcher.showErrorView("Error. Click this to make it disappear.", new OnErrorViewListener() {
                            @Override
                            public void onErrorViewClicked() {
                                switcher.showContentView();
                            }
                        });
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
