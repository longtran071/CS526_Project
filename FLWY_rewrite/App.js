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
import { createNativeStackNavigator } from '@react-navigation/native-stack';

const Stack = createNativeStackNavigator();

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

