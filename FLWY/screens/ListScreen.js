import React, {useState, useEffect} from 'react';
import {
    View,
    Text,
    TouchableOpacity,
    AsyncStorage,
    ScrollView,
    Button,
    StyleSheet
} from 'react-native';
import Youtube from 'react-native-youtube';

ListScreen = ({navigation}) => {
    const [data, setData] = useState([]);

    useEffect(() => {
        const getData = async () =>{
            try {
                const jsonValue = await AsyncStorage.getItem('myData');
                if(jsonValue != null){
                    setData(JSON.parse(jsonValue));
                }
            } catch (e) {
                console.log(e);
            }
        };
        getData();
    }, []);

    const deleteItem = async (index) => {
        let newData = [...data];
        newData.splice(index, 1);
        await AsyncStorage.setItem('myData', JSON.stringify(newData));
        setData(newData);
    };

    change = (url) => {
        const startIndex = 30;
        const endIndex = startIndex + 11;
        const subString = url.substring(startIndex, endIndex);
        navigation.navigate('ApiScreen', {url: subString});
    }

    return(
        <View>
            <ScrollView>
                {data.map((item, index) => (
                    <TouchableOpacity key={index} onPress={()=>{change(item.url)}} style={styles.btn}>
                        <Text style={styles.btn_title}>{item.title}</Text>
                        <Text>{item.url}</Text>
                        <TouchableOpacity
                            onPress={() => deleteItem(index)}
                            style={{
                                backgroundColor: 'blue',
                                padding: 10,
                                borderRadius: 5,
                                width: 100,
                                height: 40,
                                marginTop: 5
                            }}>
                            <Text style={{ color: 'white', textAlign: 'center' }}>Xóa</Text>
                        </TouchableOpacity>
                    </TouchableOpacity>
                ))}
            </ScrollView>
        </View>
    )
};

export default ListScreen;

const styles = StyleSheet.create({
    btn: {
        marginTop: 10, borderWidth: 1, borderColor: 'black', height: 120, borderRadius: 10, backgroundColor: "coral",
    },

    btn_title: {
        fontSize: 15,
        color: 'black',
        fontWeight: 'bold',
    },
});