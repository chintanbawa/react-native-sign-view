//
//  SignatureTracker.swift
//  ReactNativeModuleDemo
//
//  Created by Chintan Bawa on 25/05/24.
//

import Foundation

protocol SignatureTracker {
  func onSignature()
  func onSignatureClear()
}

extension SignatureTracker {
  func onSignature() {
    // Default implementation
  }
  
  func onSignatureClear() {
    // Default implementation
  }
}