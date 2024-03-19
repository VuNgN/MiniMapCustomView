package com.vungn.customview.topappbar

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.vungn.customview.util.RandomColors
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class TopAppBar : View {
    private var _imageBitmap: Bitmap? = null
    private lateinit var miniMatrix: Matrix
    private lateinit var bigMatrix: Matrix
    private lateinit var miniMapPaint: Paint
    private lateinit var randomColorsPaint: Paint
    private lateinit var textPaint: Paint
    private lateinit var randomColors: RandomColors
    private var isShow = false
    private var isRandom = false
    private val scaleX = 0.1f
    private val scaleY = 0.1f
    private var miniMapWidth = 0f
    private var miniMapHeight = 0f
    private val miniPadding = 50f
    private var color1 = 0xFF
    private var color2 = 0x00
    private var color3 = 0x00
    private var color4 = Color.BLACK
    private var randomColorX = 0f
    private var randomColorY = 0f
    private var randomColorRadius = 200f

    var imageBitmap: Bitmap?
        get() = _imageBitmap
        set(value) {
            _imageBitmap = value
            postInvalidate()
        }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context, attrs, defStyleAttr, defStyleRes
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet) {
        randomColors = RandomColors()
        miniMatrix = Matrix()
        bigMatrix = Matrix()
        miniMapPaint = Paint()
        randomColorsPaint = Paint()
        textPaint = Paint()
        miniMapPaint.style = Paint.Style.STROKE
        miniMapPaint.strokeJoin = Paint.Join.ROUND
        miniMapPaint.strokeCap = Paint.Cap.ROUND
        miniMapPaint.strokeWidth = scaleX * 100
        randomColorsPaint.color = randomColors.getColor()
        textPaint.color = randomColors.getColor()
        textPaint.textSize = 80f
        miniMatrix.setTranslate(0f, 0f)
        color2 = randomColors.getColor()
    }

    private fun move(x: Float, y: Float) {
        bigMatrix.setTranslate(x, y)
        postInvalidate()
    }

    private fun scale(x1: Float, y1: Float, x2: Float, y2: Float) {
        miniMatrix.setScale(x1, y1, x2, y2)
        postInvalidate()
    }

    private fun makeVisible(visible: Boolean) {
        isShow = visible
        postInvalidate()
    }

    private fun isOnCircleRange(x: Float, y: Float): Boolean {
        val rangeFromEnemyToCircle = sqrt(
            (abs(x - randomColorX).pow(2) + abs(
                y - randomColorY
            ).pow(2)).toDouble()
        )
        Log.d(TAG, "isOnCircleRange: range:$rangeFromEnemyToCircle, radius:$randomColorRadius")
        return rangeFromEnemyToCircle <= randomColorRadius
    }

    private fun randomColor() {
        if (isRandom) {
            color1 = randomColors.getColor()
            color2 = randomColors.getColor()
            color3 = randomColors.getColor()
            color4 = randomColors.getColor()
            postInvalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        setBackgroundColor(color1)
        miniMapPaint.color = color2
        randomColorsPaint.color = color3
        textPaint.color = color4
        randomColorX = width * 0.8f
        randomColorY = height * 0.8f
        miniMapWidth = scaleX * width + miniPadding
        miniMapHeight = scaleY * height + miniPadding
        Log.d(TAG, "init: scaleX: $scaleX, width: $width, minimap: ${scaleX * width}")
        canvas.drawLine(miniMapWidth, miniPadding, miniMapWidth, miniMapHeight, miniMapPaint)
        canvas.drawLine(miniPadding, miniMapHeight, miniMapWidth, miniMapHeight, miniMapPaint)
        canvas.drawLine(miniPadding, miniPadding, miniPadding, miniMapHeight, miniMapPaint)
        canvas.drawLine(miniPadding, miniPadding, miniMapWidth, miniPadding, miniMapPaint)
        canvas.drawCircle(randomColorX, randomColorY, randomColorRadius, randomColorsPaint)
        canvas.drawText("RANDOM", randomColorX - randomColorRadius * 0.8f, randomColorY, textPaint)
        _imageBitmap?.let { bitmap ->
            canvas.drawBitmap(bitmap, bigMatrix, null)
            canvas.drawBitmap(bitmap, miniMatrix, null)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                Log.d(TAG, "onTouchEvent: Move")
                _imageBitmap?.let {
                    val centerX = event.x - it.width / 2
                    val centerY = event.y - it.height / 2
                    scale(
                        scaleX,
                        scaleY,
                        centerX * scaleX + miniPadding,
                        centerY * scaleY + miniPadding
                    )
                    move(centerX, centerY)
                    isRandom = if (isOnCircleRange(event.x, event.y)) {
                        randomColor()
                        false
                    } else {
                        true
                    }
                }
                performClick()
                true
            }

            MotionEvent.ACTION_UP -> {
                Log.d(TAG, "onTouchEvent: Up")
                true
            }

            else -> {
                false
            }
        }
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    companion object {
        const val MIN_HEIGHT = 60
        private const val TAG = "TopAppBar"
    }
}