package com.ahmedtikiwa.fxapp.adapters

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("itemPrice")
fun itemPrice(view: TextView, price: Double?) {
    if (price != null) {
        view.text = price.toString()
    } else {
        view.text = "-"
    }
}