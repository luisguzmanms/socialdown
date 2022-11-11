package com.lamesa.socialdown.utils

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes

class SDAnimation(val context : Context) {

    fun setSDAnimation(view : View, @AnimRes anim : Int){
        val logoMoveAnimation: Animation = AnimationUtils.loadAnimation(context, anim)
        view.startAnimation(logoMoveAnimation)
    }
    fun setSDAnimation(view : Array<View>, @AnimRes anim : Int){
        for (v in view){
            val logoMoveAnimation: Animation = AnimationUtils.loadAnimation(context, anim)
            v.startAnimation(logoMoveAnimation)
        }
    }

}