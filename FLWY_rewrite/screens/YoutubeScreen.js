import React, { useState, useEffect } from 'react';
import {
  Text,
  View,
  Button,
  NativeModules,
  NativeEventEmitter,
  AsyncStorage,
  ToastAndroid
} from 'react-native'
import {WebView} from 'react-native-webview'
const {FLWYModule} = NativeModules;
const showToast = text => ToastAndroid.show(text, 1000)
YoutubeScreen = () => {
    const [url, setUrl] = useState("");
    const [title, setTitle] = useState("");
    const [data, setData] = useState([]);
    const getData = async () => {
        try {
          const jsonValue = await AsyncStorage.getItem('myArray');
          if (jsonValue != null) {
            setData(JSON.parse(jsonValue));
          }
        } catch (e) {
          console.log(e);
        }
    };
    let singleClick = new NativeEventEmitter(FLWYModule);
    const singleClickHandler = singleClick.addListener('BubbleSingleClick', (event) => {
      if(!FLWYModule.isInPictureInPictureMode()){
          FLWYModule.enterPipMode();
          startPiP();
      }
    });

    useEffect(() => { 
        let doubleClick = new NativeEventEmitter(FLWYModule);
        const doubleClickHandler = doubleClick.addListener("BubbleDoubleClick", (event) => {
            if(!FLWYModule.isInPictureInPictureMode()){
                In();
            }
        });
        return () => {
            doubleClickHandler.remove();
        };
    }, []);

    const storeData = async (url, title) => {
        try {
          let data = await AsyncStorage.getItem('myData');
          data = data != null ? JSON.parse(data) : []; // Kiểm tra data, nếu null thì tạo mảng mới
          const exists = data.some(item => item.url === url && item.title === title); // Kiểm tra xem đã tồn tại cặp url và title trong mảng chưa
          if (!exists) {
            data.push({url, title}); // Thêm cặp url và title vào mảng nếu chưa tồn tại
            await AsyncStorage.setItem('myData', JSON.stringify(data)); // Lưu mảng vào AsyncStorage
            showToast("Lấy url thành công");
          }
        } catch (error) {
          console.log(error);
        }
      };

    startPiP = () => {
        this.webView.injectJavaScript(`
        document.querySelector("#header-bar.sticky-player").style.display = 'none';
        document.querySelector(".player-container.sticky-player").style.top = "0px";
        document.querySelector("#app.sticky-player.watch-container-allow-sticky").style.paddingTop = '0px';
    `);
    };

    handleWebViewNavigationStateChange = (newNavState) => {
        setUrl(newNavState.url);
        setTitle(newNavState.title);
    }

    In = () => {
        storeData(url, title);
    }

    return(
        <WebView 
            ref = {(ref) => (this.webView = ref)}
            source={{uri: "http://www.youtube.com"}}
            onNavigationStateChange={this.handleWebViewNavigationStateChange}
        />
    )
}

export default YoutubeScreen;