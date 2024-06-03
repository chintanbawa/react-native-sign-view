package com.signatureview

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext

class RNSignatureViewPackage : ReactPackage {

    override fun createViewManagers(
        reactContext: ReactApplicationContext
    ) = listOf(RNSignatureViewManager(reactContext))

    override fun createNativeModules(
        reactContext: ReactApplicationContext
    ): MutableList<NativeModule> = mutableListOf()
}
