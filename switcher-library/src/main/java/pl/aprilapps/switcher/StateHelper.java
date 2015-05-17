package pl.aprilapps.switcher;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Jacek Kwiecień on 17.05.15.
 */
public class StateHelper {

    private SparseArray<Integer> states = new SparseArray<>();

    public void crossfadeViews(final View viewToHide, final View viewToShow) {
        if (viewToShow == null) return;
        int animDuration = viewToShow.getContext().getResources().getInteger(android.R.integer.config_shortAnimTime);

        fadeIn(viewToShow, animDuration);
        fadeOut(viewToHide, animDuration);
    }

    private void fadeIn(final View view, int animDuration) {
        if (view == null) return;

        view.setAlpha(0f);
        setVisibility(view, View.VISIBLE);
        view.animate().alpha(1f).setDuration(animDuration).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setVisibility(view, View.VISIBLE);
            }
        });
    }

    private void fadeOut(final View view, int animDuration) {
        if (view == null) return;

        view.animate().alpha(0f).setDuration(animDuration).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setVisibility(view, View.INVISIBLE);
            }
        });
    }

    public void setVisibility(View view, int visibility) {
        view.setVisibility(visibility);
        states.put(view.getId(), visibility);
    }

    public Integer getVisibility(View view) {
        return states.get(view.getId());
    }

    public View getCurrentlyVisibleView(View viewToShow) {
        states.put(viewToShow.getId(), View.VISIBLE);

        try {
            FrameLayout parent = (FrameLayout) viewToShow.getParent();
            for (int i = 0; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                if (child.equals(viewToShow)) continue;

                int id = child.getId();
                Integer state = states.get(id);
                if (state != null && states.get(id) == View.VISIBLE) return child;
            }
        } catch (ClassCastException e) {

        }
        throw new ClassCastException("All state views (content|error|progress) should have the same FrameLayout parent");
    }
}
