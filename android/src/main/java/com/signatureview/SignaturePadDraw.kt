package com.signatureview

import android.app.ActionBar.LayoutParams
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import com.signatureview.interfaces.SignatureTracker

class SignaturePadDraw(signatureView: SignatureView) : View(signatureView.context) {
  private val paint = Paint().apply {
    isAntiAlias = true
    color = Color.BLACK
    style = Paint.Style.STROKE
    strokeJoin = Paint.Join.ROUND
    strokeWidth = 10 * Resources.getSystem().displayMetrics.density
  }
  private val path = Path()
  private var lastTouchX = 0f
  private var lastTouchY = 0f
  private val dirtyRect = RectF()

  //Listeners
  private var signatureTracker: SignatureTracker

  init {
    signatureTracker = signatureView
    // Width and height should cover the screen
    layoutParams =
      LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
  }

  // Functions
  fun getSignatureBitmap(): Bitmap {
    val signatureBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(signatureBitmap)
    canvas.save()
    draw(canvas)
    canvas.restore()
    return signatureBitmap
  }

  fun clearSignature() {
    if (!path.isEmpty) {
      path.reset()
      invalidate()
      signatureTracker.onClear()
    }
  }

  // Prop functions
  fun setStrokeColor(strokeColor: Int) {
    post(Runnable {
      paint.color = strokeColor
      invalidate()
    })
  }

  fun setDrawStrokeWidth(drawStrokeWidth: Float) {
    post(Runnable {
      paint.strokeWidth = drawStrokeWidth * Resources.getSystem().displayMetrics.density.toInt()
      invalidate()
    })
  }

  // All touch events during the drawing
  override fun onDraw(canvas: Canvas) {
    canvas.drawPath(path, paint)
  }

  override fun onTouchEvent(event: MotionEvent): Boolean {
    val eventX = event.x
    val eventY = event.y

    when (event.action) {
      MotionEvent.ACTION_DOWN -> {
        path.moveTo(eventX, eventY)
        lastTouchX = eventX
        lastTouchY = eventY
        signatureTracker.onSignature()
        return true
      }

      MotionEvent.ACTION_MOVE -> {
        resetDirtyRect(eventX, eventY)
        val historySize = event.historySize
        for (i in 0 until historySize) {
          val historicalX = event.getHistoricalX(i)
          val historicalY = event.getHistoricalY(i)
          expandDirtyRect(historicalX, historicalY)
          path.lineTo(historicalX, historicalY)
        }
        path.lineTo(eventX, eventY)
      }

      MotionEvent.ACTION_UP -> {
        resetDirtyRect(eventX, eventY)
        val historySize = event.historySize
        for (i in 0 until historySize) {
          val historicalX = event.getHistoricalX(i)
          val historicalY = event.getHistoricalY(i)
          expandDirtyRect(historicalX, historicalY)
          path.lineTo(historicalX, historicalY)
        }
        path.lineTo(eventX, eventY)
        signatureTracker.onSignatureEnd()
      }

      else -> return false
    }

    invalidate()

    lastTouchX = eventX
    lastTouchY = eventY

    return true
  }

  private fun expandDirtyRect(historicalX: Float, historicalY: Float) {
    if (historicalX < dirtyRect.left) {
      dirtyRect.left = historicalX
    } else if (historicalX > dirtyRect.right) {
      dirtyRect.right = historicalX
    }

    if (historicalY < dirtyRect.top) {
      dirtyRect.top = historicalY
    } else if (historicalY > dirtyRect.bottom) {
      dirtyRect.bottom = historicalY
    }
  }

  private fun resetDirtyRect(eventX: Float, eventY: Float) {
    dirtyRect.left = minOf(lastTouchX, eventX)
    dirtyRect.right = maxOf(lastTouchX, eventX)
    dirtyRect.top = minOf(lastTouchY, eventY)
    dirtyRect.bottom = maxOf(lastTouchY, eventY)
  }
}

