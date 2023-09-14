package com.udacity

import android.animation.AnimatorInflater
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private var btnColor = 0
    private var textColor = 0

    private var progress = 0.0


    //Create an downloading indicator
    private val rect: RectF = RectF(0f, 0f, 0f, 0f)
    private var arc: Arc
    private var currentSweepAngle = 0


    private var valueAnimator: ValueAnimator


    companion object {
        var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { p, old, new ->

        }
    }

    private val indicatorValueAnimator = ValueAnimator.ofInt(0, 360).apply {
        duration = 2000
        repeatCount = ValueAnimator.INFINITE
        interpolator = LinearInterpolator()
        addUpdateListener {
            currentSweepAngle = it.animatedValue as Int
            invalidate()
            requestLayout()
        }
    }
    private val updateAnimatorListener = ValueAnimator.AnimatorUpdateListener {
        progress = (it.animatedValue as Float).toDouble()

        invalidate()
        requestLayout()
    }


    init {
        isClickable = true

        arc = Arc(0f, 360f, context.getColor(R.color.colorAccent))
        

        valueAnimator = AnimatorInflater.loadAnimator(
            context,
            R.animator.loading_animation
        ) as ValueAnimator
        valueAnimator.addUpdateListener(updateAnimatorListener)

        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            btnColor = getColor(R.styleable.LoadingButton_backgroundColor, 0)
            textColor = getColor(R.styleable.LoadingButton_textColor, 0)
        }
    }


    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 40.0f
        typeface = Typeface.create("", Typeface.NORMAL)
    }


    override fun performClick(): Boolean {
        super.performClick()

        if (buttonState == ButtonState.Clicked) {
            buttonState = ButtonState.Loading
        }
        valueAnimator.start()
        indicatorValueAnimator.start()


        return true
    }

    fun hasDownloadingCompleted() {
        valueAnimator.cancel()
        indicatorValueAnimator.cancel()
        buttonState = ButtonState.Clicked
        invalidate()
        requestLayout()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.strokeWidth = 0f
        paint.color = btnColor
        canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)

        if (buttonState == ButtonState.Loading) {
            paint.color = context.getColor(R.color.backgroundColor)
            canvas.drawRect(
                0f, 0f,
                (width * (progress / 100)).toFloat(), height.toFloat(), paint
            )

            //Animate indicator
            if (currentSweepAngle > arc.start + arc.sweep) {
                paint.color = arc.color
                canvas.drawArc(
                    rect,
                    arc.start,
                    arc.sweep,
                    true,
                    paint
                )
            } else {
                if (currentSweepAngle > arc.start) {
                    paint.color = arc.color
                    canvas.drawArc(
                        rect,
                        arc.start,
                        currentSweepAngle - arc.start,
                        true,
                        paint
                    )
                }
            }


            // canvas.drawCircle((widthSize / 1.3).toFloat(), (heightSize / 2).toFloat(), radius, paint)
        } else {
            paint.color = btnColor
            canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)
        }

        val buttonText = when (buttonState) {
            ButtonState.Loading -> resources.getString(R.string.button_loading)
            else -> resources.getString(R.string.button_download)
        }

        paint.color = textColor
        canvas.drawText(buttonText, (widthSize / 2).toFloat(), ((heightSize + 20) / 2).toFloat(), paint)


    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val x = ((widthSize) / 1.4).toFloat()
        val y = ((heightSize) / 2.8).toFloat()
        rect.set(0f, 0f, (widthSize / 12).toFloat(), (heightSize / 2.5).toFloat())
        rect.offset(x, y)
    }

    private class Arc(val start: Float, val sweep: Float, val color: Int)
}

