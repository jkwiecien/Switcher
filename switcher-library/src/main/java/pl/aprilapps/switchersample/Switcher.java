package pl.aprilapps.switchersample;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Jacek Kwiecień on 14.03.15.
 */
public class Switcher {

    public interface ContentViewOwner {
        public View getContentView();
    }

    public interface ErrorViewOwner {
        public View getErrorView();
    }

    public interface ClickableErrorViewOwner extends ErrorViewOwner {
        public TextView getErrorLabel();

        public void onErrorViewClicked(int errorCode);
    }

    public interface ProgressViewOwner {
        public View getProgressView();
    }

    public interface LabeledProgressViewOwner extends ProgressViewOwner {
        public TextView getProgressViewLabel();
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

    public static void showContentView(ContentViewOwner owner) {
        View viewToShow = owner.getContentView();
        View viewToHide = getCurrentlyVisibleView(viewToShow);

        if (viewToShow != viewToHide && viewToShow.getVisibility() != View.VISIBLE) {
            Animations.crossfadeViews(viewToHide, viewToShow);
        }
    }

    public static void showErrorView(ErrorViewOwner owner) {
        View viewToShow = owner.getErrorView();
        View viewToHide = getCurrentlyVisibleView(viewToShow);

        if (viewToShow != viewToHide && viewToShow.getVisibility() != View.VISIBLE) {
            Animations.crossfadeViews(viewToHide, viewToShow);
        }
    }

    public static void showErrorView(final ClickableErrorViewOwner owner, String errorMessage, int errorCode) {
        owner.getErrorView().setTag(errorCode);
        owner.getErrorLabel().setText(errorMessage);
        owner.getErrorView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                owner.onErrorViewClicked((Integer) view.getTag());
                view.setOnClickListener(null);
            }
        });
        showErrorView(owner);
    }

    public static void showProgressView(ProgressViewOwner owner) {
        View viewToShow = owner.getProgressView();
        View viewToHide = getCurrentlyVisibleView(viewToShow);

        if (viewToShow != viewToHide && viewToShow.getVisibility() != View.VISIBLE) {
            Animations.crossfadeViews(viewToHide, viewToShow);
        }
    }

    public static void showProgressView(LabeledProgressViewOwner owner, String errorMessage) {
        owner.getProgressViewLabel().setText(errorMessage);
        showProgressView(owner);
    }

}
