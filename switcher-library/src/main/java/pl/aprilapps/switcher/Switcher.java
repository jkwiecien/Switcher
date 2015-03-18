package pl.aprilapps.switcher;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Jacek Kwiecień on 14.03.15.
 */
public class Switcher {

    private View contentView;
    private View errorView;
    private View progressView;

    private TextView errorLabel;
    private TextView progressLabel;

    private int errorCode;

    private Switcher() {

    }


    private void setContentView(View contentView) {
        this.contentView = contentView;
    }

    private void setErrorView(View errorView) {
        this.errorView = errorView;
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

        public Builder withProgressLabel(TextView progressLabel) {
            switcher.setProgressLabel(progressLabel);
            return this;
        }

        public Builder withErrorLabel(TextView errorLabel) {
            switcher.setErrorLabel(errorLabel);
            return this;
        }

        public Switcher build() {
            return switcher;
        }

    }

    private static View getCurrentlyVisibleView(View viewToShow) {
        try {
            FrameLayout parent = (FrameLayout) viewToShow.getParent();
            for (int i = 0; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                if (child.getVisibility() == View.VISIBLE) return child;
            }
        } catch (ClassCastException e) {

        }
        throw new ClassCastException("All state views (content|error|progress) should have the same FrameLayout parent");
    }

    public void showContentView() {
        View viewToHide = getCurrentlyVisibleView(contentView);

        if (contentView != viewToHide && contentView.getVisibility() != View.VISIBLE) {
            Animations.crossfadeViews(viewToHide, contentView);
        }
    }

    public void showErrorView() {
        View viewToHide = getCurrentlyVisibleView(errorView);

        if (errorView != viewToHide && errorView.getVisibility() != View.VISIBLE) {
            Animations.crossfadeViews(viewToHide, errorView);
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

    public void showProgressView() {
        View viewToHide = getCurrentlyVisibleView(progressView);

        if (progressView != viewToHide && progressView.getVisibility() != View.VISIBLE) {
            Animations.crossfadeViews(viewToHide, progressView);
        }
    }

    public void showProgressView(String errorMessage) {
        if (errorLabel == null) {
            throw new NullPointerException("You have to build Switcher using withProgressLabel() method");
        }

        progressLabel.setText(errorMessage);
        showProgressView();
    }

}
