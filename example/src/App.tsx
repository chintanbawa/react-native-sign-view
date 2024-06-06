import React from 'react';
import { SafeAreaView, StyleSheet } from 'react-native';
import SignatureViewInsideView from './SignatureViewInsideView';
// import SignatureViewInsideScrollView from './SignatureViewInsideScrollView';

const App = () => {
  return (
    <SafeAreaView style={styles.app}>
      <SignatureViewInsideView />
      {/* <SignatureViewInsideScrollView /> */}
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  app: {
    flex: 1,
  },
});

export default App;
