//
//  SignatureView.swift
//  ReactNativeModuleDemo
//
//  Created by Chintan Bawa on 25/05/24.
//

import UIKit

class SignatureView: UIView, UITextFieldDelegate, SliderListener, SignatureTracker {
  
  // Props Callback
  @objc var onClear: RCTDirectEventBlock?
  @objc var onGetSign: RCTDirectEventBlock?
  @objc var onDrawBegin: RCTDirectEventBlock?
  @objc var onDrawEnd: RCTDirectEventBlock?
  
  // Values
  private var currentText = ""
  private var isDrawMode = true
  private let mineShaftColor = UIColor(red: 0.20, green: 0.20, blue: 0.20, alpha: 1.00)
  private let lightSilver = UIColor(red: 0.87, green: 0.87, blue: 0.87, alpha: 1.00)
  private let white = UIColor.white
  private let transparent = UIColor.clear
  private var tabBackgroundColor = UIColor.clear
  private var selectedTabColor = UIColor.clear
  private var unselectedTabColor = UIColor.clear
  private var selectedTabTextColor = UIColor.clear
  private var unselectedTabTextColor = UIColor.clear
  private var maxLength = Int.max
  
  // Views
  private let tabsContainer = UIStackView()
  private let drawButton = UIButton()
  private let typeButton = UIButton()
  private let signPadsContainer = UIView()
  private let signaturePadDrawContainer = UIView()
  private let signaturePadDraw = SignaturePadDraw()
  private let signPadTypeViewContainer = UIStackView()
  private let signaturePadTypeContainer = UIView()
  private let signaturePadType = SignaturePadType()
  private let slider = UISlider()
  private let textField = SignatureTextField()
  private let actionButtonContainer = UIStackView()
  private let clearButton = UIButton()
  private let getSignButton = UIButton()
  
  override func layoutSubviews() {
    let parentWidth = self.bounds.width
    let parentHeight = self.bounds.height
    tabsContainer.frame = CGRect(x: 0, y: 0, width: parentWidth, height: 30)
    signPadsContainer.frame = CGRect(x: 0, y: 35, width: parentWidth, height: parentHeight - 60)
    signaturePadDrawContainer.frame = signPadsContainer.bounds
    signaturePadDraw.frame = signPadsContainer.bounds
    signPadTypeViewContainer.frame = CGRect(x: 0, y: 0, width: parentWidth, height: parentHeight - 60)
    signaturePadTypeContainer.frame = CGRect(x: 0, y: 0, width: parentWidth, height: parentHeight - 130)
    signaturePadType.frame = signaturePadTypeContainer.bounds
    slider.frame = CGRect(x: 0, y:  parentHeight - 120, width: parentWidth, height: 20)
    textField.frame = CGRect(x: 0, y: parentHeight - 90, width: parentWidth, height: 30)
    actionButtonContainer.frame = CGRect(x: 0, y: signPadsContainer.bounds.height + 40, width: parentWidth, height: 30)
  }
  
  override init(frame: CGRect) {
    super.init(frame: frame)
    
    // Initialize colors
    tabBackgroundColor = lightSilver
    selectedTabColor = mineShaftColor
    unselectedTabColor = transparent
    selectedTabTextColor = white
    unselectedTabTextColor = mineShaftColor
    
    // Tabs
    tabsContainer.axis = .horizontal
    tabsContainer.distribution = .fillEqually
    tabsContainer.spacing = 4
    tabsContainer.backgroundColor = tabBackgroundColor
    tabsContainer.layer.borderWidth = 1
    tabsContainer.layer.borderColor = UIColor.black.cgColor
    // Draw Button
    drawButton.tag = 0
    drawButton.setTitle("Draw", for: .normal)
    drawButton.backgroundColor = selectedTabColor
    drawButton.setTitleColor(selectedTabTextColor, for: .normal)
    // Type Button
    typeButton.tag = 1
    typeButton.setTitle("Type", for: .normal)
    typeButton.backgroundColor = unselectedTabColor
    typeButton.setTitleColor(unselectedTabTextColor, for: .normal)
    tabsContainer.addArrangedSubview(drawButton)
    tabsContainer.addArrangedSubview(typeButton)
    addSubview(tabsContainer)
    
    // SignPads Container
    // SignPadDrawer Container
    signaturePadDrawContainer.layer.borderWidth = 1
    signaturePadDrawContainer.layer.borderColor = UIColor.black.cgColor
    // SignaturePadDraw
    signaturePadDraw.setSignatureTrackerListener(listener: self)
    signaturePadDrawContainer.addSubview(signaturePadDraw)
    signPadsContainer.addSubview(signaturePadDrawContainer)
    
    // SignPadTypeView
    signPadTypeViewContainer.axis = .vertical
    // SignaturePadType Container
    signaturePadTypeContainer.layer.borderWidth = 1
    signaturePadTypeContainer.layer.borderColor = UIColor.black.cgColor
    // SignaturePadType
    signaturePadType.setSliderListener(listener: self)
    signaturePadTypeContainer.addSubview(signaturePadType)
    signPadTypeViewContainer.addSubview(signaturePadTypeContainer)
    // Slider
    slider.thumbTintColor = mineShaftColor
    slider.tintColor = mineShaftColor
    signPadTypeViewContainer.addSubview(slider)
    // TextField
    textField.backgroundColor = UIColor.white
    textField.layer.borderWidth = 1
    textField.layer.borderColor = UIColor.black.cgColor
    textField.placeholder = "Type your name here"
    textField.delegate = self
    signPadTypeViewContainer.addSubview(textField)
    signPadsContainer.addSubview(signPadTypeViewContainer)
    addSubview(signPadsContainer)
    self.signPadTypeViewContainer.isHidden = true
    
    // Action Buttons
    actionButtonContainer.axis = .horizontal
    actionButtonContainer.distribution = .fillEqually
    actionButtonContainer.spacing = 10
    // Clear Button
    clearButton.setTitle("Clear", for: .normal)
    clearButton.isEnabled = false
    clearButton.setTitleColor(mineShaftColor, for: .normal)
    clearButton.backgroundColor = lightSilver
    // Get Signature Button
    getSignButton.setTitle("Get Sign", for: .normal)
    getSignButton.isEnabled = false
    getSignButton.setTitleColor(white, for: .normal)
    getSignButton.backgroundColor = mineShaftColor
    actionButtonContainer.addArrangedSubview(clearButton)
    actionButtonContainer.addArrangedSubview(getSignButton)
    addSubview(actionButtonContainer)
    
    // Listeners
    drawButton.addTarget(self, action: #selector(tabButtonTapped(_:)), for: .touchUpInside)
    typeButton.addTarget(self, action: #selector(tabButtonTapped(_:)), for: .touchUpInside)
    
    slider.addTarget(self, action: #selector(sliderValueChanged(_:)), for: .valueChanged)
    
    textField.addTarget(self, action: #selector(editTextChanged(_:)), for: .editingChanged)
    
    clearButton.addTarget(self, action: #selector(clearButtonTapped(_:)), for: .touchUpInside)
    
    getSignButton.addTarget(self, action: #selector(getSignButtonTapped(_:)), for: .touchUpInside)
  }
  
  required init?(coder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  // Set max length for textfield
  func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
      let currentString = (textField.text ?? "") as NSString
      let newString = currentString.replacingCharacters(in: range, with: string)

      return newString.count <= maxLength
  }
  
  // Functions
  private func getTypeSignatureImage() -> UIImage? {
    UIGraphicsBeginImageContextWithOptions(signaturePadType.bounds.size, false, 0)
    guard let context = UIGraphicsGetCurrentContext() else { return nil }
    signaturePadType.layer.render(in: context)
    let image = UIGraphicsGetImageFromCurrentImageContext()
    UIGraphicsEndImageContext()
    return image
  }
  
  private func getSignBase64() -> String? {
    let image = isDrawMode ? signaturePadDraw.getSignatureImage() : getTypeSignatureImage()
    guard let imageData = image?.pngData() else { return nil }
    return imageData.base64EncodedString()
  }
  
  private func onGetSignClick() {
    let signBase64 = getSignBase64() ?? ""
    
    if self.onGetSign != nil {
      self.onGetSign!(["sign": signBase64])
    }
  }
  
  /**
   * Reset signature view on configurations change,
   * so that we can generate new sign with new configurations
   */
  func resetState() {
    if !currentText.isEmpty {
      currentText = ""
    }
    clearButton.isEnabled = true
    getSignButton.isEnabled = true
  }
  
  // Props functions
  @objc func setSelectedTabColor(_ selectedTabColor: UIColor?) {
    self.selectedTabColor = selectedTabColor ?? mineShaftColor
    if isDrawMode {
      drawButton.backgroundColor = self.selectedTabColor
    } else {
      typeButton.backgroundColor = self.selectedTabColor
    }
  }
  
  @objc func setSelectedTabTextColor(_ selectedTabTextColor: UIColor?) {
    self.selectedTabTextColor = selectedTabTextColor ?? white
    if isDrawMode {
      drawButton.setTitleColor(self.selectedTabTextColor, for: .normal)
    } else {
      typeButton.setTitleColor(self.selectedTabTextColor, for: .normal)
    }
  }
  
  @objc func setUnselectedTabColor(_ unselectedTabColor: UIColor?) {
    self.unselectedTabColor = unselectedTabColor ?? lightSilver
    tabsContainer.backgroundColor = self.unselectedTabColor
  }
  
  @objc func setUnselectedTabTextColor(_ unselectedTabTextColor: UIColor?) {
    self.unselectedTabTextColor = unselectedTabTextColor ?? mineShaftColor
    if !isDrawMode {
      drawButton.setTitleColor(self.unselectedTabTextColor, for: .normal)
    } else {
      typeButton.setTitleColor(self.unselectedTabTextColor, for: .normal)
    }
  }
  
  @objc func setSeekBarColor(_ seekBarColor: UIColor?) {
    slider.thumbTintColor = seekBarColor ?? mineShaftColor
    slider.tintColor = seekBarColor ?? mineShaftColor
  }
  
  @objc func setClearText(_ clearText: String?) {
    clearButton.setTitle(clearText ?? "Clear", for: .normal)
  }
  
  @objc func setGetSignText(_ getSignText: String?) {
    getSignButton.setTitle(getSignText ?? "Get Sign", for: .normal)
  }
  
  @objc func setCapsTabText(_ capsTabText: Bool) {
    drawButton.setTitle(capsTabText ? "DRAW" : "Draw", for: .normal)
    typeButton.setTitle(capsTabText ? "TYPE" : "Type", for: .normal)
  }
  
  @objc func setCapsButtonText(_ capsButtonText: Bool) {
    let clearButtonTitle = clearButton.titleLabel?.text
    let getSignButtonTitle = getSignButton.titleLabel?.text
    clearButton.setTitle(capsButtonText ? clearButtonTitle?.uppercased() : clearButtonTitle?.capitalized, for: .normal)
    getSignButton.setTitle(capsButtonText ? getSignButtonTitle?.uppercased() : getSignButtonTitle?.capitalized, for: .normal)
  }
  
  @objc func setClearTextColor(_ clearTextColor: UIColor?) {
    let textColor = clearTextColor ?? mineShaftColor
    clearButton.setTitleColor(textColor, for: .normal)
  }
  
  @objc func setGetSignTextColor(_ getSignTextColor: UIColor?) {
    let textColor = getSignTextColor ?? UIColor.white
    getSignButton.setTitleColor(textColor, for: .normal)
  }
  
  @objc func setClearButtonBgColor(_ clearButtonBgColor: UIColor?) {
    clearButton.backgroundColor = clearButtonBgColor ?? lightSilver
  }
  
  @objc func setGetSignButtonBgColor(_ getSignButtonBgColor: UIColor?) {
    getSignButton.backgroundColor = getSignButtonBgColor ?? mineShaftColor
  }
  
  @objc func setStrokeColor(_ strokeColor: UIColor?) {
    self.resetState()
    self.signaturePadDraw.setStrokeColor(strokeColor ?? UIColor.black)
    self.signaturePadType.setStrokeColor(strokeColor ?? UIColor.black)
  }
  
  @objc func setDrawStrokeWidth(_ drawStrokeWidth: CGFloat) {
    resetState()
    signaturePadDraw.setDrawStrokeWidth(drawStrokeWidth == 0.0 ? 5.0 : drawStrokeWidth)
  }
  
  // Listeners
  @objc private func tabButtonTapped(_ sender: UIButton) {
    resetState()
    getSignButton.isEnabled = true
    isDrawMode = sender.tag == 0
    drawButton.backgroundColor = isDrawMode ? selectedTabColor : unselectedTabColor
    typeButton.backgroundColor = isDrawMode ? unselectedTabColor : selectedTabColor
    drawButton.setTitleColor(isDrawMode ? selectedTabTextColor : unselectedTabTextColor, for: .normal)
    typeButton.setTitleColor(isDrawMode ? unselectedTabTextColor : selectedTabTextColor, for: .normal)
    signaturePadDrawContainer.isHidden = !isDrawMode
    signPadTypeViewContainer.isHidden = isDrawMode
  }
  
  @objc private func sliderValueChanged(_ sender: UISlider) {
    resetState()
    signaturePadType.setSignatureFontSize(sender.value)
  }
  
  @objc private func editTextChanged(_ sender: UITextField) {
    let text = sender.text ?? ""
    
    if !text.isEmpty {
      if !clearButton.isEnabled {
        clearButton.isEnabled = true
      }
      if !getSignButton.isEnabled {
        getSignButton.isEnabled = true
      }
    } else {
      if clearButton.isEnabled {
        clearButton.isEnabled = false
      }
      if getSignButton.isEnabled {
        getSignButton.isEnabled = false
      }
    }
    signaturePadType.setText(text)
  }
  
  @objc private func clearButtonTapped(_ sender: UIButton) {
    if isDrawMode {
      signaturePadDraw.clearSignature()
    } else {
      textField.text = ""
      textField.sendActions(for: .editingChanged)
    }
    
    if self.onClear != nil {
      self.onClear!(["" : ""])
    }
  }
  
  @objc private func getSignButtonTapped(_ sender: UIButton) {
    if isDrawMode {
      onGetSignClick()
      getSignButton.isEnabled = false
      return
    }
    
    let newText = textField.text ?? ""
    if currentText != newText {
      currentText = newText
      onGetSignClick()
    }
  }
  
  // Protocol Functions
  func onSignature() {
    clearButton.isEnabled = true
    getSignButton.isEnabled = true
      
    if self.onDrawBegin != nil {
      self.onDrawBegin!(["" : ""])
    }
  }
    
  func onSignatureEnd() {
    if self.onDrawEnd != nil {
      self.onDrawEnd!(["" : ""])
    }
  }
  
  func onSignatureClear() {
    clearButton.isEnabled = false
    getSignButton.isEnabled = false
  }
  
  func onSliderProgressChange(value: Float) {
    slider.value -= value
    slider.sendActions(for: .valueChanged)
    if(slider.value == slider.minimumValue) {
      maxLength = textField.text!.count
    }
  }
  
  func setSliderRange(min: Float, max: Float) {
      // Reset slider values on dimensions change
      self.slider.minimumValue = min
      self.slider.maximumValue = max
      self.slider.value = max
      // Reset UITextField max length on dimensions change
      maxLength = Int.max
      // Calling this action to reset signature inside 
      // signature pad on dimensions change
      textField.sendActions(for: .editingChanged)
  }
}