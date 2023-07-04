#PD for React Native

## Setup

You can start from this project and not have to worry about adding the pd-for-ios library. If you are migrating to an existing project, make sure you check out these steps:

-

## Editing and adding patches

Patches need to be manually updated in both the `ios` and `android` apps. There is a better solution to this problem, but here is my current workflow:

- work on patches in `ios/patches` using desktop pd
- test on ios.
- save and commit changes as needed. you'll need to re-build app to see changes. live reload will not work.
- zip `patches` folder and copy to `android/app/src/main/res/raw` overwriting previous version.
- test android

## Troubleshooting

- [The official docs for Native Modules](https://reactnative.dev/docs/native-modules-intro) will answer a lot of questions.
- Recommend working on native files in native editors. ie obj-c files in XCode and .java files in Android Studio. Those IDEs have lots of built-in features that will do you favors.
- `AppDelegate.mm` and `MainActivity.java` each have a commented out line that can load a patch manually, bypassing the Native Module. You can also load `test_tone_on.pd` which will play a sound without any interaction.
