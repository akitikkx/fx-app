package com.ahmedtikiwa.fxapp.adapters

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("loadingVisibility")
fun loadingVisibility(view: View, isLoading: Boolean) {
    view.alpha = if (isLoading) .5f else 1f
}