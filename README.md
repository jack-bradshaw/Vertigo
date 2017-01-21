#Vertigo
Vertigo is an Android library for creating layouts where the views slide up and down over each other. For example:

<div style="text-align:center"><img src="https://raw.githubusercontent.com/MatthewTamlin/Vertigo/master/artwork/example.gif" width="425"/></div>

## Download
Releases are made available through jCentre. Add `compile 'com.matthew-tamlin:vertigo:1.0.0’` to your gradle build file to use the latest version. Older versions are available in the [maven repo](https://bintray.com/matthewtamlin/maven/Vertigo).

##Usage 
There are two key interfaces in this library:
- `VertigoView` is a View which can be coordinated by a VertigoView.
- `VertigoCoordinator` contains multiple VertigoViews and coordinates their position.

The following implementations are provided for the VertigoView interface:
- `VertigoRelativeLayout` 
- `VertigoLinearLayout`
- `VertigoFrameLayout`
- `VertigoAdapterView`

All four classes can be used in a VertigoCoordinator, but are otherwise identical to their superclasses.

The SimpleVertigoCoordinator is the only provided implementation of the VertigoCoordinator interface. The coordinator can easily be used in any layout, however the bottom of the view should be positioned so that the coordinated views are out of sight when they slid edown. This is important because the views are clipped as they pass the lower boundary of the coordinator, so the sliding effect will not look good if unconcealed.

The easiest way to use the library in your app is as follows:
  1. Create a layout containing a SimpleVertigoCoordinator.  
  2. Add multiple VertigoViews as children of the coordinator just as you would add views to any FrameLayout.
  3. Call the `onStateChanged(State)` method of each view to ensure all views have the right state.
  4. Register the views with the coordinator by calling the `SimpleVertigoView.registerViewForCoordination(VertigoView, String)` method.
  5. Call the `SimpleVertigoView.makeViewActive(String)` method when the active view needs to be changed.
  
  
Step 3 is important because the state of the VertigoViews (either active or inactive) is used by the coordinator when deciding how to position the views. A view is only active if it is in the up position and in front of all other views in the coordinator.

In step 5 when the `makeViewActive(String)` method is called, one of three scenarios will occur:
- If the target view is both in the up position and in front of all other views, none of the views change and no callbacks are delivered.
- If the target view is in the up position and hidden behind another view, all other views in the up position are slid down to reveal the target view. Callbacks are delivered to the target view and the views which were slid down.
- If the the target view is in the down position, it is slid up. A callback is delivered to the target view and the view which was previously displayed.

##Important notes:
- VertigoViews must match the width and height of the coordinator.
- Unexpected results can occur if views which don't implement the VertigoView interface are added to a SimpleVertigoCoordinator.
- VertigoViews are responsible for remembering their own state. Each VertigoView must reliabley return the last state delcared to `onStateChanged(State)` when `getCurrentState()` is called.
- SimpleVertigoCoordinators cannot be resized after coordination begins.
- Additional views can be registered with a SimpleViewCoordinator after coordination has begun, so long as the view has the correct state when added.

##Compatibility
This library is compatible with Android 12 and up.
