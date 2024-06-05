import {
  type ColorValue,
  type NativeSyntheticEvent,
  type ViewStyle,
  requireNativeComponent,
} from 'react-native';

type SignEvent = {
  sign: string;
};

type TRNSignatureView = {
  style: ViewStyle;
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

const RNSignatureView =
  requireNativeComponent<TRNSignatureView>('RNSignatureView');

export default RNSignatureView;
