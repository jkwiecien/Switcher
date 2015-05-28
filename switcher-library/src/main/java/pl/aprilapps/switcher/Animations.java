package pl.aprilapps.switcher;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.util.Log;
import android.util.Pair;
import android.view.View;

/**
 * Created by Jacek Kwiecień on 14.03.15.
 */
public class Animations {

    public static Pair<FadeInListener, FadeOutListener> crossfadeViews(final View viewToHide, final View viewToShow) {
        if (viewToShow == null)
            return new Pair<FadeInListener, FadeOutListener>(null, null);
        int animDuration = viewToShow.getContext().getResources().getInteger(android.R.integer.config_shortAnimTime);


        FadeInListener fadeInListener = fadeIn(viewToShow, animDuration);
        FadeOutListener fadeOutListener = fadeOut(viewToHide, animDuration);

        return new Pair<FadeInListener, FadeOutListener>(fadeInListener, fadeOutListener);
    }

    public static FadeInListener fadeIn(final View view, int animDuration) {
        if (view == null) return null;

        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);

        FadeInListener listener = new FadeInListener(view);

        view.animate().alpha(1f).setDuration(animDuration).setListener(listener);

        return listener;
    }

    public static FadeOutListener fadeOut(final View view, int animDuration) {
        if (view == null) return null;

        FadeOutListener listener = new FadeOutListener(view);

        view.animate().alpha(0f).setDuration(animDuration).setListener(listener);

        return listener;
    }

    public static class FadeInListener extends AnimatorListenerAdapter {

        private View view;

        public FadeInListener(View view) {
            this.view = view;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            onAnimationEnd();
        }

        public void onAnimationEnd() {
            Log.i(Switcher.class.getSimpleName(), String.format("fadeIn END: %s", view.getContext().getResources().getResourceName(view.getId())));
            view.setVisibility(View.VISIBLE);
        }
    }

    public static class FadeOutListener extends AnimatorListenerAdapter {

        private View view;

        public FadeOutListener(View view) {
            this.view = view;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            onAnimationEnd();
        }

        public void onAnimationEnd() {
            Log.i(Switcher.class.getSimpleName(), String.format("fadeOut END: %s", view.getContext().getResources().getResourceName(view.getId())));
            view.setVisibility(View.INVISIBLE);
        }
    }
}
