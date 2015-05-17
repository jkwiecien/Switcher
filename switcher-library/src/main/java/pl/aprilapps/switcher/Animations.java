package pl.aprilapps.switcher;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

/**
 * Created by Jacek Kwiecień on 14.03.15.
 */
public class Animations {

    public static void crossfadeViews(final View viewToHide, final View viewToShow) {
        if (viewToShow == null) return;
        int animDuration = viewToShow.getContext().getResources().getInteger(android.R.integer.config_shortAnimTime);

        fadeIn(viewToShow, animDuration);
        fadeOut(viewToHide, animDuration);
    }

    public static void crossfadeViews(final View viewToHide, final View viewToShow, int animDuration) {
        if (viewToShow == null) return;

        fadeIn(viewToShow, animDuration);
        fadeOut(viewToHide, animDuration);
    }

    public static void fadeIn(final View view, int animDuration) {
        if (view == null) return;

        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        view.animate().alpha(1f).setDuration(animDuration).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }
        });
    }

    public static void fadeOut(final View view, int animDuration) {
        if (view == null) return;

        view.animate().alpha(0f).setDuration(animDuration).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.INVISIBLE);
            }
        });
    }

}
