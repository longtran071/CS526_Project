import React from 'react';
import {
  Text,
  View,
  Button,
} from 'react-native'

import HomeScreen from './screens/HomeScreen';
import YoutubeScreen from './screens/YoutubeScreen';
import ListScreen from './screens/ListScreen';
import ApiScreen from './screens/ApiScreen';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';

const Stack = createStackNavigator();

const App = () => {
  return(
    <NavigationContainer>
      <Stack.Navigator initialRouteName="HomeScreen" screenOptions={{headerShown: false}}>
        <Stack.Screen name="HomeScreen" component={HomeScreen}/>
        <Stack.Screen name="YoutubeScreen" component={YoutubeScreen}/>
        <Stack.Screen name="ListScreen" component={ListScreen}/>
        <Stack.Screen name="ApiScreen" component={ApiScreen}/>
      </Stack.Navigator>
    </NavigationContainer>
  );
}

export default App;
// import React, {useState} from 'react';
// import {
//   Text,
//   View,
//   Button,
// } from 'react-native'
// import {WebView} from 'react-native-webview';

// const App = () => {
//   javascript = () => {
//     this.webView.injectJavaScript(`alert(document.documentElement.outerHTML)`);
//   }
//   return(
//     <WebView 
//       ref = {(ref) => (this.webView = ref)}
//       source={{uri: "https://www.youtube.com/watch?v=JX6KqrVBY2U&t=382s"}}
//       onLoadEnd={javascript}
//     />
//   )
// }

// export default App;
