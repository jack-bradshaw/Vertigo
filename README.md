# Vertigo
Vertigo is an Android library for creating layouts where the views slide vertically. For example:

<div style="text-align:center"><img src="https://raw.githubusercontent.com/MatthewTamlin/Vertigo/master/artwork/example.gif" width="425"/></div>

## Download
Releases are made available through jCentre. Add `compile 'com.matthew-tamlin:vertigo:1.0.0’` to your gradle build file to use the latest version. Older versions are available in the [maven repo](https://bintray.com/matthewtamlin/maven/Vertigo).

## Usage 
There are two key interfaces in this library:
- `VertigoView` is a View which can be used in a VertigoCoordinator.
- `VertigoCoordinator` contains multiple VertigoViews and coordinates their positions.

The following implementations are provided for the VertigoView interface:
- `VertigoRelativeLayout` 
- `VertigoLinearLayout`
- `VertigoFrameLayout`
- `VertigoAdapterView`

All four classes can be used in a VertigoCoordinator, but are otherwise identical to their respective superclasses.

The SimpleVertigoCoordinator class is the only provided implementation of the VertigoCoordinator interface. It can easily be used in any layout, however care should be given to the coordinator's bottom position. The coordinated views are clipped as they slide out of the coordinator, which will not look good if the bottom of the coordinator is sitting in free space. To create a layout using the SimpleVertigoCoordinator, follow these steps:
  1. Create a layout containing a SimpleVertigoCoordinator.  
  2. Add VertigoViews as children of the coordinator just as you would add views to any FrameLayout.
  3. Call the `onStateChanged(State)` method of each view to ensure all views have the correct state.
  4. Register the views with the coordinator by calling the `SimpleVertigoView.registerViewForCoordination(VertigoView, String)` method.
  5. Call the `SimpleVertigoView.makeViewActive(String)` method to change the active view as desired.
  
In step 5 when the `makeViewActive(String)` method is called, one of three events will occur:
- If the target view is both in the up position and in front of all other views, none of the views change and no callbacks are delivered.
- If the target view is in the up position and hidden behind another view, all other views in the up position are slid down to reveal the target view. Callbacks are delivered to the target view and the views which were slid down.
- If the the target view is in the down position, it is slid up. A callback is delivered to the target view and the view which was previously displayed.

Step 3 is important because the states of the contained views (either active or inactive) are used by the coordinator when deciding which views to move. A view must only be declared active if it is both in the up position and in front of all other views in the coordinator.

For further details, read the Javadoc and have a look at [the example](example/src/main/java/com/matthewtamlin/vertigo/example).

## Important notes:
- VertigoViews must match the width and height of the coordinator.
- Unexpected results can occur if views which don't implement the VertigoView interface are added to a SimpleVertigoCoordinator.
- VertigoViews are responsible for remembering their own state. Each VertigoView must reliably return the last state declared to `onStateChanged(State)` when `getCurrentState()` is called.
- SimpleVertigoCoordinators cannot be resized after coordination begins.
- Additional views can be registered with a SimpleViewCoordinator after coordination has begun, so long as the new view has the correct state when added.

## License
This library is licensed under the Apache v2.0 licence. Have a look at [the license](LICENSE) for details.

## Dependencies and Attribution
This library uses the following open source libraries as level 1 dependencies:
- [Android Support Library](https://developer.android.com/topic/libraries/support-library/index.html), licensed under the Apache 2.0 license.
- [Android Utilities](https://github.com/MatthewTamlin/AndroidUtilities), licensed under the Apache 2.0 license.
- [Java Utilities](https://github.com/MatthewTamlin/JavaUtilities), licensed under the Apache 2.0 license.
- [Timber](https://github.com/JakeWharton/timber), licensed under the Apache 2.0 license.

## Compatibility
This library is compatible with Android 12 and up.
