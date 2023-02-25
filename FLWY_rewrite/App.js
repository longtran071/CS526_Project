import React from 'react'
import{
  Text,
  View,
  NativeModules,
  Button,
} from 'react-native'

const {FLWYModule} = NativeModules;
const add = () => {FLWYModule.show();};
const App = () => {
  return(
    <View>
      <Button title="bubble" onPress={add}></Button>
    </View>
  )
}

export default App;