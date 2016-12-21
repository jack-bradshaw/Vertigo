package com.matthewtamlin.vertigo.library;

/**
 * A view which can be coordinated by a VertigoViewCoordinator. Each VertigoView can have one of two
 * states: active and inactive. In the active state, the view is being displayed to the user and
 * should be available for interaction. In the inactive state, the view is not visible to the user
 * and should not be available for interaction. The view is notified of state changes by the
 * coordinator through the {@link #getCurrentState()} method.
 */
public interface VertigoView {
	/**
	 * @return the current state of this VertigoView, not null
	 */
	public State getCurrentState();

	/**
	 * Called to declare the state of this VertigoView.
	 *
	 * @param state
	 * 		the new state of this VertigoView, not null
	 */
	public void onStateChanged(State state);

	/**
	 * The possible states a VertigoView can have.
	 */
	public enum State {
		/**
		 * The view is currently displayed to the user and can receive user interactions.
		 */
		ACTIVE,

		/**
		 * The view is currently hidden from the user and cannot receive user interactions.
		 */
		INACTIVE
	}
}