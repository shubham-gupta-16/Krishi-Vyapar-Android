package com.acoder.krishivyapar.utils

import android.animation.Animator
import android.view.ViewPropertyAnimator

fun ViewPropertyAnimator.setEndListener(listener: (animation: Animator?) -> Unit): ViewPropertyAnimator {
    this.setListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
            listener(animation)
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationRepeat(animation: Animator?) {
        }
    })
    return this
}
