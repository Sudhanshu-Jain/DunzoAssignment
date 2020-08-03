package com.example.dunzotest

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.text.Spanned
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import com.google.android.material.textfield.TextInputEditText
class CustomEditInputText : TextInputEditText {

    private var mDrawableRight: Drawable? = null
    private var mDrawableLeft: Drawable? = null
    private var mDrawableTop: Drawable? = null
    private var mDrawableBottom: Drawable? = null
    private var mOnImeBack: BackPressedListener? = null

    internal var actionX: Int = 0
    internal var actionY: Int = 0

    private var clickListener: DrawableClickListener? = null

    interface DrawableClickListener {

        enum class DrawablePosition {
            TOP, BOTTOM, LEFT, RIGHT
        }

        fun onClick(target: DrawablePosition)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        filters = arrayOf<InputFilter>(EmojiExcludeFilter())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun setCompoundDrawables(
        left: Drawable?, top: Drawable?,
        right: Drawable?, bottom: Drawable?
    ) {
        if (left != null) {
            mDrawableLeft = left
        }
        if (right != null) {
            mDrawableRight = right
        }
        if (top != null) {
            mDrawableTop = top
        }
        if (bottom != null) {
            mDrawableBottom = bottom
        }
        super.setCompoundDrawables(left, top, right, bottom)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        var bounds: Rect
        if (event.action == MotionEvent.ACTION_DOWN) {
            actionX = event.x.toInt()
            actionY = event.y.toInt()
            if (mDrawableBottom != null && mDrawableBottom!!.bounds.contains(actionX, actionY)) {
                clickListener!!.onClick(DrawableClickListener.DrawablePosition.BOTTOM)
                return super.onTouchEvent(event)
            }

            if (mDrawableTop != null && mDrawableTop!!.bounds.contains(actionX, actionY)) {
                clickListener!!.onClick(DrawableClickListener.DrawablePosition.TOP)
                return super.onTouchEvent(event)
            }

            // this works for left since container shares 0,0 origin with bounds
            if (mDrawableLeft != null) {
                bounds = mDrawableLeft!!.bounds

                var x: Int
                var y: Int
                val extraTapArea = (13 * resources.displayMetrics.density + 0.5).toInt()

                x = actionX
                y = actionY

                if (!bounds.contains(actionX, actionY)) {
                    x = actionX - extraTapArea
                    y = actionY - extraTapArea

                    if (x <= 0)
                        x = actionX
                    if (y <= 0)
                        y = actionY

                    if (x < y) {
                        y = x
                    }
                }

                if (bounds.contains(x, y) && clickListener != null) {
                    clickListener!!
                        .onClick(DrawableClickListener.DrawablePosition.LEFT)
                    event.action = MotionEvent.ACTION_CANCEL
                    return false

                }
            }

            if (mDrawableRight != null) {

                bounds = mDrawableRight!!.bounds

                var x: Int
                var y: Int
                val extraTapArea = 13

                x = actionX + extraTapArea
                y = actionY - extraTapArea

                x = width - x

                /*x can be negative if user taps at x co-ordinate just near the width.
                 * e.g views width = 300 and user taps 290. Then as per previous calculation
                 * 290 + 13 = 303. So subtract X from getWidth() will result in negative value.
                 * So to avoid this add the value previous added when x goes negative.
                 */

                if (x <= 0) {
                    x += extraTapArea
                }

                /* If result after calculating for extra tappable area is negative.
                 * assign the original value so that after subtracting
                 * extratapping area value doesn't go into negative value.
                 */

                if (y <= 0)
                    y = actionY

                if (bounds.contains(x, y) && clickListener != null) {
                    clickListener!!
                        .onClick(DrawableClickListener.DrawablePosition.RIGHT)
                    event.action = MotionEvent.ACTION_CANCEL
                    return false
                }
                return super.onTouchEvent(event)
            }

        }
        return super.onTouchEvent(event)
    }

    @Throws(Throwable::class)
    protected  fun finalize() {
        mDrawableRight = null
        mDrawableBottom = null
        mDrawableLeft = null
        mDrawableTop = null
    }

    fun setDrawableClickListener(listener: DrawableClickListener) {
        this.clickListener = listener
    }

    private inner class EmojiExcludeFilter : InputFilter {

        override fun filter(
            source: CharSequence,
            start: Int,
            end: Int,
            dest: Spanned,
            dstart: Int,
            dend: Int
        ): CharSequence? {
            for (i in start until end) {
                val type = Character.getType(source[i])
                if (type == Character.SURROGATE.toInt() || type == Character.OTHER_SYMBOL.toInt()) {
                    return ""
                }
            }
            return null
        }
    }

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
            if (mOnImeBack != null) mOnImeBack!!.onImeBack(this)
        }
        return super.dispatchKeyEvent(event)
    }

    fun setBackPressedListener(listener: BackPressedListener) {
        mOnImeBack = listener
    }

    interface BackPressedListener {
        fun onImeBack(editText: CustomEditInputText)
    }
}
