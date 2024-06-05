//
//  SignatureTracker.swift
//  ReactNativeModuleDemo
//
//  Created by Chintan Bawa on 25/05/24.
//

import Foundation

protocol SignatureTracker {
    func onSignature()
    func onSignatureEnd()
    func onSignatureClear()
}

extension SignatureTracker {
    func onSignature() {
        // Default implementation
    }
    
    func onSignatureEnd() {
        // Default implementation
    }
    
    func onSignatureClear() {
        // Default implementation
    }
}
