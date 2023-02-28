import React, {useState, useCallback, useRef} from 'react';
import {
    View,
    Text,
    NativeEventEmitter,
    NativeModules
} from 'react-native';
import YoutubePlayer from "react-native-youtube-iframe";
const {FLWYModule} = NativeModules;

let singleClick1 = new NativeEventEmitter(FLWYModule);
const singleClickHandler1 = singleClick1.addListener('BubbleSingleClick', (event) => {
  if(!FLWYModule.isInPictureInPictureMode()){
      FLWYModule.enterPipMode();
  }
});

ApiScreen = ({route}) => {
    const {url} = route.params;
    const playerRef = useRef(null);
    const [playing, setPlaying] = useState(true);
    const onStateChange = useCallback((state) => {
        if (state === "ended") {
          playerRef.current?.seekTo(0, true);
        }
    }, []);
    return(
        <View>
            <YoutubePlayer
                ref={playerRef}
                height={300}
                play={true}
                videoId={url}
                onChangeState={onStateChange}
            />
        </View>
    )
}

export default ApiScreen;