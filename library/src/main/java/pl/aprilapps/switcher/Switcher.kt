package pl.aprilapps.switcher

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.TextView

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

    var animatorSet = AnimatorSet()
    val runningAnimators = mutableListOf<Animator>()

    private fun areAllContentViewsVisible(): Boolean {
        contentViews.forEach { if (it.visibility != View.VISIBLE) return false }
        return true
    }

    private fun areAllProgressViewsVisible(): Boolean {
        progressViews.forEach { if (it.visibility != View.VISIBLE) return false }
        return true
    }

    private fun areAllErrorViewsVisible(): Boolean {
        errorViews.forEach { if (it.visibility != View.VISIBLE) return false }
        return true
    }

    private fun areAllEmptyViewsVisible(): Boolean {
        emptyViews.forEach { if (it.visibility != View.VISIBLE) return false }
        return true
    }

    fun startAnimationsPackage(animators: List<Animator>) {
        printLog("ENDING currently running animations: " + runningAnimators.size)
        runningAnimators.forEach { it.end() }
        runningAnimators.clear()
        animatorSet = AnimatorSet()
        animatorSet.playTogether(animators)
        printLog("STARTING animations package: " + animators.size + " animators.")
        animatorSet.start()
    }

    private fun printLog(message: String) {
        if (logsEnabled) Log.v(javaClass.simpleName, message)
    }

    private fun printWarningLog(message: String) {
        if (logsEnabled) Log.w(javaClass.simpleName, message)
    }


    private fun printErrorLog(message: String) {
        if (logsEnabled) Log.e(javaClass.simpleName, message)
    }

    private fun fadeInAnimator(view: View, animationDuration: Long): Animator {
        val animator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).setDuration(animationDuration)
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animator: Animator) {
                animator.removeAllListeners()
                val iterator = runningAnimators.iterator()
                iterator.takeIf { it == animator }?.forEach { iterator.remove() }
                printLog("fade IN animation ENDED: " + animator.toString())
            }

            override fun onAnimationStart(animator: Animator) {
                printLog("fade IN animation STARTED: " + animator.toString())
                view.visibility = View.VISIBLE
            }
        })

        runningAnimators.add(animator)
        return animator
    }

    private fun fadeOutAnimator(view: View, animationDuration: Long): Animator {
        val animator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f).setDuration(animationDuration)
        animator.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationEnd(animator: Animator) {
                animator.removeAllListeners()
                val iterator = runningAnimators.iterator()
                iterator.takeIf { it == animator }?.forEach { iterator.remove() }
                printLog("fade OUT animation ENDED: " + animator.toString())
                view.visibility = invisibleState
            }

            override fun onAnimationStart(animator: Animator) {
                printLog("fade OUT animation STARTED: " + animator.toString())
            }
        })

        runningAnimators.add(animator)
        return animator
    }

    //    ===================   CONTENT   ===================

    private fun showContentView(duration: Long) {
        if (areAllContentViewsVisible()) {
            printWarningLog("All content views are currently visible. Aborting.")
            return
        }

        val animators = mutableListOf<Animator>()
                .plus(contentViews.map { fadeInAnimator(it, duration) })
                .plus(allNonContentViews().filter { it.visibility != invisibleState }.map { fadeOutAnimator(it, duration) })
        startAnimationsPackage(animators)
    }

    fun showContentView() {
        printLog("Showing content")
        showContentView(animationDuration)
    }

    fun showContentViewImmediately() {
        printLog("Showing content immediately")
        showContentView(0)
    }

    //    ===================   PROGRESS   ===================

    private fun showProgressView(duration: Long, progressMessage: String?) {
        progressMessage?.let { progressLabel?.let { progressLabel!!.text = progressMessage } }

        if (areAllProgressViewsVisible()) {
            printWarningLog("All progress views are currently visible. Aborting.")
            return
        }

        val animators = mutableListOf<Animator>()
                .plus(progressViews.map { fadeInAnimator(it, duration) })
                .plus(allNonProgressViews().filter { it.visibility != invisibleState }.map { fadeOutAnimator(it, duration) })
        startAnimationsPackage(animators)
    }

    fun showProgressView() {
        showProgressView(animationDuration, null)
    }

    fun showProgressViewImmediately() {
        printLog("Showing progress immediately")
        showProgressView(0, null)
    }

    fun showProgressView(progressMessage: String?) {
        printLog("Showing progress")
        showProgressView(animationDuration, progressMessage)
    }

    fun showProgressViewImmediately(progressMessage: String?) {
        printLog("Showing progress immediately")
        showProgressView(0, progressMessage)
    }


    //    ===================   ERROR   ===================

    private fun showErrorView(duration: Long, errorMessage: String?) {
        errorMessage?.let { errorLabel?.let { errorLabel!!.text = errorMessage } }

        if (areAllErrorViewsVisible()) {
            printWarningLog("All error views are currently visible. Aborting.")
            return
        }

        val animators = mutableListOf<Animator>()
                .plus(errorViews.map { fadeInAnimator(it, duration) })
                .plus(allNonErrorViews().filter { it.visibility != invisibleState }.map { fadeOutAnimator(it, duration) })
        startAnimationsPackage(animators)
    }

    fun showErrorView() {
        printLog("Showing error")
        showErrorView(animationDuration, null)
    }

    fun showErrorViewImmediately() {
        printLog("Showing error immediately")
        showErrorView(0, null)
    }

    fun showErrorView(errorMessage: String?) {
        printLog("Showing error")
        showErrorView(animationDuration, errorMessage)
    }

    fun showErrorViewImmediately(errorMessage: String?) {
        printLog("Showing error immediately")
        showErrorView(0, errorMessage)
    }

    //    ===================   ERROR   ===================

    private fun showEmptyView(duration: Long) {
        if (areAllEmptyViewsVisible()) {
            printWarningLog("All empty views are currently visible. Aborting.")
            return
        }

        val animators = mutableListOf<Animator>()
        animators.addAll(emptyViews.map { fadeInAnimator(it, duration) })
        animators.addAll(allNonEmptyViews().map { fadeOutAnimator(it, duration) })
        startAnimationsPackage(animators)
    }

    fun showEmptyView() {
        printLog("Showing empty view")
        showEmptyView(animationDuration)
    }

    fun showEmptyViewImmediately() {
        printLog("Showing empty view immediately")
        showEmptyView(0)
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

    private fun setupViewsInitialVisibility() {
        printLog("=======================================")
        printLog("Building Switcher. Content views: " + contentViews.size + ", error views: " + errorViews.size + ", progress views: " + progressViews.size + ", empty views: " + emptyViews.size)
        contentViews.plus(progressViews).plus(errorViews).plus(emptyViews).forEach { it.visibility = View.VISIBLE }
        allNonContentViews().forEach { it.visibility = invisibleState }
        contentViews.forEach { it.visibility = View.VISIBLE }
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
            switcher.errorLabel = errorLabel
            return this
        }

        fun setProgressLabel(progressLabel: TextView): Builder {
            switcher.progressLabel = progressLabel
            return this
        }

        fun addContentView(view: View): Builder {
            switcher.contentViews.add(view)
            return this
        }

        fun addProgressView(view: View): Builder {
            switcher.progressViews.add(view)
            return this
        }

        fun addErrorView(view: View): Builder {
            switcher.errorViews.add(view)
            return this
        }

        fun addEmptyView(view: View): Builder {
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