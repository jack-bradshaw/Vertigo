#Vertigo
Vertigo is an Android library for creating layouts where the views slide up and down over each other. For example:

<div style="text-align:center"><img src="https://raw.githubusercontent.com/MatthewTamlin/Vertigo/master/artwork/example.gif" width="425"/></div>

## Download
Releases are made available through jCentre. Add `compile 'com.matthew-tamlin:vertigo:1.0.0’` to your gradle build file to use the latest version. Older versions are available in the [maven repo](https://bintray.com/matthewtamlin/maven/Vertigo).

##Usage 
The library has two primary components:
- The `SlidingVertigoCoordinator` class. A FrameLayout subclass which implements the `VertigoCoordinator` interface.
- The `VertigoView` interface. Define views which can be used with a VertigoCoordinator. Several implementations are provided.

The library can be used as follows:
  1. Create a layout containing a SimpleVertigoCoordinator.
  2. Add multiple VertigoView implementations as children of the coordinator. Do this as you would normally add views to a FrameLayout.
  3. Register the views with the coordinator by calling the `SimpleVertigoView.registerViewForCoordination(VertigoView, String)` method. For safety, only do this once the layout has been inflated.
  4. Call the `SimpleVertigoView.makeViewActive(String)` method to change the view being displayed. This will deliver the appropriate callbacks to the VertigoViews.
  
When the `makeViewActive(String)` method is called, one of three scenarios will occur:
- If the target view is both in the up position and in front of all other views, none of the views change and no callbacks are delivered.
- If the target view is in the up position and hidden behind another view, all other views in the up position are slid down to reveal the target view. Callbacks are delivered to the target view and the views which were slid down.
- If the the target view is in the down position, it is slid up. A callback is delivered to the target view and the view which was previously displayed.

##Important notes:
- The sliding coordinator can only coordinate views which implement the VertigoView interface, however it still functions as a regular FrameLayout so can hold any view.
- The library contains subclasses of the standard layouts (linear, relative, frame etc.) which implement the VerigoView interface.
- VertigoViews are responsible for remembering their own state, and must reliable return the last state delcared. This means that `VertigoView.getCurrentState()` method must always return the last state declared to `VertigoView.onStateChanged(State)`. If this condition is not satisfied, then the coordinator will not function correctly.
- The `SimpleVertigoCoordinator` must not change size.
- New views can be registered with the coordinator after coordination has begun, however the views must be centred in the coordinator when added and their state must be correct. A view can only be active if it is centred in the coordinator (i.e. not slid down) and at the top of the view stack within the coordinator.

##Compatibility
This library is compatible with Android 12 and up.
