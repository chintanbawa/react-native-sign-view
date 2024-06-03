//
//  SignatureViewManager.swift
//  ReactNativeModuleDemo
//
//  Created by Chintan Bawa on 25/05/24.
//

import Foundation

@objc(RNSignatureViewManager)
class RNSignatureViewManager: RCTViewManager{
  
  override func view() -> UIView! {
    return SignatureView()
  }
  
  override class func requiresMainQueueSetup() -> Bool {
    return true
  }
}
