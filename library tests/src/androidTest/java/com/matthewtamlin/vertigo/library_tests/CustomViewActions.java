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

package com.matthewtamlin.vertigo.library_tests;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;

import com.matthewtamlin.vertigo.library.SimpleVertigoCoordinator;
import com.matthewtamlin.vertigo.library.VertigoCoordinator.ActiveViewChangedListener;
import com.matthewtamlin.vertigo.library.VertigoView;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;

/**
 * Custom Espresso ViewActions for use in testing.
 */
public class CustomViewActions {
	/**
	 * The delay needed to ensure async events finish, measured in milliseconds.
	 */
	private static final int DELAY_MS = 100;

	/**
	 * Creates a ViewAction which performs the makeViewActive operation on a
	 * SimpleVertigoCoordinator. A short delay occurs after the operation completes to allow
	 * asynchronous events to complete.
	 *
	 * The returned ViewAction can only be applied to a SimpleVertigoCoordinator.
	 *
	 * @param viewKey
	 * 		the key of the view to make active
	 * @param animate
	 * 		whether or not the change should be animated
	 * @param listener
	 * 		the listener to call when the change completes, may be null
	 * @return the ViewAction
	 */
	public static ViewAction makeViewActive(final String viewKey, final boolean animate,
			final ActiveViewChangedListener listener) {
		return new ViewAction() {
			@Override
			public Matcher<View> getConstraints() {
				return isAssignableFrom(SimpleVertigoCoordinator.class);
			}

			@Override
			public String getDescription() {
				return "make " + viewKey + " view active" + (animate ? " using animation" :
						" without animation");
			}

			@Override
			public void perform(final UiController uiController, final View view) {
				((SimpleVertigoCoordinator) view).makeViewActive(viewKey, animate, listener);

				// Even with animations disabled, there are asynchronous events which must finish
				try {
					Thread.sleep(DELAY_MS);
				} catch (final InterruptedException e) {
					throw new RuntimeException("Wait failed due to interruption.");
				}
			}
		};
	}

	/**
	 * Creates a ViewAction which adds a VertigoView to a SimpleVertigoViewCoordinator and registers
	 * the view for coordination. A short delay occurs after the operation completes to allow
	 * asynchronous events to complete.
	 *
	 * The returned ViewAction can only be applied to a SimpleVertigoCoordinator.
	 *
	 * @param vertigoView
	 * 		the view to register, not null
	 * @param viewKey
	 * 		the key to associate with the registered view, not null
	 * @return the ViewAction
	 */
	public static ViewAction addViewAndRegister(final VertigoView vertigoView,
			final String viewKey) {
		return new ViewAction() {
			@Override
			public Matcher<View> getConstraints() {
				return isAssignableFrom(SimpleVertigoCoordinator.class);
			}

			@Override
			public String getDescription() {
				return "adding and registering view using view key " + viewKey;
			}

			@Override
			public void perform(final UiController uiController, final View view) {
				final SimpleVertigoCoordinator coordinator = (SimpleVertigoCoordinator) view;
				coordinator.addView((View) vertigoView);
				coordinator.registerViewForCoordination(vertigoView, viewKey);
			}
		};
	}
}