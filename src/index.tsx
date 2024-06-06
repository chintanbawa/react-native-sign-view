import React from 'react';
import {
  View,
  StyleSheet,
  type ColorValue,
  type NativeSyntheticEvent,
  type ViewStyle,
  requireNativeComponent,
} from 'react-native';

type SignEvent = {
  sign: string;
};

type TRNSignatureView = {
  style?: ViewStyle;
  selectedTabColor?: ColorValue;
  selectedTabTextColor?: ColorValue;
  unselectedTabColor?: ColorValue;
  unselectedTabTextColor?: ColorValue;
  seekBarColor?: ColorValue;
  clearText?: string;
  getSignText?: string;
  capsTabText?: boolean;
  capsButtonText?: boolean;
  clearButtonBgColor?: ColorValue;
  clearTextColor?: ColorValue;
  getSignButtonBgColor?: ColorValue;
  getSignTextColor?: ColorValue;
  strokeColor?: ColorValue;
  drawStrokeWidth?: number;
  onGetSign: (event: NativeSyntheticEvent<SignEvent>) => void;
  onDrawBegin?: () => void;
  onDrawEnd?: () => void;
  onClear?: () => void;
};

const SignatureNativeComponent =
  requireNativeComponent<TRNSignatureView>('RNSignatureView');

const RNSignatureView = (props: TRNSignatureView) => (
  <View style={[styles.defaultStyle, props.style]}>
    <SignatureNativeComponent {...props} style={styles.signatureView} />
  </View>
);

const styles = StyleSheet.create({
  defaultStyle: {
    width: '100%',
    height: 180,
    borderWidth: 1,
  },
  signatureView: {
    width: '100%',
    height: '100%',
  },
});

export default RNSignatureView;
