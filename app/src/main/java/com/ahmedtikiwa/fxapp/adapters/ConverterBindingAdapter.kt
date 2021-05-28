package com.ahmedtikiwa.fxapp.adapters

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ahmedtikiwa.fxapp.R
import com.ahmedtikiwa.fxapp.domain.Conversion

@BindingAdapter("showHideView")
fun showHideView(view: View, show: Boolean) {
    if (show) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }
}

@BindingAdapter("conversion")
fun conversion(view: TextView, conversion: Conversion?) {
    if (conversion != null) {
        view.text = view.resources.getString(
            R.string.conversion_response,
            conversion.from,
            conversion.to,
            conversion.total
        )
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }
}