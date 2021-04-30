package com.ininmm.verification_code

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.InputType
import android.util.AttributeSet
import android.view.Gravity
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.inputmethod.BaseInputConnection
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView

/**
 * Created by Michael.Lien
 * on 4/30/21
 */
class VerificationCodeTextView : LinearLayout {

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initial(context, attrs, 0)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        initial(context, attrs, defStyleAttr)
    }

    private lateinit var verificationCodeString: StringBuilder

    private lateinit var verifyCodeTextList: MutableList<TextView>

    /**
     * Sets or Gets the text to be displayed.
     * @attr
     */
    var text
        get() = if (!::verificationCodeString.isInitialized) "" else verificationCodeString.toString()
        set(value) {
            if (!::verificationCodeString.isInitialized) {
                verificationCodeString = StringBuilder(value)
            } else {
                verificationCodeString.clear()
                verificationCodeString.append(value)
            }
        }

    var textSize = 0f

    var textColor = Color.TRANSPARENT

    var textFocusIcon: Drawable? = null

    var textUnFocusIcon: Drawable? = null

    var viewCount = 0

    var textSpaceWidth = 0

    var verifyCodeWidth = 0

    var verifyCodeHeight = 0

    private fun initial(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER
        isFocusableInTouchMode = true

        verificationCodeString = StringBuilder()
        verifyCodeTextList = mutableListOf()

        setupAttribute(attrs, defStyleAttr)
        setupVerifyItemCode()
    }

    private fun setupAttribute(attrs: AttributeSet?, defStyleAttr: Int) {
        TODO("Not yet implemented")
    }

    private fun setupVerifyItemCode() {
        verifyCodeTextList.forEach {
            removeView(it)
        }
        verifyCodeTextList.clear()
        for (index in 0 until viewCount) {
            val itemView = if (index == 0) {
                createVerifyItemView(textFocusIcon)
            } else {
                createVerifyItemView(textUnFocusIcon)
            }
            verifyCodeTextList.add(itemView)
            addView(itemView)
        }
    }

    private fun createVerifyItemView(drawable: Drawable?): TextView {
        val textView = TextView(context)
        textView.apply {
            textSize = this@VerificationCodeTextView.textSize
            gravity = Gravity.CENTER
            setTextColor(this@VerificationCodeTextView.textColor)
            val textPadding = textSpaceWidth / 2
            setPadding(textPadding, 0, textPadding, 0)
        }
        setItemViewBackground(textView, drawable)
        return textView
    }

    private fun setItemViewBackground(textView: TextView, drawable: Drawable?) {
        // TODO: 4/30/21
    }

    override fun onCreateInputConnection(outAttrs: EditorInfo?): InputConnection {
        val connection = BaseInputConnection(this, false)
        outAttrs?.apply {
            actionLabel = null
            inputType = InputType.TYPE_CLASS_NUMBER
            imeOptions = EditorInfo.IME_ACTION_NONE
        }
        return connection
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        // TODO: 4/30/21 handle long click
        //  https://stackoverflow.com/a/32052993/5643911
        requestFocus()
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            // open keyboard
            val imm: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(this, InputMethodManager.SHOW_FORCED)
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (!::verificationCodeString.isInitialized) verificationCodeString = StringBuilder()
        if (keyCode == KeyEvent.KEYCODE_DEL && verificationCodeString.isNotEmpty()) {
            verificationCodeString.deleteCharAt(verificationCodeString.length - 1)
            // TODO: 4/30/21 refresh view
        } else if (event?.number?.toInt() in 48..57 && verificationCodeString.length < verifyCodeTextList.size) {
            // input ascii code 0..9
            verificationCodeString.append(event?.number)
            // TODO: 4/30/21 refresh view
        }

        if (verificationCodeString.length >= verifyCodeTextList.size || keyCode == KeyEvent.KEYCODE_ENTER) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(windowToken, 0)
        }
        return super.onKeyDown(keyCode, event)
    }
}