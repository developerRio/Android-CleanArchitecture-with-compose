package com.originalstocks.llabank.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.textfield.TextInputEditText
import com.originalstocks.llabank.R

object Utils {
    const val RETRO_CACHE_SIZE: Long = 10 * 1024 * 1024 // 10MB

    fun ImageView.loadImageWithGlide(url: String?, placeholder: Int = R.drawable.round_yard_24) {
        url?.let {
            Glide.with(this)
                .load(url)
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .placeholder(placeholder)
                .into(this)
        }
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun closeSoftInput(context: Context, currentFocus: View?) {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    fun TextInputEditText.addOnTextChangeListener(afterOnTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                afterOnTextChanged.invoke(s.toString())
            }
        })
    }

}