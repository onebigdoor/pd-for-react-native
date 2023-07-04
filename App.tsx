/* eslint-disable react-native/no-inline-styles */
import React, {FC, useEffect} from 'react';

import {
  SafeAreaView,
  TouchableOpacity,
  NativeModules,
  View,
  TouchableOpacityProps,
} from 'react-native';

export enum PureDataSignal {
  MasterEnable = 'onOff',
}

const {PureDataModule} = NativeModules;

interface ButtonProps extends TouchableOpacityProps {
  signal: PureDataSignal;
}

const PdButton: FC<ButtonProps> = ({signal, ...rest}) => {
  return (
    <TouchableOpacity
      {...rest}
      onPressIn={() =>
        PureDataModule.sendFloat(signal, 1, (err: string, name: string) =>
          console.log(err, name),
        )
      }
      onPressOut={() =>
        PureDataModule.sendFloat(signal, 0, (err: string, name: string) =>
          console.log(err, name),
        )
      }>
      <View
        style={{
          width: 300,
          aspectRatio: 1,
          borderRadius: 150,
          backgroundColor: 'white',
        }}
      />
    </TouchableOpacity>
  );
};

function App(): JSX.Element {
  useEffect(() => {
    console.log('load main pd patch');
    PureDataModule.loadPatch('test_tone.pd', (err: string, name: string) =>
      console.log(err, name),
    );
  }, []);
  return (
    <SafeAreaView
      style={{
        backgroundColor: '#911250',
        justifyContent: 'center',
        alignItems: 'center',
        flex: 1,
      }}>
      <PdButton signal={PureDataSignal.MasterEnable} />
    </SafeAreaView>
  );
}

export default App;
