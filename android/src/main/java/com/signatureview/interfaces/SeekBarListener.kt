package com.signatureview.interfaces

interface SeekBarListener {
    fun setSeekBarRange(min: Int, max: Int) {
    }

    fun onSeekBarProgressChange(progress: Int) {

    }
}