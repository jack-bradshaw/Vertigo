/*
 * Copyright 2016 Matthew Tamlin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.matthewtamlin.vertigo.library;

/**
 * A view which can be coordinated by a VertigoCoordinator. Each VertigoView can have one of two
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