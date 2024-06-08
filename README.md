# @cbawa/react-native-signature-view
![demo-gif](https://github.com/chintanbawa/react-native-signature-view/assets/33692327/07fdb9c5-2b9e-46f9-84e5-de3fee1a75e2)

React Native Sign View module is developed for both `Android` and `iOS`. This package offers two signing options: `Draw` and `Type`. It does not require any dependencies, like `react-native-webview`.

- Supports Android and iOS
- Tested with RN 0.72
- Draw your Signature
- Type your Signature and plugin will draw it for you
- Change font size of typed signature via slider.
- No dependencies required, like `react-native-webview`

## Installation(for React Native v0.60.0 or greater)

```bash
yarn add @cbawa/react-native-signature-view
```
or
```bash
npm install --save @cbawa/react-native-signature-view
```

## - For Android - Autolinking will do it's magic.
## - For iOS 
```js 
$ npx pod-install
```
If you want to use the same font in iOS as we have in Android. You can download the font form [here](https://www.fontspace.com/wood-dragon-font-f108337) and follow the steps below:
- Create new group `Assets` inside root folder of your project
- Create new group `Fonts` inside `Assets` folder.
- Drap and Drop the downloaded font inside `Fonts` folder.
- Check `copy items if needed`
- Select `create groups`
- Inside `Add to targets`, select project name as target
- Open `info.plist` form your project
- Add a new row inside `info.plist` and type `Fonts provided by application`
- Expand `Fonts provided by application` and add font name (eg: Signature.ttf) as the value of `item 0`

## Usage

```js
import * as React from 'react';
import { StyleSheet } from 'react-native';
import RNSignatureView from '@cbawa/react-native-signature-view';

const App = () => {
  return (
    <RNSignatureView
      style={styles.signatureView}
      onGetSign={(event) => {
        console.log(event.nativeEvent.sign);
      }}
    />
  );
};

const styles = StyleSheet.create({
  signatureView: {
    width: '100%',
    height: 200,
    borderWidth: 1,
  },
});

export default App;
```

## Properties

---

| Prop                                |                   Type                   | Description                                                                                                                                           |
| :---------------------------------- | :--------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------------------------------- |
| style | `object` | styles the `RNSignatureView` container |
| onGetSign| `(event) => { console.log(event.nativeEvent.sign) }` | gives you base64 string for your signature |
| selectedTabColor| `string` | changes selected tab color. Default color is "#333" |
| selectedTabTextColor| `string` | changes selected tab text color. Default color is "#fff" |
| unselectedTabColor| `string` | changes unselected tab color. Default color is "#ddd" |
| unselectedTabTextColor| `string` | changes unselected tab text color. Default color is "#333" |
| seekBarColor| `string` | changes slider color inside `Type` option. Default color is "#333" |
| clearText| `string` |   changes `Clear` button text. Default text is "Clear" |
| getSignText| `string` |  changes `Get Sign` button text. Default text is "Get Sign" |
| capsTabText| `boolean` |  shows tabs text in capital letters. Default is false |
| capsButtonText| `boolean` |  shows buttons text in capital letters. Default is false |
| clearButtonBgColor| `string` | changes `Clear` button background color. Default color is "#ddd" |
| clearTextColor| `string` | changes `Clear` button text color. Default color is "#333" |
| getSignButtonBgColor| `string` | changes `Get Sign` button background color. Default color is "#333" |
| getSignTextColor| `string` | changes `Get Sign` button text color. Default color is "#fff" |
| strokeColor| `string` | changes stroke color in both `Draw` and `Type` options. Default color is "#000" |
| drawStrokeWidth| `number` | changes stroke width in `Draw` option. Default value is 10 |
| onDrawBegin| `() => void` | is called when a stroke is started in `Draw` option |
| onDrawEnd| `() => void` | is called when a stroke is ended in `Draw` option |
| onClear| `() => void` | is called when `Clear` button is pressed. |

## Example

```js
import * as React from 'react';
import { Image, Platform, StyleSheet, View } from 'react-native';
import RNSignatureView from '@cbawa/react-native-signature-view';

const App = () => {
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
    height: 200,
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

export default App;
```

## Example inside ScrollView

If you are going to use `RNSignatureView` inside a `ScrollView`, you must use `onDrawBegin` and `onDrawEnd` functions to enable/disable the scroll event of the `Scrollview`. Otherwise, `Scrollview` would highjack the touch and move event form `RNSignatureView`'s canvas, while drawing a signature.
Here's an example:

```js
import * as React from 'react';
import { Image, ScrollView, StyleSheet, View, Text } from 'react-native';
import RNSignatureView from '@cbawa/react-native-signature-view';

const App = () => {
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

export default App;
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)