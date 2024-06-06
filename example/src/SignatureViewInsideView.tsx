import * as React from 'react';
import { Image, Platform, StyleSheet, View } from 'react-native';
import RNSignatureView from 'react-native-signature-view';

export default function SignatureViewInsideView() {
  const [sign, setSign] = React.useState('');
  return (
    <View style={styles.container}>
      <RNSignatureView
        style={styles.signatureView}
        selectedTabColor="#080"
        selectedTabTextColor="#fff"
        unselectedTabColor="#ddd"
        unselectedTabTextColor="#333"
        seekBarColor="#080"
        clearText="Wipe"
        getSignText="Capture"
        capsTabText
        capsButtonText
        clearButtonBgColor="#ddd"
        clearTextColor="#333"
        getSignButtonBgColor="#080"
        getSignTextColor="#fff"
        strokeColor="#080"
        drawStrokeWidth={14}
        onGetSign={(event) => {
          setSign(event.nativeEvent.sign);
        }}
        onClear={() => setSign('')}
      />
      {sign && (
        <View style={styles.view}>
          <Image
            source={{ uri: `data:image/png;base64,${sign}` }}
            alt="sign"
            style={styles.signImage}
          />
        </View>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    padding: 20,
  },
  signatureView: {
    width: '100%',
    height: Platform.OS === 'ios' ? 220 : 180,
    borderWidth: 1,
  },
  view: {
    width: '100%',
    height: 100,
    padding: 5,
    borderWidth: 1,
    borderColor: 'black',
    marginTop: 10,
  },
  signImage: {
    width: '100%',
    height: '100%',
    objectFit: 'contain',
    backgroundColor: 'white',
  },
});
