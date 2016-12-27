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

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.FrameLayout;

import com.matthewtamlin.vertigo.library.SimpleVertigoCoordinator;
import com.matthewtamlin.vertigo.library.VertigoCoordinator.ActiveViewChangedListener;
import com.matthewtamlin.vertigo.library.VertigoFrameLayout;
import com.matthewtamlin.vertigo.library.VertigoView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.matthewtamlin.android_testing_tools.library.EspressoHelper.viewToViewInteraction;
import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;
import static com.matthewtamlin.vertigo.library.VertigoView.State.ACTIVE;
import static com.matthewtamlin.vertigo.library.VertigoView.State.INACTIVE;
import static com.matthewtamlin.vertigo.library_tests.CustomViewActions.addViewAndRegister;
import static com.matthewtamlin.vertigo.library_tests.CustomViewActions.makeViewActive;
import static com.matthewtamlin.vertigo.library_tests.CustomViewAssertions.hasState;
import static com.matthewtamlin.vertigo.library_tests.CustomViewAssertions.isInDownPosition;
import static com.matthewtamlin.vertigo.library_tests.CustomViewAssertions.isInUpPosition;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the SimpleVertigoCoordinator class.
 */
@RunWith(AndroidJUnit4.class)
public class TestSimpleVertigoCoordinator {
	/**
	 * A key which uniquely identifies the back subview.
	 */
	private static final String BACK_SUBVIEW_KEY = "back subview";

	/**
	 * A key which uniquely identifies the middle subview.
	 */
	private static final String MIDDLE_SUBVIEW_KEY = "middle subview";

	/**
	 * A key which uniquely identifies the front subview.
	 */
	private static final String FRONT_SUBVIEW_KEY = "front subview";

	/**
	 * Hosts a SimpleVertigoView.
	 */
	@Rule
	public final ActivityTestRule<SimpleVertigoCoordinatorTestHarness> testHarnessRule = new
			ActivityTestRule<>(SimpleVertigoCoordinatorTestHarness.class);

	/**
	 * The view under test, as a direct view reference.
	 */
	private SimpleVertigoCoordinator testViewDirect;

	/**
	 * The view under test, as an espresso ViewInteraction.
	 */
	private ViewInteraction testViewEspresso;

	/**
	 * A mock of the ActiveViewChangedListener interface.
	 */
	private ActiveViewChangedListener listener;

	/**
	 * A view which is contained in the test view and coordinated by it, as a direct view reference.
	 * The view is initially the back most view in the coordinator.
	 */
	private VertigoFrameLayout backSubviewDirect;

	/**
	 * A view which is contained in the test view and coordinated by it, as a direct view reference.
	 * The view is initially the middle view in the coordinator.
	 */
	private VertigoFrameLayout middleSubviewDirect;

	/**
	 * A view which is contained in the test view and coordinated by it, as a direct view reference.
	 * The view is initially the front most view in the coordinator.
	 */
	private VertigoFrameLayout frontSubviewDirect;

	/**
	 * A view which is contained in the test view and coordinated by it, as an espresso
	 * ViewInteraction. The view is initially the back most view in the coordinator.
	 */
	private ViewInteraction backSubviewEspresso;

	/**
	 * A view which is contained in the test view and coordinated by it, as an espresso
	 * ViewInteraction. The view is initially the middle view in the coordinator.
	 */
	private ViewInteraction middleSubviewEspresso;

	/**
	 * A view which is contained in the test view and coordinated by it, as an espresso
	 * ViewInteraction. The view is initially the front most view in the coordinator.
	 */
	private ViewInteraction frontSubviewEspresso;

	/**
	 * Sets up the test environment and checks that all preconditions are satisfied.
	 */
	@Before
	public void setup() {
		testViewDirect = checkNotNull(testHarnessRule.getActivity().getTestView(),
				"testViewDirect cannot be null.");
		testViewEspresso = checkNotNull(viewToViewInteraction(testViewDirect, "1"),
				"testViewEspresso cannot be null.");

		listener = mock(ActiveViewChangedListener.class);

		backSubviewDirect = createSubview(INACTIVE);
		middleSubviewDirect = createSubview(INACTIVE);
		frontSubviewDirect = createSubview(ACTIVE);

		backSubviewEspresso = viewToViewInteraction(backSubviewDirect, "2");
		middleSubviewEspresso = viewToViewInteraction(middleSubviewDirect, "3");
		frontSubviewEspresso = viewToViewInteraction(frontSubviewDirect, "4");

		testViewEspresso.perform(addViewAndRegister(backSubviewDirect, BACK_SUBVIEW_KEY));
		testViewEspresso.perform(addViewAndRegister(middleSubviewDirect, MIDDLE_SUBVIEW_KEY));
		testViewEspresso.perform(addViewAndRegister(frontSubviewDirect, FRONT_SUBVIEW_KEY));
	}

	/**
	 * Test to ensure that the {@link SimpleVertigoCoordinator#makeViewActive(String, boolean,
	 * ActiveViewChangedListener)} method functions correctly when the target view is in the up
	 * position, behind one or more other views. This test examines the case where animations are
	 * enabled. The test will only pass if the views are all in the correct locations and the
	 * correct callback is delivered.
	 */
	@Test
	public void testMakeViewActive_viewIsInUpPositionBehindActiveView_usingAnimation() {
		testViewEspresso.perform(makeViewActive(MIDDLE_SUBVIEW_KEY, true, listener));

		backSubviewEspresso.check(hasState(INACTIVE, "back subview"));
		middleSubviewEspresso.check(hasState(ACTIVE, "middle subview"));
		frontSubviewEspresso.check(hasState(INACTIVE, "front subview"));

		backSubviewEspresso.check(isInDownPosition(testViewDirect));
		middleSubviewEspresso.check(isInUpPosition(testViewDirect));
		frontSubviewEspresso.check(isInDownPosition(testViewDirect));

		verify(listener, only()).onActiveViewChanged(testViewDirect, middleSubviewDirect);
	}

	/**
	 * Test to ensure that the {@link SimpleVertigoCoordinator#makeViewActive(String, boolean,
	 * ActiveViewChangedListener)} method functions correctly when the target view is in the up
	 * position, behind one or more other views. This test examines the case where animations are
	 * disabled. The test will only pass if the views are all in the correct locations and the
	 * correct callback is delivered.
	 */
	@Test
	public void testMakeViewActive_viewIsInUpPositionBehindActiveView_withoutAnimation() {
		testViewEspresso.perform(makeViewActive(MIDDLE_SUBVIEW_KEY, false, listener));

		backSubviewEspresso.check(hasState(INACTIVE, "back subview"));
		middleSubviewEspresso.check(hasState(ACTIVE, "middle subview"));
		frontSubviewEspresso.check(hasState(INACTIVE, "front subview"));

		backSubviewEspresso.check(isInDownPosition(testViewDirect));
		middleSubviewEspresso.check(isInUpPosition(testViewDirect));
		frontSubviewEspresso.check(isInDownPosition(testViewDirect));

		verify(listener, only()).onActiveViewChanged(testViewDirect, middleSubviewDirect);
	}

	/**
	 * Test to ensure that the {@link SimpleVertigoCoordinator#makeViewActive(String, boolean,
	 * ActiveViewChangedListener)} method functions correctly when the target view is in the down
	 * position. This test examines the case where animations are enabled. The test will only pass
	 * if the views are all in the correct location and the correct callback is delivered.
	 */
	@Test
	public void testMakeViewActive_viewIsInDownPosition_usingAnimation() {
		// Setup for actual test, make front and middle slide down
		testViewEspresso.perform(makeViewActive(BACK_SUBVIEW_KEY, true, listener));

		backSubviewEspresso.check(hasState(ACTIVE, "back subview"));
		middleSubviewEspresso.check(hasState(INACTIVE, "middle subview"));
		frontSubviewEspresso.check(hasState(INACTIVE, "front subview"));

		backSubviewEspresso.check(isInUpPosition(testViewDirect));
		middleSubviewEspresso.check(isInDownPosition(testViewDirect));
		frontSubviewEspresso.check(isInDownPosition(testViewDirect));

		verify(listener, times(1)).onActiveViewChanged(testViewDirect, backSubviewDirect);

		// Make middle subview slide up
		testViewEspresso.perform(makeViewActive(MIDDLE_SUBVIEW_KEY, true, listener));

		backSubviewEspresso.check(hasState(INACTIVE, "back subview"));
		middleSubviewEspresso.check(hasState(ACTIVE, "middle subview"));
		frontSubviewEspresso.check(hasState(INACTIVE, "front subview"));

		backSubviewEspresso.check(isInUpPosition(testViewDirect));
		middleSubviewEspresso.check(isInUpPosition(testViewDirect));
		frontSubviewEspresso.check(isInDownPosition(testViewDirect));

		verify(listener, times(1)).onActiveViewChanged(testViewDirect, middleSubviewDirect);
	}

	/**
	 * Test to ensure that the {@link SimpleVertigoCoordinator#makeViewActive(String, boolean,
	 * ActiveViewChangedListener)} method functions correctly when the target view is in the down
	 * position. This test examines the case where animations are disabled. The test will only pass
	 * if the views are all in the correct location and the correct callback is delivered.
	 */
	@Test
	public void testMakeViewActive_viewIsInDownPosition_withoutAnimation() {
		// Slide middle subview and 3 down
		testViewEspresso.perform(makeViewActive(BACK_SUBVIEW_KEY, false, listener));

		backSubviewEspresso.check(hasState(ACTIVE, "back subview"));
		middleSubviewEspresso.check(hasState(INACTIVE, "middle subview"));
		frontSubviewEspresso.check(hasState(INACTIVE, "front subview"));

		backSubviewEspresso.check(isInUpPosition(testViewDirect));
		middleSubviewEspresso.check(isInDownPosition(testViewDirect));
		frontSubviewEspresso.check(isInDownPosition(testViewDirect));

		verify(listener, times(1)).onActiveViewChanged(testViewDirect, backSubviewDirect);

		// Make middle subview slide up
		testViewEspresso.perform(makeViewActive(MIDDLE_SUBVIEW_KEY, false, listener));

		backSubviewEspresso.check(hasState(INACTIVE, "back subview"));
		middleSubviewEspresso.check(hasState(ACTIVE, "middle subview"));
		frontSubviewEspresso.check(hasState(INACTIVE, "front subview"));

		backSubviewEspresso.check(isInUpPosition(testViewDirect));
		middleSubviewEspresso.check(isInUpPosition(testViewDirect));
		frontSubviewEspresso.check(isInDownPosition(testViewDirect));

		verify(listener, times(1)).onActiveViewChanged(testViewDirect, middleSubviewDirect);
	}

	/**
	 * Test to ensure that the {@link SimpleVertigoCoordinator#makeViewActive(String, boolean,
	 * ActiveViewChangedListener)} method functions correctly when the target view is already
	 * active. This test examines the case where animations are enabled. The test will only pass if
	 * the views are all in the correct location and no callback is delivered.
	 */
	@Test
	public void testMakeViewActive_viewIsAlreadyActive_usingAnimation() {
		testViewEspresso.perform(makeViewActive(FRONT_SUBVIEW_KEY, true, listener));

		backSubviewEspresso.check(hasState(INACTIVE, "back subview"));
		middleSubviewEspresso.check(hasState(INACTIVE, "middle subview"));
		frontSubviewEspresso.check(hasState(ACTIVE, "front subview"));

		backSubviewEspresso.check(isInUpPosition(testViewDirect));
		middleSubviewEspresso.check(isInUpPosition(testViewDirect));
		frontSubviewEspresso.check(isInUpPosition(testViewDirect));

		verify(listener, never()).onActiveViewChanged(eq(testViewDirect), any(VertigoView.class));
	}

	/**
	 * Test to ensure that the {@link SimpleVertigoCoordinator#makeViewActive(String, boolean,
	 * ActiveViewChangedListener)} method functions correctly when the target view is already
	 * active. This test examines the case where animations are disabled. The test will only pass if
	 * the views are all in the correct location and no callback is delivered.
	 */
	@Test
	public void testMakeViewActive_viewIsAlreadyActive_withoutAnimation() {
		testViewEspresso.perform(makeViewActive(FRONT_SUBVIEW_KEY, false, listener));

		backSubviewEspresso.check(hasState(INACTIVE, "back subview"));
		middleSubviewEspresso.check(hasState(INACTIVE, "middle subview"));
		frontSubviewEspresso.check(hasState(ACTIVE, "front subview"));

		backSubviewEspresso.check(isInUpPosition(testViewDirect));
		middleSubviewEspresso.check(isInUpPosition(testViewDirect));
		frontSubviewEspresso.check(isInUpPosition(testViewDirect));

		verify(listener, never()).onActiveViewChanged(eq(testViewDirect), any(VertigoView.class));
	}

	/**
	 * Test to ensure that the {@link SimpleVertigoCoordinator#makeViewActive(String, boolean,
	 * ActiveViewChangedListener)} method functions correctly when multiple transitions occur. This
	 * test examines the case where animations are enabled. The test will only pass if the views are
	 * all in the correct location and the correct callbacks are delivered.
	 */
	@Test
	public void testMakeViewActive_multipleTransitions_usingAnimation() {
		testViewEspresso.perform(makeViewActive(MIDDLE_SUBVIEW_KEY, true, listener));

		backSubviewEspresso.check(hasState(INACTIVE, "back subview"));
		middleSubviewEspresso.check(hasState(ACTIVE, "middle subview"));
		frontSubviewEspresso.check(hasState(INACTIVE, "front subview"));

		backSubviewEspresso.check(isInDownPosition(testViewDirect));
		middleSubviewEspresso.check(isInUpPosition(testViewDirect));
		frontSubviewEspresso.check(isInDownPosition(testViewDirect));

		verify(listener, times(1)).onActiveViewChanged(testViewDirect, middleSubviewDirect);

		testViewEspresso.perform(makeViewActive(BACK_SUBVIEW_KEY, true, listener));

		backSubviewEspresso.check(hasState(ACTIVE, "back subview"));
		middleSubviewEspresso.check(hasState(INACTIVE, "middle subview"));
		frontSubviewEspresso.check(hasState(INACTIVE, "front subview"));

		backSubviewEspresso.check(isInUpPosition(testViewDirect));
		middleSubviewEspresso.check(isInUpPosition(testViewDirect));
		frontSubviewEspresso.check(isInDownPosition(testViewDirect));

		verify(listener, times(1)).onActiveViewChanged(testViewDirect, backSubviewDirect);

		testViewEspresso.perform(makeViewActive(FRONT_SUBVIEW_KEY, true, listener));

		backSubviewEspresso.check(hasState(INACTIVE, "back subview"));
		middleSubviewEspresso.check(hasState(INACTIVE, "middle subview"));
		frontSubviewEspresso.check(hasState(ACTIVE, "front subview"));

		backSubviewEspresso.check(isInUpPosition(testViewDirect));
		middleSubviewEspresso.check(isInUpPosition(testViewDirect));
		frontSubviewEspresso.check(isInUpPosition(testViewDirect));

		verify(listener, times(1)).onActiveViewChanged(testViewDirect, frontSubviewDirect);
	}

	/**
	 * Test to ensure that the {@link SimpleVertigoCoordinator#makeViewActive(String, boolean,
	 * ActiveViewChangedListener)} method functions correctly when multiple transitions occur. This
	 * test examines the case where animations are disabled. The test will only pass if the views
	 * are all in the correct location and the correct callbacks are delivered.
	 */
	@Test
	public void testMakeViewActive_multipleTransitions_withoutAnimation() {
		testViewEspresso.perform(makeViewActive(MIDDLE_SUBVIEW_KEY, false, listener));

		backSubviewEspresso.check(hasState(INACTIVE, "back subview"));
		middleSubviewEspresso.check(hasState(ACTIVE, "middle subview"));
		frontSubviewEspresso.check(hasState(INACTIVE, "front subview"));

		backSubviewEspresso.check(isInDownPosition(testViewDirect));
		middleSubviewEspresso.check(isInUpPosition(testViewDirect));
		frontSubviewEspresso.check(isInDownPosition(testViewDirect));

		verify(listener, times(1)).onActiveViewChanged(testViewDirect, middleSubviewDirect);

		testViewEspresso.perform(makeViewActive(BACK_SUBVIEW_KEY, false, listener));

		backSubviewEspresso.check(hasState(ACTIVE, "back subview"));
		middleSubviewEspresso.check(hasState(INACTIVE, "middle subview"));
		frontSubviewEspresso.check(hasState(INACTIVE, "front subview"));

		backSubviewEspresso.check(isInUpPosition(testViewDirect));
		middleSubviewEspresso.check(isInUpPosition(testViewDirect));
		frontSubviewEspresso.check(isInDownPosition(testViewDirect));

		verify(listener, times(1)).onActiveViewChanged(testViewDirect, backSubviewDirect);

		testViewEspresso.perform(makeViewActive(FRONT_SUBVIEW_KEY, false, listener));

		backSubviewEspresso.check(hasState(INACTIVE, "back subview"));
		middleSubviewEspresso.check(hasState(INACTIVE, "middle subview"));
		frontSubviewEspresso.check(hasState(ACTIVE, "front subview"));

		backSubviewEspresso.check(isInUpPosition(testViewDirect));
		middleSubviewEspresso.check(isInUpPosition(testViewDirect));
		frontSubviewEspresso.check(isInUpPosition(testViewDirect));

		verify(listener, times(1)).onActiveViewChanged(testViewDirect, frontSubviewDirect);
	}

	/**
	 * Creates a VertigoFrameLayout with the supplied state.
	 *
	 * @param state
	 * 		the state of the view, not null
	 * @return the view
	 */
	private VertigoFrameLayout createSubview(final VertigoView.State state) {
		checkNotNull(state, "state cannot be null.");

		final VertigoFrameLayout subview = new VertigoFrameLayout(testHarnessRule.getActivity());
		subview.setLayoutParams(new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
		subview.onStateChanged(state);

		return subview;
	}
}