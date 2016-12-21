package com.matthewtamlin.vertigo.library;

import java.util.Set;

/**
 * Coordinates multiple VertigoViews by sliding them up and down. Views may exist in the up position
 * (centred in the coordinator) or the down position (below the lower bound of the coordinator).
 * Multiple views may exist in the up position simultaneously, but only one will be in front of the
 * others. This view is declared active, and all others are considered inactive. To change the
 * active view, {@link #makeViewActive(String, boolean, ActiveViewChangedListener)} can be called.
 */
public interface VertigoViewCoordinator {
	/**
	 * Adds a vertigo view to this coordinator. The supplied view must be in the up position,
	 * however it may be behind another view.
	 *
	 * @param view
	 * 		the view to add, not null
	 * @param key
	 * 		a String key which uniquely identifies the view, not null
	 */
	public void addVertigoView(VertigoView view, String key);

	/**
	 * Returns the view which is mapped to the supplied key. If no view exists for the supplied key,
	 * null is returned.
	 *
	 * @param key
	 * 		the key to get the view for
	 * @return the view mapped to the supplied key, may be null
	 */
	public VertigoView getView(String key);

	/**
	 * @return a set containing all VertigoViews currently in the coordinator, not null
	 */
	public Set<VertigoView> getAllViews();

	/**
	 * @return a set containing all keys currently mapped to VertigoViews
	 */
	public Set<String> getAllKeys();

	/**
	 * Makes a view active by moving the coordinated views. If the view is already active then the
	 * method exits normally without changing anything. If the view is in the up position but is not
	 * the front view, all other views in the up position are moved down. If the view is in the down
	 * position, it is moved up.
	 *
	 * @param key
	 * 		the key for the view to move
	 * @param animate
	 * 		whether or not views should be animated when sliding up and down
	 * @param listener
	 * 		a listener to call when all changes have completed, may be null
	 */
	public void makeViewActive(String key, boolean animate, ActiveViewChangedListener listener);

	/**
	 * Changes the length of time to use when sliding views up and down.
	 *
	 * @param animationDurationMs
	 * 		the length of time, measured in milliseconds
	 */
	public void setAnimationDurationMs(int animationDurationMs);

	/**
	 * @return the length of time to use when sliding views up and down, measured in milliseconds
	 */
	public int getAnimationDurationMs();

	/**
	 * A callback to be delivered when the active view changes.
	 */
	public interface ActiveViewChangedListener {
		/**
		 * Called to indicate that the active view has changed.
		 *
		 * @param coordinator
		 * 		the coordinated which changed the view, not null
		 * @param activeView
		 * 		the view which was made active, not null
		 */
		public void onActiveViewChanged(VertigoViewCoordinator coordinator, VertigoView activeView);
	}
}