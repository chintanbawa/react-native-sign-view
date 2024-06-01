import {
  requireNativeComponent,
  UIManager,
  Platform,
  type ViewStyle,
} from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-signature-view' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

type SignatureViewProps = {
  color: string;
  style: ViewStyle;
};

const ComponentName = 'SignatureViewView';

export const SignatureViewView =
  UIManager.getViewManagerConfig(ComponentName) != null
    ? requireNativeComponent<SignatureViewProps>(ComponentName)
    : () => {
        throw new Error(LINKING_ERROR);
      };
