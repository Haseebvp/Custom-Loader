package com.haseeb.unacademyassignment

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import kotlin.math.cos
import kotlin.math.sin


class CustomView constructor(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var strokeWidth = 4f
    private var progress = 0
    private var min = 0
    private var max = 100
    private val startAngle = -90
    private var currentAngle = 0
    private var color = Color.DKGRAY
    private var rectF: RectF? = null
    private var backgroundPaint: Paint
    private var backgroundTransparentPaint: Paint
    private var foregroundPaint: Paint
    private var dotPaint: Paint
    private var center_x = 0f
    private var center_y = 0f
    private var radius = 0f
    private var dotradius = 15
    private var animator: ValueAnimator? = null


    init {
        rectF = RectF()
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CircleProgressBar,
            0, 0
        )
        try {
            strokeWidth = typedArray.getDimension(
                R.styleable.CircleProgressBar_progressBarThickness,
                strokeWidth
            )
            progress = typedArray.getInt(R.styleable.CircleProgressBar_progress, progress)
            color = typedArray.getInt(R.styleable.CircleProgressBar_progressbarColor, color)
            min = typedArray.getInt(R.styleable.CircleProgressBar_min, min)
            max = typedArray.getInt(R.styleable.CircleProgressBar_max, max)
        } finally {
            typedArray.recycle()
        }


        backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        backgroundPaint.color = Color.parseColor("#e0e0e0")
        backgroundPaint.style = Paint.Style.STROKE
        backgroundPaint.strokeWidth = strokeWidth

        backgroundTransparentPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        backgroundTransparentPaint.color = Color.TRANSPARENT
        backgroundTransparentPaint.style = Paint.Style.STROKE
        backgroundTransparentPaint.strokeWidth = dotradius.toFloat()

        dotPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        dotPaint.color = Color.parseColor("#f9a825")
        dotPaint.style = Paint.Style.FILL

        foregroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        foregroundPaint.color = color
        foregroundPaint.style = Paint.Style.STROKE
        foregroundPaint.strokeWidth = strokeWidth

        startAnimation()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        val width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        radius = if (width > height) {
            height / 2.toFloat()
        } else {
            width / 2.toFloat()
        }
        min = width.coerceAtMost(height)
        setMeasuredDimension(min, min)
        rectF?.set(
            0f + dotradius,
            0f + dotradius,
            min.toFloat() - dotradius,
            min.toFloat() - dotradius
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val angle = (360 * currentAngle / max).toFloat()
        center_x = (width.toFloat()) / 2
        center_y = (height.toFloat()) / 2
        canvas?.drawCircle(center_x, center_y, radius, backgroundTransparentPaint)
        canvas?.drawCircle(center_x, center_y, radius - dotradius, backgroundPaint)
        val endX: Double =
            cos(Math.toRadians(270 + angle.toDouble())) * (radius - dotradius)  + center_x

        val endY: Double =
            sin(Math.toRadians(270 + angle.toDouble())) * (radius - dotradius)  + center_y
        canvas?.drawArc(rectF!!, startAngle.toFloat(), angle, false, foregroundPaint!!)
        canvas?.drawCircle(endX.toFloat(), endY.toFloat(), dotradius.toFloat(), dotPaint)

    }

    private fun startAnimation() {
        animator?.cancel()
        val startAngle = if (progress > currentAngle) {
            currentAngle
        } else {
            0
        }
        animator = ValueAnimator.ofInt(startAngle, progress).apply {
            duration = 500
            interpolator = AccelerateInterpolator()
            addUpdateListener { valueAnimator ->
                currentAngle = valueAnimator.animatedValue as Int
                invalidate()
            }
        }
        animator?.start()
    }

    fun setProgress(progress: Int) {
        if (progress <= 100) {
            this.progress = progress
            invalidate()
            startAnimation()
        }
    }


}