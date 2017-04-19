package pl.aprilapps.switcher

import android.animation.Animator
import android.content.Context
import android.util.Log
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
    private var logsEnabled = false

    var animationDuration = context.resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

    val runningAnimations = mutableSetOf<Animator>()

    private fun areAllContentViewsVisible(): Boolean {
        contentViews.forEach { if (it.visibility != View.VISIBLE) return false }
        return true
    }

    private fun printLog(message: String) {
        if (logsEnabled) Log.v(javaClass.simpleName, message)
    }

    private fun printErrorLog(message: String) {
        if (logsEnabled) Log.e(javaClass.simpleName, message)
    }

    fun showContentView() {
        printLog("Showing content immediately")
        val screenStateCheck = finishRunningAnimations()
        if (!screenStateCheck || areAllContentViewsVisible()) return
        contentViews.forEach { fadeInView(it, animationDuration) }
        allNonContentViews().filter { it.visibility == View.VISIBLE }.forEach { fadeOutView(it, animationDuration) }
    }

    fun showContentViewImmediately() {
        printLog("Showing content immediately")
        val screenStateCheck = finishRunningAnimations()
        if (!screenStateCheck || areAllContentViewsVisible()) return
        contentViews.forEach { it.visibility = View.VISIBLE }
        allNonContentViews().filter { it.visibility == View.VISIBLE }.forEach { it.visibility = invisibleState }
    }

    fun showProgressView() {
        showProgressView(null)
    }

    fun showProgressViewImmediately() {
        printLog("Showing progress immediately")
        showProgressViewImmediately(null)
    }

    fun showProgressView(progressMessage: String?) {
        printLog("Showing progress")
        val screenStateCheck = finishRunningAnimations()
        if (!screenStateCheck) return
        progressViews.forEach { fadeInView(it, animationDuration) }
        progressMessage?.let { progressLabel?.let { errorLabel!!.text = progressMessage } }
        allNonProgressViews().filter { it.visibility == View.VISIBLE }.forEach { fadeOutView(it, animationDuration) }
    }

    fun showProgressViewImmediately(progressMessage: String?) {
        printLog("Showing progress immediately")
        val screenStateCheck = finishRunningAnimations()
        if (!screenStateCheck) return
        progressViews.forEach { it.visibility = View.VISIBLE }
        progressMessage?.let { progressLabel?.let { errorLabel!!.text = progressMessage } }
        allNonProgressViews().filter { it.visibility == View.VISIBLE }.forEach { it.visibility = invisibleState }
    }

    fun showErrorView() {
        printLog("Showing error")
        showErrorView(null)
    }

    fun showErrorViewImmediately() {
        printLog("Showing error immediately")
        showErrorViewImmediately(null)
    }

    fun showErrorView(errorMessage: String?) {
        printLog("Showing error")
        val screenStateCheck = finishRunningAnimations()
        if (!screenStateCheck) return
        errorViews.forEach { fadeInView(it, animationDuration) }
        errorMessage?.let { errorLabel?.let { errorLabel!!.text = errorMessage } }
        allNonErrorViews().filter { it.visibility == View.VISIBLE }.forEach { fadeOutView(it, animationDuration) }
    }

    fun showErrorViewImmediately(errorMessage: String?) {
        printLog("Showing error immediately")
        val screenStateCheck = finishRunningAnimations()
        if (!screenStateCheck) return
        errorViews.forEach { it.visibility = View.VISIBLE }
        errorMessage?.let { errorLabel?.let { errorLabel!!.text = errorMessage } }
        allNonErrorViews().filter { it.visibility == View.VISIBLE }.forEach { it.visibility = invisibleState }
    }

    fun showEmptyView() {
        printLog("Showing empty view")
        val screenStateCheck = finishRunningAnimations()
        if (!screenStateCheck) return
        emptyViews.forEach { fadeInView(it, animationDuration) }
        allNonEmptyViews().filter { it.visibility == View.VISIBLE }.forEach { fadeOutView(it, animationDuration) }
    }

    fun showEmptyViewImmediately() {
        printLog("Showing empty view immediately")
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
            printLog("Attempt to finish running animations. Currently running: " + runningAnimations.size)
            runningAnimations.forEach(Animator::end)
            return true
        } catch (error: IllegalStateException) {
            printErrorLog(error.message ?: "IllegalStateException - no message")
            return false
        }
    }

    private fun setupViewsInitialVisibility() {
        printLog("=======================================")
        printLog("Building Switcher. Content views: " + contentViews.size + ", error views: " + errorViews.size + ", progress views: " + progressViews.size + ", empty views: " + emptyViews.size)
        allNonContentViews().forEach { it.visibility = invisibleState }
        contentViews.forEach { it.visibility = View.VISIBLE }
    }

    private fun fadeInView(view: View, animationDuration: Long) {
        printLog("Fading IN view: " + view.toString())
        view.visibility = View.VISIBLE
        view.alpha = 0f

        view.animate().alpha(1f).setDuration(animationDuration).setListener(object : Animator.AnimatorListener {

            override fun onAnimationRepeat(animator: Animator) {
            }

            override fun onAnimationEnd(animator: Animator) {
                printLog("fade IN animation ENDED: " + animator.toString())
                val iterator = runningAnimations.iterator()
                iterator.takeIf { it == animator }?.forEach { iterator.remove() }
            }

            override fun onAnimationCancel(animator: Animator) {
            }

            override fun onAnimationStart(animator: Animator) {
                printLog("fade IN animation STARTED: " + animator.toString())
                runningAnimations.add(animator)
            }
        })
    }

    private fun fadeOutView(view: View, animationDuration: Long) {
        view.animate().alpha(0f).setDuration(animationDuration).setListener(object : Animator.AnimatorListener {

            override fun onAnimationRepeat(animator: Animator) {
            }

            override fun onAnimationEnd(animator: Animator) {
                printLog("fade OUT animation ENDED: " + animator.toString())
                val iterator = runningAnimations.iterator()
                iterator.takeIf { it == animator }?.forEach { iterator.remove() }
                view.visibility = invisibleState
            }

            override fun onAnimationCancel(animator: Animator) {
            }

            override fun onAnimationStart(animator: Animator) {
                printLog("fade OUT animation STARTED: " + animator.toString())
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

        fun setLogsEnabled(enabled: Boolean): Builder {
            switcher.logsEnabled = enabled
            return this
        }

        fun build(): Switcher {
            switcher.setupViewsInitialVisibility()
            return switcher
        }
    }

}