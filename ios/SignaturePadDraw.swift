//
//  SignaturePadDraw.swift
//  ReactNativeModuleDemo
//
//  Created by Chintan Bawa on 25/05/24.
//

import UIKit

class SignaturePadDraw: UIView {
  private let paint: UIBezierPath = {
    let path = UIBezierPath()
    path.lineWidth = 5.0
    path.lineJoinStyle = .round
    return path
  }()
  private var strokeColor = UIColor.black
  private var lastTouchPoint: CGPoint = .zero
  private var dirtyRect: CGRect = .zero
  
  // Protocols
  private var signatureTracker: SignatureTracker?
  
  override init(frame: CGRect) {
    super.init(frame: frame)
    self.backgroundColor = .white
  }
  
  required init?(coder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  func setSignatureTrackerListener(listener: SignatureView) {
    self.signatureTracker = listener
  }
  
  // Functions
  func getSignatureImage() -> UIImage? {
    UIGraphicsBeginImageContext(self.bounds.size)
    guard let context = UIGraphicsGetCurrentContext() else { return nil }
    self.layer.render(in: context)
    let signatureImage = UIGraphicsGetImageFromCurrentImageContext()
    UIGraphicsEndImageContext()
    return signatureImage
  }
  
  func clearSignature() {
    if !paint.isEmpty {
      paint.removeAllPoints()
      setNeedsDisplay()
      signatureTracker?.onSignatureClear()
    }
  }
  
  // Prop functions
  func setStrokeColor(_ strokeColor: UIColor) {
    DispatchQueue.main.async {
      self.strokeColor = strokeColor
      self.setNeedsDisplay()
    }
  }
  
  func setDrawStrokeWidth(_ drawStrokeWidth: CGFloat) {
    DispatchQueue.main.async {
      self.paint.lineWidth = drawStrokeWidth
      self.setNeedsDisplay()
    }
  }
  
  // All touch events during the drawing
  override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
    guard let touch = touches.first else { return }
    let touchPoint = touch.location(in: self)
    paint.move(to: touchPoint)
    lastTouchPoint = touchPoint
    signatureTracker?.onSignature()
  }
  
  override func touchesMoved(_ touches: Set<UITouch>, with event: UIEvent?) {
    guard let touch = touches.first else { return }
    let touchPoint = touch.location(in: self)
    resetDirtyRect(touchPoint)
    paint.addLine(to: touchPoint)
    lastTouchPoint = touchPoint
    setNeedsDisplay()
  }
  
  override func touchesEnded(_ touches: Set<UITouch>, with event: UIEvent?) {
    guard let touch = touches.first else { return }
    let touchPoint = touch.location(in: self)
    resetDirtyRect(touchPoint)
    paint.addLine(to: touchPoint)
    lastTouchPoint = touchPoint
    setNeedsDisplay()
  }
  
  private func resetDirtyRect(_ touchPoint: CGPoint) {
    dirtyRect = CGRect(x: min(lastTouchPoint.x, touchPoint.x),
                       y: min(lastTouchPoint.y, touchPoint.y),
                       width: abs(lastTouchPoint.x - touchPoint.x),
                       height: abs(lastTouchPoint.y - touchPoint.y))
  }

   override func draw(_ rect: CGRect) {
    strokeColor.setStroke()
    paint.stroke()
  }
}
