package pl.aprilapps.switcher;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.util.Log;
import android.view.View;

/**
 * Created by Jacek Kwiecień on 14.03.15.
 */
public class Animations {

    public static CrossfadeListeners crossfadeViews(final View viewToHide, final View viewToShow) {
        return crossfadeViews(viewToHide, viewToShow);
    }

    public static CrossfadeListeners crossfadeViews(final View viewToHide, final View viewToShow, int animDuration) {
        if (viewToShow == null) return null;
        FadeInListener fadeInListener = fadeIn(viewToShow, animDuration);
        FadeOutListener fadeOutListener = fadeOut(viewToHide, animDuration);
        return new CrossfadeListeners(fadeOutListener, fadeInListener);
    }

    public static FadeInListener fadeIn(final View view) {
        int animDuration = view.getContext().getResources().getInteger(android.R.integer.config_shortAnimTime);
        return fadeIn(view, animDuration);
    }

    public static FadeOutListener fadeOut(final View view) {
        int animDuration = view.getContext().getResources().getInteger(android.R.integer.config_shortAnimTime);
        return fadeOut(view, animDuration);
    }

    public static FadeInListener fadeIn(final View view, int animDuration) {
        if (view == null || view.getVisibility() == View.VISIBLE) return null;

        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);

        FadeInListener listener = new FadeInListener(view);

        view.animate().alpha(1f).setDuration(animDuration).setListener(listener);

        return listener;
    }

    public static FadeOutListener fadeOut(final View view, int animDuration) {
        if (view == null || view.getVisibility() == View.INVISIBLE) return null;

        FadeOutListener listener = new FadeOutListener(view);

        view.animate().alpha(0f).setDuration(animDuration).setListener(listener);

        return listener;
    }

    public static abstract class FadeListener extends AnimatorListenerAdapter {
        protected View view;

        public FadeListener(View view) {
            this.view = view;
        }

        public abstract void onAnimationEnd();
    }

    public static class FadeInListener extends FadeListener {

        public FadeInListener(View view) {
            super(view);
        }

        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            Log.i(Switcher.class.getSimpleName(), String.format("fadeIn START: %s", view.getContext().getResources().getResourceName(view.getId())));
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            onAnimationEnd();
        }

        public void onAnimationEnd() {
            if (view.getVisibility() != View.VISIBLE) {
                Log.i(Switcher.class.getSimpleName(), String.format("fadeIn END: %s", view.getContext().getResources().getResourceName(view.getId())));
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    public static class FadeOutListener extends FadeListener {

        public FadeOutListener(View view) {
            super(view);
        }

        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            Log.i(Switcher.class.getSimpleName(), String.format("fadeOut START: %s", view.getContext().getResources().getResourceName(view.getId())));
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            onAnimationEnd();
        }

        public void onAnimationEnd() {
            if (view.getVisibility() == View.VISIBLE) {
                Log.i(Switcher.class.getSimpleName(), String.format("fadeOut END: %s", view.getContext().getResources().getResourceName(view.getId())));
                view.setVisibility(View.INVISIBLE);
            }
        }
    }

    public static class CrossfadeListeners {
        private FadeOutListener fadeOutListener;
        private FadeInListener fadeInListener;

        public CrossfadeListeners(FadeOutListener fadeOutListener, FadeInListener fadeInListener) {
            this.fadeOutListener = fadeOutListener;
            this.fadeInListener = fadeInListener;
        }

        public FadeOutListener getFadeOutListener() {
            return fadeOutListener;
        }

        public FadeInListener getFadeInListener() {
            return fadeInListener;
        }
    }
}
