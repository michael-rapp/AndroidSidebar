# AndroidSidebar - RELEASE NOTES

## Version 3.0.0 (Oct. 29th 2018)

A major release, which introduces the following changes:

- Migrated library to Android X.
- Updated dependency "AndroidUtil" to version 2.0.0.
- Updated targetSdkVersion to 28

## Version 2.1.11 (May 5th 2018)

A minor release, which introduces the following changes:

- Updated dependency "AndroidUtil" to version 1.20.2.
- Updated AppCompat support annotations library to version 27.1.1.

## Version 2.1.10 (Jan. 26th 2018)

A minor release, which introduces the following changes:

- Updated `targetSdkVersion` to API level 27 (Android 8.1).
- Updated dependency "AndroidUtil" to version 1.19.0.
- The data structure `ListenerList` is now used to manage event listeners.

## Version 2.1.9 (Dec. 30th 2017)

A minor release, which introduces the following changes:

- Updated `targetSdkVersion` to API level 26 (Android 8.0). This required to increase the minimum API level to 14.
- Updated AppCompat support annotations library to version 27.0.2.
- Updated dependency "AndroidUtil" to version 1.18.3.

## Version 2.1.8 (Apr. 23th 2017)

A minor release, which introduces the following changes:

- Updated AppCompat support annotations library to version 25.3.0.
- Updated dependency "AndroidUtil" to version 1.15.0. The class `DragHelper`, which is provided by the library is now used instead of implementing an own one.

## Version 2.1.7 (Jan. 26th 2017)

A minor release, which introduces the following changes:

- Updated `targetSdkVersion` to API level 25 (Android 7.1).
- Updated AppCompat support annotations library to version 25.1.0.
- Updated dependency "AndroidUtil" to version 1.12.3.

## Version 2.1.6 (Sep. 12th 2016)

A minor release, which introduces the following changes:

- Updated `targetSdkVersion` to API level 24 (Android 7.0).
- Updated AppCompat support annotations library to version 24.2.0.
- Updated dependency "AndroidUtil" to version 1.11.1.

## Version 2.1.5 (Mar. 18th 2016)

A bugfix release, which introduces the following changes:

- Fixed issue https://github.com/michael-rapp/AndroidSidebar/issues/6
- Updated dependency "AndroidUtil" to version 1.4.5.
- Updated AppCompat v7 support library to version 23.2.1.
- Fixed some deprecation warnings.
- Minor changes of the example app.

## Version 2.1.4 (Jan. 24th 2016)

A minor release, which introduces the following changes:

- The library is from now on distributed under the Apache License version 2.0. 
- Updated dependency "AndroidUtil" to version 1.4.3.
- Minor changes of the example app.

## Version 2.1.3 (Jan. 6th 2016)

A bugfix release, which fixes the following issue:

- https://github.com/michael-rapp/AndroidSidebar/issues/5

## Version 2.1.2 (Jan. 4th 2016)

A minor release, which introduces the following changes:

- Updated dependency "AndroidUtil" to version 1.3.0.
- Added implementation of the interface `Parcelable.Creator` to the class `SidebarSavedState`.

## Version 2.1.1 (Nov. 6th 2015)

A bugfix release, which fixes the following issue:

- https://github.com/michael-rapp/AndroidSidebar/issues/4

## Version 2.1.0 (Oct. 24th 2015)

A feature release, which introduces the following changes:

- Version 1.2.0 of the library "AndroidUtil" is now used to emulate the elevation of the sidebar. The methods and XML attributes, which previously allowed to specify a shadow width and color, have been replaced by one single method and attribute, which allows to set an elevation.

## Version 2.0.0 (Oct. 17th 2015)

A major release, which introduces the following changes:

- The project has been migrated from the legacy Eclipse ADT folder structure to Android Studio. It now uses the Gradle build system and the library as well as the example app are contained by one single project.
- The library can now be added to Android apps using the Gradle dependency `com.github.michael-rapp:android-sidebar:2.0.0`

## Version 1.1.1 (Nov. 27th 2014)

A bugfix release, which fixes the following re-opened issue:

- https://github.com/michael-rapp/AndroidMaterialDialog/issues/3

## Version 1.1.0 (Oct. 28th 2014)

A feature release, which introduces the following functionalities:
 
- Added the drag mode `EDGE`, which can be used as the hidden drag mode. When using this drag mode, only drag gesture's, which have been started at the edge of the sidebar's parent view (usually the edge of the display), are recognized.
- Non-linear interpolators are now used to animate the sidebar in order to create a more natural impression.

## Version 1.0.3 (Oct. 28th 2014)

A bugfix release, which fixes the following issues:

- https://github.com/michael-rapp/AndroidSidebar/issues/3

## Version 1.0.2 (Oct. 21th 2014)

A minor release, which provides the following changes:

- Prepared for Android 5.0 (API level 21).

## Version 1.0.1 (Sept. 22th 2014)

A bugfix release, which fixes the following issues:

- https://github.com/michael-rapp/AndroidSidebar/issues/1

## Version 1.0.0 (Sept. 22th 2014)

The first stable release, which provides a Preference implementation, which provides a custom view implementation, which allows to show a sidebar, which overlaps the view's main content and can be shown or hidden in an animated manner. The implementation initially provides the following features:

- The sidebar can be located at left or right edge of its parent view.
- The width of the sidebar can be set relatively to the width of its parent view. Additionally, a maximum width can be defined.
- It is possible to specify an offset, which defines the amount of space the sidebar is visible even if it currently hidden. The offset can be set relatively to the width of the parent view and a maximum value can be defined.
- The way, the main content is animated, when the sidebar becomes shown or hidden, can be adjusted in many ways, including the possibility to move it or to resize it.
- The sidebar's state can be changed via drag gestures or by clicking the touch screen of the device. For each possibility multiple settings exist, e.g. it is possible to change the sensibility for recognizing drag gestures.
- The speed of the animation, which is used to show or hide the sidebar, can be chosen freely.
- The sidebar may render a semi-transparent shadow in front of the main content, whose appearance can be adjusted concerning size and color.
- It is possible to fade in a semi-transparent overlay, which is shown in front of the main content, when the sidebar becomes shown. The transparency and color of the overlay can be adjusted.