package com.signatureview

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.view.View
import com.signatureview.interfaces.SeekBarListener


class SignaturePadType : View {
  private var signatureText = ""
  private var currentFontSize = 150f
  private var currentFontSizeHalf = currentFontSize / 2
  private var canvasTopMargin = 0f
  private var canvasMid = 0

  //views
  private lateinit var textPaint: TextPaint
  private var staticLayout: StaticLayout? = null

  //listeners
  private var seekBarListener: SeekBarListener


  constructor(signatureView: SignatureView) : super(signatureView.context) {
    seekBarListener = signatureView
    initTextPaint()
  }

  // Functions
  private fun initTextPaint() {
    textPaint = TextPaint()
    textPaint.isAntiAlias = true
    textPaint.textSize = currentFontSize
    textPaint.color = Color.parseColor("#000000")
    textPaint.typeface = Typeface.createFromAsset(context.assets, "fonts/signature.ttf");
  }

  fun setText(text: String) {
    signatureText = text

    if (signatureText.length > 1 && width < textPaint.measureText(
        signatureText
      )
    ) {
      seekBarListener.onSeekBarProgressChange(10)
      return
    }

    val builder = StaticLayout.Builder.obtain(
      signatureText, 0, signatureText.length, textPaint, width
    ).setAlignment(Layout.Alignment.ALIGN_CENTER).setLineSpacing(1f, 0f).setIncludePad(false)

    staticLayout = builder.build()

    canvasTopMargin = canvasMid - (staticLayout!!.lineCount * currentFontSizeHalf)
    invalidate()
  }

  fun setSignatureFontSize(signatureFontSize: Int) {
    if (staticLayout != null) {
      this.currentFontSize = signatureFontSize.toFloat()
      this.currentFontSizeHalf = currentFontSize / 2
      textPaint.textSize = currentFontSize
      setText(signatureText)
    }
  }

  // Prop functions
  fun setStrokeColor(strokeColor: Int) {
    post(Runnable {
      textPaint.color = strokeColor
      setText(signatureText)
    })
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val width = MeasureSpec.getSize(widthMeasureSpec)
    val height = MeasureSpec.getSize(heightMeasureSpec)
    this.canvasMid = height / 2
    val maxFontSize = height
    val minFontSize = Math.round((maxFontSize / 2).toDouble())
    this.currentFontSize = maxFontSize.toFloat()
    this.currentFontSizeHalf = currentFontSize / 2
    textPaint.textSize = currentFontSize
    seekBarListener.setSeekBarRange(minFontSize.toInt(), maxFontSize)

    // Required call: set width and height
    setMeasuredDimension(width, height)
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    if (staticLayout == null) return

    canvas.save()
    canvas.translate(0f, canvasTopMargin)
    staticLayout!!.draw(canvas)
    canvas.restore()
  }
}
