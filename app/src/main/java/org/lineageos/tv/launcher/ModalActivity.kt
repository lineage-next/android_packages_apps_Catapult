package org.lineageos.tv.launcher

import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

abstract class ModalActivity(layout: Int) : AppCompatActivity(layout) {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        val layoutParams = window.attributes.apply{
            gravity = Gravity.TOP or Gravity.END
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.MATCH_PARENT
        }
        window.attributes = layoutParams
    }
}