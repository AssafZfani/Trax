package zfani.assaf.trax.utils

import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import zfani.assaf.trax.R

fun Fragment.toast(@StringRes res: Int) {
    Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
}

fun Fragment.showLoading() {
    activity?.findViewById<ProgressBar>(R.id.progress_bar)?.let {
        if (it.isGone) {
            activity?.window?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            it.isVisible = true
        }
    }
}

fun Fragment.hideLoading() {
    activity?.findViewById<ProgressBar>(R.id.progress_bar)?.let {
        if (it.isVisible) {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            it.isVisible = false
        }
    }
}