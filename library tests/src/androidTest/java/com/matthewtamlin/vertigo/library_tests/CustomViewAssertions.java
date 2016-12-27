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

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.view.View;

import com.matthewtamlin.vertigo.library.SimpleVertigoCoordinator;
import com.matthewtamlin.vertigo.library.VertigoView;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Custom Espresso ViewAssertions for use in testing.
 */
public class CustomViewAssertions {
	/**
	 * Creates a ViewAssertion which can be applied to a VertigoView to check that it has the
	 * desired state. The ViewAssertion can only be applied to a non-null VertigoView.
	 *
	 * @param state
	 * 		the expected state of the VertigoView
	 * @param viewName
	 * 		the name of the VertigoView, used purely for logging
	 * @return the ViewAssertion
	 */
	public static ViewAssertion hasState(final VertigoView.State state, final String viewName) {
		return new ViewAssertion() {
			@Override
			public void check(final View view, final NoMatchingViewException noViewFoundException) {
				if (view == null || !(view instanceof VertigoView)) {
					throw new AssertionError("view must be a non-null instance of VertigoView");
				} else {
					final VertigoView castView = (VertigoView) view;
					assertThat(viewName + " has wrong state.", castView.getCurrentState(),
							is(state));
				}
			}
		};
	}

	/**
	 * Creates a ViewAssertion which can be applied to a VertigoView to check that it is in the down
	 * position of a SimpleVertigoCoordinator. The ViewAssertion can only be applied to a non-null
	 * VertigoView.
	 *
	 * @param coordinator
	 * 		the coordinator the VertigoView is in, not null
	 * @return the ViewAssertion
	 */
	public static ViewAssertion isInDownPosition(final SimpleVertigoCoordinator coordinator) {
		return new ViewAssertion() {
			@Override
			public void check(final View view, final NoMatchingViewException noViewFoundException) {
				if (view == null || !(view instanceof VertigoView)) {
					throw new AssertionError("view must be a non-null instance of VertigoView");
				} else {
					final float expectedY = coordinator.getY() + view.getHeight();
					assertThat("view has wrong y position.", view.getY(), is(expectedY));
				}
			}
		};
	}

	/**
	 * Creates a ViewAssertion which can be applied to a VertigoView to check that it is in the up
	 * position of a SimpleVertigoCoordinator. The ViewAssertion can only be applied to a non-null
	 * VertigoView.
	 *
	 * @param coordinator
	 * 		the coordinator the VertigoView is in, not null
	 * @return the ViewAssertion
	 */
	public static ViewAssertion isInUpPosition(final SimpleVertigoCoordinator coordinator) {
		return new ViewAssertion() {
			@Override
			public void check(final View view, final NoMatchingViewException noViewFoundException) {
				if (view == null || !(view instanceof VertigoView)) {
					throw new AssertionError("view must be a non-null instance of VertigoView");
				} else {
					final float expectedY = coordinator.getY();
					assertThat("view has wrong y position.", view.getY(), is(expectedY));
				}
			}
		};
	}
}