# AndroidSidebar - RELEASE NOTES

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