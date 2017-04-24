# TaipeiTechStudentAndroid
## Firebase

- [Analytics](https://firebase.google.com/docs/analytics/?hl=zh-cn):分析使用者行為
- [Cloud Messaging](https://firebase.google.com/docs/cloud-messaging/?hl=zh-cn)：雲端訊息傳遞
- [Crash Reporting](https://firebase.google.com/docs/crash/?hl=zh-cn)：崩潰分析

## Fragment

- ActivityFragment：活動介面
- CourseFragment：課程介面
- CalendarFragment：行事曆介面
- OtherFragment：其他介面
    - CreditFragment：學分介面
    - NportalFragment:入口網站介面
    - SettingFragment:設定介面
    
## Bottom Navigation Bar
Library 連結:https://github.com/Ashok-Varma/BottomNavigation

    使用此第三方庫原因：良好的自訂性
    
外型參考：![](https://i.imgur.com/ayWPnb6.jpg)

## Resourse
- drawable:圖檔資源，只保留與裝置螢幕相對應的圖檔資源
- layout:介面
- mipmap:圖檔資源，保留各解析度，建議將 icon 存到此
- value：建議將所有用到的顏色、尺寸、字串、樣式皆存到 xml 中，方便以後維護修改
    - colors.xml:顏色
    - dimens.xml:尺寸
    - strings.xml：字串
    - styles.xml：樣式

## Proguard
ProGuard 工具會移除不用的程式碼，並使用語意不明的名稱來重新命名類別、欄位和方法，藉此縮短、最佳化和模糊處理您的程式碼。

### build.gradle
```xml=
buildTypes {
        release {
            minifyEnabled true
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
}
```

## Icon 生成工具
### [Android Asset Studio](https://romannurik.github.io/AndroidAssetStudio/)
Android Asset Studio可以自己選擇其內置的圖標，也可以從本地文件選擇圖標，提供了7種圖標生成工具，分別是app啟動圖標、shortcut圖標、動畫圖標、通知欄圖標、.9圖標、普通圖標、actionbar或者tab上的圖標。
![](https://i.imgur.com/69Nq5Qc.png)

