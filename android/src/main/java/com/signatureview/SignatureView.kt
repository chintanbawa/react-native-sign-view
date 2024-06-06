package com.signatureview

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.InputFilter
import android.text.InputType
import android.util.Base64
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.view.isInvisible
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import androidx.core.widget.addTextChangedListener
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.uimanager.events.RCTEventEmitter
import com.signatureview.interfaces.SeekBarListener
import com.signatureview.interfaces.SignatureTracker
import java.io.ByteArrayOutputStream

class SignatureView(context: Context) : LinearLayout(context), SeekBarListener, SignatureTracker {
  // Values
  private var minValueSeekBar = 0
  private val stepSeekBar = 1
  private var currentText = ""
  private var isDrawMode = true
  private var parentWidth: Float = 1f
  private val defaultBorderWidth = 1 * Resources.getSystem().displayMetrics.density.toInt()
  private val white = Color.parseColor("#ffffff")
  private val black = Color.parseColor("#000000")
  private val lightGray = Color.parseColor("#dddddd")
  private val textColor = Color.parseColor("#333333")
  private var tabBackgroundColor = lightGray
  private var selectedTabColor = textColor
  private var unselectedTabColor = Color.parseColor("#00000000")
  private var selectedTabTextColor = white
  private var unselectedTabTextColor = textColor

  // Views
  private val tabsContainer: LinearLayout
  private val drawButton: Button
  private val typeButton: Button
  private val signaturePadDraw: SignaturePadDraw
  private val signaturePadTypeContainer: FrameLayout
  private val signaturePadType: SignaturePadType
  private val seekBar: SeekBar
  private val editText: EditText
  private val clearButton: Button
  private val getSignButton: Button

  init {
    orientation = VERTICAL
    setBackgroundColor(white)

    // Tabs
    tabsContainer = LinearLayout(context)
    val tabsLP = LayoutParams(
      LayoutParams.MATCH_PARENT, 40 * Resources.getSystem().displayMetrics.density.toInt()
    )
    tabsLP.setMargins(0, 0, 0, (0.5 * Resources.getSystem().displayMetrics.density).toInt())
    tabsContainer.layoutParams = tabsLP
    tabsContainer.setBackgroundColor(tabBackgroundColor)
    // Draw Button
    drawButton = Button(context)
    drawButton.tag = "TAB_DRAW"
    drawButton.layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1f);
    drawButton.setPadding(0)
    drawButton.text = "Draw"
    drawButton.isAllCaps = false
    drawButton.setBackgroundColor(selectedTabColor)
    drawButton.setTextColor(selectedTabTextColor)
    // Type Button
    typeButton = Button(context)
    typeButton.tag = "TAB_TYPE"
    typeButton.layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1f);
    typeButton.setPadding(0)
    typeButton.text = "Type"
    typeButton.isAllCaps = false
    typeButton.setBackgroundColor(unselectedTabColor)
    typeButton.setTextColor(unselectedTabTextColor)
    tabsContainer.addView(drawButton)
    tabsContainer.addView(typeButton)
    addView(tabsContainer)
    // Divider
    addView(getDivider())
    //SignPads Container
    val signPadsContainerFL = FrameLayout(context)
    signPadsContainerFL.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f);
    // SignaturePadDraw
    val signaturePadDrawContainer = FrameLayout(context)
    signaturePadDrawContainer.layoutParams =
      LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    signaturePadDraw = SignaturePadDraw(this)
    signaturePadDraw.layoutParams =
      LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    signaturePadDrawContainer.addView(signaturePadDraw)
    signPadsContainerFL.addView(signaturePadDrawContainer)
    // SignaturePadType View Container
    val signPadTypeViewContainerLL = LinearLayout(context)
    signPadTypeViewContainerLL.layoutParams =
      LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    signPadTypeViewContainerLL.orientation = VERTICAL
    // SignaturePadType inside FrameLayout
    signaturePadTypeContainer = FrameLayout(context)
    signaturePadTypeContainer.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f);
    signaturePadTypeContainer.setPadding(10 * Resources.getSystem().displayMetrics.density.toInt())
    signaturePadType = SignaturePadType(this)
    signaturePadType.layoutParams =
      LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    signaturePadTypeContainer.addView(signaturePadType)
    signPadTypeViewContainerLL.addView(signaturePadTypeContainer)
    //Divider
    signPadTypeViewContainerLL.addView(getDivider())
    // SeekBar
    seekBar = SeekBar(context)
    seekBar.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    setSeekBarColor(textColor)
    signPadTypeViewContainerLL.addView(seekBar)
    //Divider
    signPadTypeViewContainerLL.addView(getDivider())
    // EditText
    editText = EditText(context)
    val editTextLP = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    editTextLP.setMargins(2 * Resources.getSystem().displayMetrics.density.toInt())
    editText.layoutParams = editTextLP
    editText.setPadding(2 * Resources.getSystem().displayMetrics.density.toInt())
    editText.setSingleLine()
    editText.textSize = 14f
    editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
    editText.setBackgroundColor(white)
    editText.hint = "Type your name here"
    signPadTypeViewContainerLL.addView(editText)
    signPadTypeViewContainerLL.isInvisible = true
    signPadsContainerFL.addView(signPadTypeViewContainerLL)
    addView(signPadsContainerFL)
    signPadsContainerFL.post(Runnable {
      parentWidth = signPadsContainerFL.width.toFloat()
      signPadTypeViewContainerLL.animate().translationX(parentWidth).withEndAction(Runnable {
        signPadTypeViewContainerLL.isInvisible = false
      })
    })
    // Divider
    addView(getDivider())
    // Action Buttons inside LinearLayout
    val actionButtonContainer = LinearLayout(context)
    actionButtonContainer.layoutParams = LayoutParams(
      LayoutParams.MATCH_PARENT, 40 * Resources.getSystem().displayMetrics.density.toInt()
    )
    // Clear Button
    clearButton = Button(context)
    clearButton.layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1f)
    clearButton.setPadding(0)
    clearButton.text = "Clear"
    clearButton.isAllCaps = false
    clearButton.isEnabled = false
    clearButton.setTextColor(textColor)
    clearButton.setBackgroundColor(lightGray)
    // Get Signature Button
    getSignButton = Button(context)
    getSignButton.layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1f);
    getSignButton.setPadding(0)
    getSignButton.text = "Get Sign"
    getSignButton.isAllCaps = false
    getSignButton.isEnabled = false
    getSignButton.setTextColor(white)
    getSignButton.setBackgroundColor(textColor)
    actionButtonContainer.addView(clearButton)
    actionButtonContainer.addView(getSignButton)
    addView(actionButtonContainer)

    // Listeners
    val tabListener = OnClickListener {
      resetState()
      getSignButton.isEnabled = true
      isDrawMode = it.tag == "TAB_DRAW"
      drawButton.setBackgroundColor(if (isDrawMode) selectedTabColor else unselectedTabColor)
      typeButton.setBackgroundColor(if (!isDrawMode) selectedTabColor else unselectedTabColor)
      drawButton.setTextColor(if (isDrawMode) selectedTabTextColor else unselectedTabTextColor)
      typeButton.setTextColor(if (!isDrawMode) selectedTabTextColor else unselectedTabTextColor)
      signaturePadDrawContainer.animate().translationX(if (isDrawMode) 0f else -parentWidth)
      signPadTypeViewContainerLL.animate().translationX(if (isDrawMode) parentWidth else 0f)
    }
    drawButton.setOnClickListener(tabListener)
    typeButton.setOnClickListener(tabListener)

    seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
      override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        signaturePadType.setSignatureFontSize(minValueSeekBar + (progress * stepSeekBar))
      }

      override fun onStartTrackingTouch(seekBar: SeekBar) {
        resetState()
      }

      override fun onStopTrackingTouch(seekBar: SeekBar) {
      }
    })

    editText.addTextChangedListener { mText ->
      val text = mText.toString()

      if (text.isNotEmpty()) {
        if (!clearButton.isEnabled) clearButton.isEnabled = true
        if (!getSignButton.isEnabled) getSignButton.isEnabled = true
      } else {
        if (clearButton.isEnabled) clearButton.isEnabled = false
        if (getSignButton.isEnabled) getSignButton.isEnabled = false
      }
      signaturePadType.setText(text)
    }

    clearButton.setOnClickListener {
      if (isDrawMode) {
        signaturePadDraw.clearSignature()
      } else {
        editText.setText("")
      }

      val context = context as ReactContext
      context.getJSModule(RCTEventEmitter::class.java).receiveEvent(
        id, RNSignatureViewManager.ON_CLEAR_EVENT, null
      )
    }

    getSignButton.setOnClickListener {
      if (isDrawMode) {
        onGetSignClick()
        getSignButton.isEnabled = false
        return@setOnClickListener
      }

      val newText = editText.text.toString()
      if (currentText != newText) {
        currentText = newText
        onGetSignClick()
      }
    }
  }

  // Functions
  private fun getDivider(): View {
    val divider = View(context)
    divider.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, defaultBorderWidth)
    divider.setBackgroundColor(black)
    return divider
  }

  private fun getTypeSignatureBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(
      signaturePadType.width, signaturePadType.height, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    canvas.save()
    signaturePadType.draw(canvas)
    canvas.restore()
    return bitmap
  }

  private fun getSignBase64(): String {
    val bitmap = if (isDrawMode) signaturePadDraw.getSignatureBitmap() else getTypeSignatureBitmap()
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
  }

  private fun onGetSignClick() {
    val map = Arguments.createMap()
    map.putString("sign", getSignBase64())
    val context = context as ReactContext
    context.getJSModule(RCTEventEmitter::class.java).receiveEvent(
      id, RNSignatureViewManager.ON_GET_SIGN_EVENT, map
    )
  }

  /**
   * Reset signature view on configurations change,
   * so that we can generate new sign with new configurations
   */
  fun resetState() {
    if (currentText != "") {
      currentText = ""
    }
    clearButton.isEnabled = true
    getSignButton.isEnabled = true
  }

  // Prop functions
  fun setSelectedTabColor(mSelectedTabColor: Int) {
    selectedTabColor = mSelectedTabColor
    if (isDrawMode) drawButton.setBackgroundColor(selectedTabColor)
    else typeButton.setBackgroundColor(selectedTabColor)
  }

  fun setSelectedTabTextColor(mSelectedTabTextColor: Int) {
    selectedTabTextColor = mSelectedTabTextColor
    if (isDrawMode) drawButton.setTextColor(selectedTabTextColor)
    else typeButton.setTextColor(selectedTabTextColor)
  }

  fun setUnselectedTabColor(mUnselectedTabColor: Int) {
    this.unselectedTabColor = mUnselectedTabColor
    tabsContainer.setBackgroundColor(unselectedTabColor)
  }

  fun setUnselectedTabTextColor(mUnselectedTabTextColor: Int) {
    unselectedTabTextColor = mUnselectedTabTextColor
    if (!isDrawMode) drawButton.setTextColor(unselectedTabTextColor)
    else typeButton.setTextColor(unselectedTabTextColor)
  }

  fun setSeekBarColor(mSeekBarColor: Int) {
    seekBar.progressDrawable.colorFilter =
      BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
        mSeekBarColor, BlendModeCompat.SRC_IN
      )
    seekBar.thumb.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
      mSeekBarColor, BlendModeCompat.SRC_IN
    )
  }

  fun setClearText(clearText: String) {
    clearButton.text = clearText
  }

  fun setGetSignText(getSignText: String) {
    getSignButton.text = getSignText
  }

  fun setTabTextCapital(isCaps: Boolean) {
    drawButton.isAllCaps = isCaps
    typeButton.isAllCaps = isCaps
  }

  fun setButtonTextCapital(isCaps: Boolean) {
    clearButton.isAllCaps = isCaps
    getSignButton.isAllCaps = isCaps
  }

  fun setClearTextColor(clearTextColor: Int) {
    clearButton.setTextColor(clearTextColor)
  }

  fun setGetSignTextColor(getSignTextColor: Int) {
    getSignButton.setTextColor(getSignTextColor)
  }

  fun setClearButtonBgColor(clearButtonBgColor: Int) {
    clearButton.setBackgroundColor(clearButtonBgColor)
  }

  fun setGetSignButtonBgColor(getSignButtonBgColor: Int) {
    getSignButton.setBackgroundColor(getSignButtonBgColor)
  }

  fun setStrokeColor(strokeColor: Int) {
    resetState()
    signaturePadType.setStrokeColor(strokeColor)
    signaturePadDraw.setStrokeColor(strokeColor)
  }

  fun setDrawStrokeWidth(drawStrokeWidth: Float) {
    resetState()
    signaturePadDraw.setDrawStrokeWidth(drawStrokeWidth)
  }

  // Listeners
  override fun setSeekBarRange(min: Int, max: Int) {
    editText.filters = arrayOf()
    this.minValueSeekBar = min
    val maxValueSeekBar = (max - min) / stepSeekBar
    seekBar.max = maxValueSeekBar
    seekBar.progress = maxValueSeekBar
  }

  override fun onSeekBarProgressChange(progress: Int) {
    seekBar.progress -= progress
    if (seekBar.progress == 0) {
      editText.filters = arrayOf(InputFilter.LengthFilter(editText.length()))
    }
  }

  override fun onSignature() {
    clearButton.isEnabled = true
    getSignButton.isEnabled = true

    val context = context as ReactContext
    context.getJSModule(RCTEventEmitter::class.java).receiveEvent(
      id, RNSignatureViewManager.ON_DRAW_BEGIN_EVENT, null
    )
  }

  override fun onSignatureEnd() {
    super.onSignatureEnd()
    val context = context as ReactContext
    context.getJSModule(RCTEventEmitter::class.java).receiveEvent(
      id, RNSignatureViewManager.ON_DRAW_END_EVENT, null
    )
  }

  override fun onClear() {
    clearButton.isEnabled = false
    getSignButton.isEnabled = false
  }
}
