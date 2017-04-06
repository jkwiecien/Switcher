package pl.aprilapps.switcher

import android.animation.Animator
import android.content.Context
import android.view.View
import android.widget.TextView
import java.lang.IllegalStateException

/**
 * Created by Jacek Kwiecień on 04.03.2017.
 */
open class Switcher(context: Context) {

    private val contentViews = mutableSetOf<View>()
    private val progressViews = mutableSetOf<View>()
    private val errorViews = mutableSetOf<View>()
    private val emptyViews = mutableSetOf<View>()
    private var progressLabel: TextView? = null
    private var errorLabel: TextView? = null
    private var invisibleState = View.INVISIBLE

    var animationDuration = context.resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

    val runningAnimations = mutableSetOf<Animator>()

    fun showContentView() {
        val screenStateCheck = finishRunningAnimations()
        if (!screenStateCheck) return
        contentViews.forEach { fadeInView(it, animationDuration) }
        allNonContentViews().filter { it.visibility == View.VISIBLE }.forEach { fadeOutView(it, animationDuration) }
    }

    fun showContentViewImmediately() {
        val screenStateCheck = finishRunningAnimations()
        if (!screenStateCheck) return
        contentViews.forEach { it.visibility = View.VISIBLE }
        allNonContentViews().filter { it.visibility == View.VISIBLE }.forEach { it.visibility = invisibleState }
    }

    fun showProgressView() {
        showProgressView(null)
    }

    fun showProgressViewImmediately() {
        showProgressViewImmediately(null)
    }

    fun showProgressView(progressMessage: String?) {
        val screenStateCheck = finishRunningAnimations()
        if (!screenStateCheck) return
        progressViews.forEach { fadeInView(it, animationDuration) }
        progressMessage?.let { progressLabel?.let { errorLabel!!.text = progressMessage } }
        allNonProgressViews().filter { it.visibility == View.VISIBLE }.forEach { fadeOutView(it, animationDuration) }
    }

    fun showProgressViewImmediately(progressMessage: String?) {
        val screenStateCheck = finishRunningAnimations()
        if (!screenStateCheck) return
        progressViews.forEach { it.visibility = View.VISIBLE }
        progressMessage?.let { progressLabel?.let { errorLabel!!.text = progressMessage } }
        allNonProgressViews().filter { it.visibility == View.VISIBLE }.forEach { it.visibility = invisibleState }
    }

    fun showErrorView() {
        showErrorView(null)
    }

    fun showErrorViewImmediately() {
        showErrorViewImmediately(null)
    }

    fun showErrorView(errorMessage: String?) {
        val screenStateCheck = finishRunningAnimations()
        if (!screenStateCheck) return
        errorViews.forEach { fadeInView(it, animationDuration) }
        errorMessage?.let { errorLabel?.let { errorLabel!!.text = errorMessage } }
        allNonErrorViews().filter { it.visibility == View.VISIBLE }.forEach { fadeOutView(it, animationDuration) }
    }

    fun showErrorViewImmediately(errorMessage: String?) {
        val screenStateCheck = finishRunningAnimations()
        if (!screenStateCheck) return
        errorViews.forEach { it.visibility = View.VISIBLE }
        errorMessage?.let { errorLabel?.let { errorLabel!!.text = errorMessage } }
        allNonErrorViews().filter { it.visibility == View.VISIBLE }.forEach { it.visibility = invisibleState }
    }

    fun showEmptyView() {
        val screenStateCheck = finishRunningAnimations()
        if (!screenStateCheck) return
        emptyViews.forEach { fadeInView(it, animationDuration) }
        allNonEmptyViews().filter { it.visibility == View.VISIBLE }.forEach { fadeOutView(it, animationDuration) }
    }

    fun showEmptyViewImmediately() {
        val screenStateCheck = finishRunningAnimations()
        if (!screenStateCheck) return
        emptyViews.forEach { it.visibility = View.VISIBLE }
        allNonEmptyViews().filter { it.visibility == View.VISIBLE }.forEach { it.visibility = invisibleState }
    }

    private fun allNonContentViews(): MutableSet<View> {
        val set = mutableSetOf<View>()
        set.addAll(progressViews)
        set.addAll(errorViews)
        set.addAll(emptyViews)
        return set
    }

    private fun allNonErrorViews(): MutableSet<View> {
        val set = mutableSetOf<View>()
        set.addAll(contentViews)
        set.addAll(progressViews)
        set.addAll(emptyViews)
        return set
    }

    private fun allNonEmptyViews(): MutableSet<View> {
        val set = mutableSetOf<View>()
        set.addAll(contentViews)
        set.addAll(progressViews)
        set.addAll(errorViews)
        return set
    }

    private fun allNonProgressViews(): MutableSet<View> {
        val set = mutableSetOf<View>()
        set.addAll(contentViews)
        set.addAll(errorViews)
        set.addAll(emptyViews)
        return set
    }

    private fun finishRunningAnimations(): Boolean {
        try {
            runningAnimations.forEach(Animator::end)
            return true
        } catch (illegalStateException: IllegalStateException) {
            illegalStateException.printStackTrace()
            return false
        }
    }

    private fun setupViewsInitialVisibility() {
        allNonContentViews().forEach { it.visibility = invisibleState }
        contentViews.forEach { it.visibility = View.VISIBLE }
    }

    private fun fadeInView(view: View, animationDuration: Long) {
        view.visibility = View.VISIBLE
        view.alpha = 0f

        view.animate().alpha(1f).setDuration(animationDuration).setListener(object : Animator.AnimatorListener {

            override fun onAnimationRepeat(animator: Animator) {
            }

            override fun onAnimationEnd(animator: Animator) {
                val iterator = runningAnimations.iterator()
                iterator.takeIf { it == animator }?.forEach { iterator.remove() }
            }

            override fun onAnimationCancel(animator: Animator) {
            }

            override fun onAnimationStart(animator: Animator) {
                runningAnimations.add(animator)
            }
        })
    }

    private fun fadeOutView(view: View, animationDuration: Long) {
        view.animate().alpha(0f).setDuration(animationDuration).setListener(object : Animator.AnimatorListener {

            override fun onAnimationRepeat(animator: Animator) {
            }

            override fun onAnimationEnd(animator: Animator) {
                val iterator = runningAnimations.iterator()
                iterator.takeIf { it == animator }?.forEach { iterator.remove() }
                view.visibility = invisibleState
            }

            override fun onAnimationCancel(animator: Animator) {
            }

            override fun onAnimationStart(animator: Animator) {
                runningAnimations.add(animator)
            }
        })
    }


    open class Builder(context: Context) {
        val switcher = Switcher(context)

        fun setAnimDuration(millis: Long): Builder {
            switcher.animationDuration = millis
            return this
        }

        fun setInvisibleStateToGone() {
            switcher.invisibleState = View.GONE
        }

        fun setErrorLabel(errorLabel: TextView): Builder {
            if (errorLabel == null) throw NullPointerException("Non-null param cannot be null")
            switcher.errorLabel = errorLabel
            return this
        }

        fun setProgressLabel(progressLabel: TextView): Builder {
            if (progressLabel == null) throw NullPointerException("Non-null param cannot be null")
            switcher.progressLabel = progressLabel
            return this
        }

        fun addContentView(view: View): Builder {
            if (view == null) throw NullPointerException("Non-null param cannot be null")
            switcher.contentViews.add(view)
            return this
        }

        fun addProgressView(view: View): Builder {
            if (view == null) throw NullPointerException("Non-null param cannot be null")
            switcher.progressViews.add(view)
            return this
        }

        fun addErrorView(view: View): Builder {
            if (view == null) throw NullPointerException("Non-null param cannot be null")
            switcher.errorViews.add(view)
            return this
        }

        fun addEmptyView(view: View): Builder {
            if (view == null) throw NullPointerException("Non-null param cannot be null")
            switcher.emptyViews.add(view)
            return this
        }

        fun build(): Switcher {
            switcher.setupViewsInitialVisibility()
            return switcher
        }
    }

}