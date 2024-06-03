//
//  SeekBarListener.swift
//  ReactNativeModuleDemo
//
//  Created by Chintan Bawa on 25/05/24.
//

import Foundation

protocol SliderListener {
  func setSliderRange(min: Float, max: Float)
  func onSliderProgressChange(value: Float)
}

extension SliderListener{
  func setSliderRange(min: Float, max: Float) {
    // Default implementation
  }
  
  func onSliderProgressChange(value: Float) {
    // Default implementation
  }
}