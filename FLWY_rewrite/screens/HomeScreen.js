import React from 'react';
import {
  Text,
  View,
  Button,
  NativeModules,
  NativeEventEmitter,
  TouchableOpacity,
  StyleSheet,
  Image
} from 'react-native'


const {FLWYModule} = NativeModules;

const add = () => FLWYModule.show();
const hide = () => FLWYModule.hide();

export default HomeScreen = ({navigation}) => {
    return(
        <View style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.title}>Floating Window for Youtube</Text>
                <Image style={styles.img} source={require('../assets/myImage.png')} />
            </View>
            <View style={styles.buttonContainer}>
                <TouchableOpacity style={styles.btn} title='Add Bubble' onPress={add}>
                    <Text style={styles.btn_text}>Nổi bong bóng</Text>
                </TouchableOpacity>
                <TouchableOpacity style={styles.btn} title='Hide Bubble' onPress={hide}>
                    <Text style={styles.btn_text}>Xóa bong bóng</Text>
                </TouchableOpacity>
                <TouchableOpacity style={styles.btn} title="YoutubeWeb" onPress={() => {navigation.navigate('YoutubeScreen')}}>
                    <Text style={styles.btn_text}>Youtube Web</Text>
                </TouchableOpacity>
                <TouchableOpacity style={styles.btn} title="YoutubeAPI" onPress={() => {navigation.navigate('ListScreen')}}>
                    <Text style={styles.btn_text}>Youtube API</Text>
                </TouchableOpacity>
            </View>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
      flex: 1,
      backgroundColor: 'coral'
    },
    header: {
      flex: 5,
      justifyContent: 'center',
      alignItems: 'center',
    },
    buttonContainer: {
      flex: 5,    
      justifyContent: 'space-around',
      alignItems: 'center',
    },
    title: {
      fontSize: 24,
      fontWeight: 'bold',
      color: 'white',
    },
    icon: {
      fontSize: 20,
    },
    img: {
        width: 150,
        height: 150,
    },
    btn: {
        backgroundColor: 'blue',
        padding: 13,
        borderRadius: 25,
        width: 150,
        height: 50,
        marginTop: 5
    },
    btn_text: {
        color: 'white', textAlign: 'center', fontWeight: 'bold'
    }
});
