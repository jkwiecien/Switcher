package pl.aprilapps.switcher;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Jacek Kwiecień on 14.03.15.
 */
public class Switcher {

    private View contentView;
    private View errorView;
    private View progressView;
    private View blurView;

    private TextView errorLabel;
    private TextView progressLabel;

    private int errorCode;
    private StateHelper stateHelper = new StateHelper();

    private Switcher() {

    }

    private void setContentView(final View contentView) {
        this.contentView = contentView;
    }

    private void setErrorView(View errorView) {
        this.errorView = errorView;
    }

    public void setBlurView(View blurView) {
        this.blurView = blurView;
    }

    private void setProgressView(View progressView) {
        this.progressView = progressView;
    }

    private void setErrorLabel(TextView errorLabel) {
        this.errorLabel = errorLabel;
    }

    private void setProgressLabel(TextView progressLabel) {
        this.progressLabel = progressLabel;
    }

    public static class Builder {

        private Switcher switcher = new Switcher();

        public Builder withContentView(View contentView) {
            switcher.setContentView(contentView);
            return this;
        }

        public Builder withErrorView(View errorView) {
            switcher.setErrorView(errorView);
            return this;
        }

        public Builder withProgressView(View progressView) {
            switcher.setProgressView(progressView);
            return this;
        }

        public Builder withBlurView(View blurView) {
            switcher.setBlurView(blurView);
            return this;
        }

        public Builder withProgressLabel(TextView progressLabel) {
            switcher.setProgressLabel(progressLabel);
            return this;
        }

        public Builder withErrorLabel(TextView errorLabel) {
            switcher.setErrorLabel(errorLabel);
            return this;
        }

        public Switcher build() {
            switcher.setupViews();
            return switcher;
        }
    }

    private void setupViews() {
        if (contentView != null) stateHelper.setVisibility(contentView, View.VISIBLE);
        if (errorView != null) stateHelper.setVisibility(errorView, View.INVISIBLE);
        if (progressView != null) stateHelper.setVisibility(progressView, View.INVISIBLE);
        if (blurView != null) stateHelper.setVisibility(blurView, View.INVISIBLE);
    }


    public void showContentView() {
        View viewToHide = stateHelper.getCurrentlyVisibleView(contentView);

        if (contentView != viewToHide) {
            stateHelper.crossfadeViews(viewToHide, contentView);
        }
    }

    public void showProgressView() {
        View viewToHide = stateHelper.getCurrentlyVisibleView(progressView);

        if (progressView != viewToHide) {
            stateHelper.crossfadeViews(viewToHide, progressView);
        }
    }

    public void showErrorView() {
        View viewToHide = stateHelper.getCurrentlyVisibleView(errorView);

        if (errorView != viewToHide) {
            stateHelper.crossfadeViews(viewToHide, errorView);
        }
    }

    public void showBlurView(View viewToBlur) {
        View viewToHide = stateHelper.getCurrentlyVisibleView(blurView);

        if (blurView != viewToHide) {
            Bitmap blurBitmap = BlurUtils.takeBlurredScreenshot(viewToBlur);
            blurView.setBackgroundDrawable(new BitmapDrawable(blurView.getResources(), blurBitmap));
            stateHelper.crossfadeViews(viewToHide, blurView);
        }
    }

    public void showErrorView(String errorMessage) {
        if (errorLabel == null) {
            throw new NullPointerException("You have to build Switcher using withErrorLabel() method");
        }

        errorLabel.setText(errorMessage);
        showErrorView();
    }

    public void showErrorView(final OnErrorViewListener listener, final int errorCode) {
        this.errorCode = errorCode;

        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onErrorViewClicked(errorCode);
                view.setOnClickListener(null);
            }
        });
        showErrorView();
    }

    public void showErrorView(String errorMessage, final OnErrorViewListener listener, final int errorCode) {
        if (errorLabel == null) {
            throw new NullPointerException("You have to build Switcher using withErrorLabel() method");
        }

        errorLabel.setText(errorMessage);
        showErrorView(listener, errorCode);
    }


    public void showProgressView(String errorMessage) {
        if (errorLabel == null) {
            throw new NullPointerException("You have to build Switcher using withProgressLabel() method");
        }

        progressLabel.setText(errorMessage);
        showProgressView();
    }

}
