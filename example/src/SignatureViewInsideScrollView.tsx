import * as React from 'react';
import { Image, ScrollView, StyleSheet, View, Text } from 'react-native';
import RNSignatureView from 'react-native-signature-view';

export default function SignatureViewInsideScrollView() {
  const [scrollEnabled, setScrollEnabled] = React.useState(true);
  const [sign, setSign] = React.useState('');
  return (
    <ScrollView scrollEnabled={scrollEnabled}>
      <View style={styles.container}>
        <View style={styles.view}>
          <Text style={styles.text}>One View</Text>
        </View>
        <RNSignatureView
          style={styles.signatureView}
          onGetSign={(event) => {
            setSign(event.nativeEvent.sign);
          }}
          onDrawBegin={() => setScrollEnabled(false)}
          onDrawEnd={() => setScrollEnabled(true)}
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
        <View style={styles.view}>
          <Text style={styles.text}>Another View</Text>
        </View>
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
  },
  signatureView: {
    width: '100%',
    height: 180,
    marginTop: 10,
  },
  view: {
    width: '100%',
    height: 140,
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
  text: {
    color: '#333',
    textAlign: 'center',
    fontWeight: '600',
  },
});
