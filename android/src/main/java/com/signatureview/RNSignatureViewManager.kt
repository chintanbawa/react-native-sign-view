package com.signatureview

import android.graphics.Color
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp


class RNSignatureViewManager(
  private val callerContext: ReactApplicationContext
) : SimpleViewManager<SignatureView>() {

  override fun getName() = REACT_CLASS
  override fun createViewInstance(context: ThemedReactContext): SignatureView {
    return SignatureView(context)
  }

  @ReactProp(name = "selectedTabColor", customType = "Color")
  fun setSelectedTabColor(view: SignatureView, selectedTabColor: Int?) {
    view.setSelectedTabColor(selectedTabColor ?: Color.parseColor("#333333"))
  }

  @ReactProp(name = "selectedTabTextColor", customType = "Color")
  fun setSelectedTabTextColor(view: SignatureView, selectedTabTextColor: Int?) {
    view.setSelectedTabTextColor(selectedTabTextColor ?: Color.parseColor("#ffffff"))
  }

  @ReactProp(name = "unselectedTabColor", customType = "Color")
  fun setUnselectedTabColor(view: SignatureView, unselectedTabColor: Int?) {
    view.setUnselectedTabColor(unselectedTabColor ?: Color.parseColor("#dddddd"))
  }

  @ReactProp(name = "unselectedTabTextColor", customType = "Color")
  fun setUnselectedTabTextColor(view: SignatureView, unselectedTabTextColor: Int?) {
    view.setUnselectedTabTextColor(unselectedTabTextColor ?: Color.parseColor("#333333"))
  }

  @ReactProp(name = "seekBarColor", customType = "Color")
  fun setSeekBarColor(view: SignatureView, seekBarColor: Int?) {
    view.setSeekBarColor(seekBarColor ?: Color.parseColor("#333333"))
  }

  @ReactProp(name = "clearText")
  fun setClearText(view: SignatureView, clearText: String?) {
    view.setClearText(clearText ?: "Clear")
  }

  @ReactProp(name = "getSignText")
  fun setGetSignText(view: SignatureView, getSignText: String?) {
    view.setGetSignText(getSignText ?: "Get Sign")
  }

  @ReactProp(name = "capsButtonText")
  fun setButtonTextCapital(view: SignatureView, capsButtonText: Boolean?) {
    view.setButtonTextCapital(capsButtonText ?: false)
  }

  @ReactProp(name = "capsTabText")
  fun setTabTextCapital(view: SignatureView, capsTabText: Boolean?) {
    view.setTabTextCapital(capsTabText ?: false)
  }

  @ReactProp(name = "clearButtonBgColor", customType = "Color")
  fun setClearButtonBgColor(view: SignatureView, clearButtonBgColor: Int?) {
    view.setClearButtonBgColor(clearButtonBgColor ?: Color.parseColor("#dddddd"))
  }

  @ReactProp(name = "clearTextColor", customType = "Color")
  fun setClearTextColor(view: SignatureView, clearTextColor: Int?) {
    view.setClearTextColor(clearTextColor ?: Color.parseColor("#333333"))
  }

  @ReactProp(name = "getSignButtonBgColor", customType = "Color")
  fun setGetSignButtonBgColor(view: SignatureView, getSignButtonBgColor: Int?) {
    view.setGetSignButtonBgColor(getSignButtonBgColor ?: Color.parseColor("#333333"))
  }

  @ReactProp(name = "getSignTextColor", customType = "Color")
  fun setGetSignTextColor(view: SignatureView, getSignTextColor: Int?) {
    view.setGetSignTextColor(getSignTextColor ?: Color.parseColor("#ffffff"))
  }

  @ReactProp(name = "strokeColor", customType = "Color")
  fun setStrokeColor(view: SignatureView, strokeColor: Int?) {
    view.setStrokeColor(strokeColor ?: Color.parseColor("#000000"))
  }

  @ReactProp(name = "drawStrokeWidth")
  fun setDrawStrokeWidth(view: SignatureView, drawStrokeWidth: Int?) {
    view.setDrawStrokeWidth(drawStrokeWidth?.toFloat() ?: 10f)
  }

  override fun getExportedCustomDirectEventTypeConstants(): MutableMap<String, Any>? {
    return MapBuilder.of(
      ON_GET_SIGN_EVENT,
      MapBuilder.of("registrationName", ON_GET_SIGN_EVENT),
      ON_CLEAR_EVENT,
      MapBuilder.of("registrationName", ON_CLEAR_EVENT),
      ON_DRAW_BEGIN_EVENT,
      MapBuilder.of("registrationName", ON_DRAW_BEGIN_EVENT),
      ON_DRAW_END_EVENT,
      MapBuilder.of("registrationName", ON_DRAW_END_EVENT),
    );
  }

  companion object {
    const val REACT_CLASS = "RNSignatureView"
    const val ON_GET_SIGN_EVENT = "onGetSign"
    const val ON_CLEAR_EVENT = "onClear"
    const val ON_DRAW_BEGIN_EVENT = "onDrawBegin"
    const val ON_DRAW_END_EVENT = "onDrawEnd"
  }
}
