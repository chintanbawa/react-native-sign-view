//
//  SignaturePadType.swift
//  ReactNativeModuleDemo
//
//  Created by Chintan Bawa on 25/05/24.
//

import UIKit

class SignaturePadType: UIView {
    private var signatureText = ""
    private var currentFontSize: CGFloat = 40.0
    private var currentFontSizeHalf: CGFloat {
        return currentFontSize / 2
    }
    private var canvasTopMargin: CGFloat = 0.0
    private var canvasMid: Int = 0
    private var attributes : [NSAttributedString.Key: Any]?
    
    // Views
    private var textPaint: NSMutableParagraphStyle!
    private var staticLayout: NSAttributedString?
    
    
    // Listeners
    private var sliderListener: SliderListener?
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        self.backgroundColor = UIColor.white
        initTextPaint()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    func setSliderListener(listener: SignatureView) {
        sliderListener = listener
    }
    
    // Functions
    private func initTextPaint() {
        textPaint = NSMutableParagraphStyle()
        textPaint.alignment = .center
    }
    
    func setText(_ text: String) {
        signatureText = text
        
        if frame.width <= text.size(withAttributes: attributes).width {
            sliderListener?.onSliderProgressChange(value: Float(5))
            return
        }
        
        staticLayout = NSAttributedString(string: signatureText, attributes: attributes)
        
        let textHeight = staticLayout!.size().height
        canvasTopMargin = CGFloat(canvasMid) - (textHeight / 2)
        setNeedsDisplay()
    }
    
    func setSignatureFontSize(_ signatureFontSize: Float) {
        DispatchQueue.main.async {
            self.attributes?[.font] = UIFont(name: "Wood Dragon", size: CGFloat(signatureFontSize)) ?? UIFont(name: "Helvetica", size: CGFloat(signatureFontSize))
            self.setText(self.signatureText)
        }
    }
    
    // Prop functions
    func setStrokeColor(_ strokeColor: UIColor) {
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5)  {
            self.attributes?[.foregroundColor] = strokeColor
            self.setText(self.signatureText)
        }
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        
        // determine the width
        let width: CGFloat =  frame.width
        // determine the height
        let height: CGFloat = frame.height
        
        self.canvasMid = Int(height / 2)
        let maxFontSize = height
        let minFontSize = round(maxFontSize / 2)
        self.currentFontSize = maxFontSize
        attributes = [
            .font: UIFont(name: "Wood Dragon", size: currentFontSize) ?? UIFont(name: "Helvetica", size: CGFloat(currentFontSize))!,
            .paragraphStyle: textPaint!,
            .foregroundColor: UIColor.black
        ]
        
        sliderListener?.setSliderRange(min: Float(minFontSize), max: Float(maxFontSize))
        
        // Required call: set width and height
        frame.size = CGSize(width: width, height: height)
    }
    
    override func draw(_ rect: CGRect) {
        super.draw(rect)
        guard let staticLayout = staticLayout else { return }
        
        let context = UIGraphicsGetCurrentContext()
        context?.saveGState()
        context?.translateBy(x: 0, y: canvasTopMargin)
        staticLayout.draw(in: rect)
        context?.restoreGState()
    }
}