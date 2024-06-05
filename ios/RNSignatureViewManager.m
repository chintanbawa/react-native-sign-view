//
//  SignatureViewManager.m
//  ReactNativeModuleDemo
//
//  Created by Chintan Bawa on 25/05/24.
//

#import "Foundation/Foundation.h"
#import "React/RCTViewManager.h"

@interface RCT_EXTERN_MODULE(RNSignatureViewManager, RCTViewManager)
  RCT_EXPORT_VIEW_PROPERTY(selectedTabColor, UIColor)
  RCT_EXPORT_VIEW_PROPERTY(selectedTabTextColor, UIColor)
  RCT_EXPORT_VIEW_PROPERTY(unselectedTabColor, UIColor)
  RCT_EXPORT_VIEW_PROPERTY(unselectedTabTextColor, UIColor)
  RCT_EXPORT_VIEW_PROPERTY(seekBarColor, UIColor)
  RCT_EXPORT_VIEW_PROPERTY(clearText, NSString)
  RCT_EXPORT_VIEW_PROPERTY(getSignText, NSString)
  RCT_EXPORT_VIEW_PROPERTY(capsTabText, BOOL)
  RCT_EXPORT_VIEW_PROPERTY(capsButtonText, BOOL)
  RCT_EXPORT_VIEW_PROPERTY(clearButtonBgColor, UIColor)
  RCT_EXPORT_VIEW_PROPERTY(clearTextColor, UIColor)
  RCT_EXPORT_VIEW_PROPERTY(getSignButtonBgColor, UIColor)
  RCT_EXPORT_VIEW_PROPERTY(getSignTextColor, UIColor)
  RCT_EXPORT_VIEW_PROPERTY(strokeColor, UIColor)
  RCT_EXPORT_VIEW_PROPERTY(drawStrokeWidth, CGFloat)
  RCT_EXPORT_VIEW_PROPERTY(onClear, RCTDirectEventBlock)
  RCT_EXPORT_VIEW_PROPERTY(onGetSign, RCTDirectEventBlock)
  RCT_EXPORT_VIEW_PROPERTY(onDrawBegin, RCTDirectEventBlock)
  RCT_EXPORT_VIEW_PROPERTY(onDrawEnd, RCTDirectEventBlock)
@end


