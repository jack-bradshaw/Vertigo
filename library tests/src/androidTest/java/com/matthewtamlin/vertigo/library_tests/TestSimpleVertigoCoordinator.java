package com.matthewtamlin.vertigo.library_tests;

import android.graphics.Color;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.FrameLayout;

import com.matthewtamlin.vertigo.library.SimpleVertigoCoordinator;
import com.matthewtamlin.vertigo.library.VertigoCoordinator.ActiveViewChangedListener;
import com.matthewtamlin.vertigo.library.VertigoFrameLayout;
import com.matthewtamlin.vertigo.library.VertigoView;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.matthewtamlin.android_utilities.library.testing.EspressoHelper.viewToViewInteraction;
import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;
import static com.matthewtamlin.vertigo.library.VertigoView.State.ACTIVE;
import static com.matthewtamlin.vertigo.library.VertigoView.State.INACTIVE;
import static com.matthewtamlin.vertigo.library_tests.CustomViewActions.addViewAndRegister;
import static com.matthewtamlin.vertigo.library_tests.CustomViewActions.makeViewActive;
import static com.matthewtamlin.vertigo.library_tests.CustomViewAssertions.hasState;
import static com.matthewtamlin.vertigo.library_tests.CustomViewAssertions.isInDownPosition;
import static com.matthewtamlin.vertigo.library_tests.CustomViewAssertions.isInUpPosition;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class TestSimpleVertigoCoordinator {
	private static final String SUBVIEW_1_KEY = "subview 1";

	private static final String SUBVIEW_2_KEY = "subview 2";

	private static final String SUBVIEW_3_KEY = "subview 3";

	/**
	 * Hosts the view being tested.
	 */
	@Rule
	public final ActivityTestRule<SimpleVertigoCoordinatorTestHarness> testHarnessRule = new
			ActivityTestRule<>(SimpleVertigoCoordinatorTestHarness.class);

	private SimpleVertigoCoordinator testViewDirect;

	private ViewInteraction testViewEspresso;

	private ActiveViewChangedListener listener;

	private VertigoFrameLayout subview1Direct;

	private VertigoFrameLayout subview2Direct;

	private VertigoFrameLayout subview3Direct;

	private ViewInteraction subview1Espresso;

	private ViewInteraction subview2Espresso;

	private ViewInteraction subview3Espresso;

	@Before
	public void setup() {
		testViewDirect = checkNotNull(testHarnessRule.getActivity().getTestView(),
				"testViewDirect cannot be null.");
		testViewEspresso = checkNotNull(viewToViewInteraction(testViewDirect, "1"),
				"testViewEspresso cannot be null.");

		listener = mock(ActiveViewChangedListener.class);

		subview1Direct = createSubview(INACTIVE);
		subview2Direct = createSubview(INACTIVE);
		subview3Direct = createSubview(ACTIVE);

		subview1Espresso = viewToViewInteraction(subview1Direct, "2");
		subview2Espresso = viewToViewInteraction(subview2Direct, "3");
		subview3Espresso = viewToViewInteraction(subview3Direct, "4");

		testViewEspresso.perform(addViewAndRegister(subview1Direct, SUBVIEW_1_KEY));
		testViewEspresso.perform(addViewAndRegister(subview2Direct, SUBVIEW_2_KEY));
		testViewEspresso.perform(addViewAndRegister(subview3Direct, SUBVIEW_3_KEY));
	}

	@Test
	public void testMakeViewActive_viewIsInUpPositionBehindActiveView_usingAnimation() {
		testViewEspresso.perform(makeViewActive(SUBVIEW_2_KEY, true, listener));

		subview1Espresso.check(hasState(INACTIVE, "subview 1"));
		subview2Espresso.check(hasState(ACTIVE, "subview 2"));
		subview3Espresso.check(hasState(INACTIVE, "subview 3"));

		subview1Espresso.check(isInDownPosition(testViewDirect));
		subview2Espresso.check(isInUpPosition(testViewDirect));
		subview3Espresso.check(isInDownPosition(testViewDirect));

		verify(listener, only()).onActiveViewChanged(testViewDirect, subview2Direct);
	}

	@Test
	public void testMakeViewActive_viewIsInUpPositionBehindActiveView_withoutAnimation() {
		testViewEspresso.perform(makeViewActive(SUBVIEW_2_KEY, false, listener));

		subview1Espresso.check(hasState(INACTIVE, "subview 1"));
		subview2Espresso.check(hasState(ACTIVE, "subview 2"));
		subview3Espresso.check(hasState(INACTIVE, "subview 3"));

		subview1Espresso.check(isInDownPosition(testViewDirect));
		subview2Espresso.check(isInUpPosition(testViewDirect));
		subview3Espresso.check(isInDownPosition(testViewDirect));

		verify(listener, only()).onActiveViewChanged(testViewDirect, subview2Direct);
	}

	@Test
	public void testMakeViewActive_viewIsInDownPosition_usingAnimation() {
		// Slide subview 2 and 3 down
		testViewEspresso.perform(makeViewActive(SUBVIEW_1_KEY, true, listener));

		subview1Espresso.check(hasState(ACTIVE, "subview 1"));
		subview2Espresso.check(hasState(INACTIVE, "subview 2"));
		subview3Espresso.check(hasState(INACTIVE, "subview 3"));

		subview1Espresso.check(isInUpPosition(testViewDirect));
		subview2Espresso.check(isInDownPosition(testViewDirect));
		subview3Espresso.check(isInDownPosition(testViewDirect));

		verify(listener, times(1)).onActiveViewChanged(testViewDirect, subview1Direct);

		// Make subview 2 slide up
		testViewEspresso.perform(makeViewActive(SUBVIEW_2_KEY, true, listener));

		subview1Espresso.check(hasState(INACTIVE, "subview 1"));
		subview2Espresso.check(hasState(ACTIVE, "subview 2"));
		subview3Espresso.check(hasState(INACTIVE, "subview 3"));

		subview1Espresso.check(isInUpPosition(testViewDirect));
		subview2Espresso.check(isInUpPosition(testViewDirect));
		subview3Espresso.check(isInDownPosition(testViewDirect));

		verify(listener, times(1)).onActiveViewChanged(testViewDirect, subview2Direct);
	}

	@Test
	public void testMakeViewActive_viewIsInDownPosition_withoutAnimation() {
		// Slide subview 2 and 3 down
		testViewEspresso.perform(makeViewActive(SUBVIEW_1_KEY, false, listener));

		subview1Espresso.check(hasState(ACTIVE, "subview 1"));
		subview2Espresso.check(hasState(INACTIVE, "subview 2"));
		subview3Espresso.check(hasState(INACTIVE, "subview 3"));

		subview1Espresso.check(isInUpPosition(testViewDirect));
		subview2Espresso.check(isInDownPosition(testViewDirect));
		subview3Espresso.check(isInDownPosition(testViewDirect));

		verify(listener, times(1)).onActiveViewChanged(testViewDirect, subview1Direct);

		// Make subview 2 slide up
		testViewEspresso.perform(makeViewActive(SUBVIEW_2_KEY, false, listener));

		subview1Espresso.check(hasState(INACTIVE, "subview 1"));
		subview2Espresso.check(hasState(ACTIVE, "subview 2"));
		subview3Espresso.check(hasState(INACTIVE, "subview 3"));

		subview1Espresso.check(isInUpPosition(testViewDirect));
		subview2Espresso.check(isInUpPosition(testViewDirect));
		subview3Espresso.check(isInDownPosition(testViewDirect));

		verify(listener, times(1)).onActiveViewChanged(testViewDirect, subview2Direct);
	}

	@Test
	public void testMakeViewActive_viewIsAlreadyActive_usingAnimation() {
		testViewEspresso.perform(makeViewActive(SUBVIEW_3_KEY, true, listener));

		subview1Espresso.check(hasState(INACTIVE, "subview 1"));
		subview2Espresso.check(hasState(INACTIVE, "subview 2"));
		subview3Espresso.check(hasState(ACTIVE, "subview 3"));

		subview1Espresso.check(isInUpPosition(testViewDirect));
		subview2Espresso.check(isInUpPosition(testViewDirect));
		subview3Espresso.check(isInUpPosition(testViewDirect));

		verify(listener, never()).onActiveViewChanged(eq(testViewDirect), any(VertigoView.class));
	}

	@Test
	public void testMakeViewActive_viewIsAlreadyActive_withoutAnimation() {
		testViewEspresso.perform(makeViewActive(SUBVIEW_3_KEY, false, listener));

		subview1Espresso.check(hasState(INACTIVE, "subview 1"));
		subview2Espresso.check(hasState(INACTIVE, "subview 2"));
		subview3Espresso.check(hasState(ACTIVE, "subview 3"));

		subview1Espresso.check(isInUpPosition(testViewDirect));
		subview2Espresso.check(isInUpPosition(testViewDirect));
		subview3Espresso.check(isInUpPosition(testViewDirect));

		verify(listener, never()).onActiveViewChanged(eq(testViewDirect), any(VertigoView.class));
	}

	@Test
	public void testMakeViewActive_multipleTransitions_usingAnimation() {
		testViewEspresso.perform(makeViewActive(SUBVIEW_2_KEY, true, listener));

		subview1Espresso.check(hasState(INACTIVE, "subview 1"));
		subview2Espresso.check(hasState(ACTIVE, "subview 2"));
		subview3Espresso.check(hasState(INACTIVE, "subview 3"));

		subview1Espresso.check(isInDownPosition(testViewDirect));
		subview2Espresso.check(isInUpPosition(testViewDirect));
		subview3Espresso.check(isInDownPosition(testViewDirect));

		verify(listener, times(1)).onActiveViewChanged(testViewDirect, subview2Direct);

		testViewEspresso.perform(makeViewActive(SUBVIEW_1_KEY, true, listener));

		subview1Espresso.check(hasState(ACTIVE, "subview 1"));
		subview2Espresso.check(hasState(INACTIVE, "subview 2"));
		subview3Espresso.check(hasState(INACTIVE, "subview 3"));

		subview1Espresso.check(isInUpPosition(testViewDirect));
		subview2Espresso.check(isInUpPosition(testViewDirect));
		subview3Espresso.check(isInDownPosition(testViewDirect));

		verify(listener, times(1)).onActiveViewChanged(testViewDirect, subview1Direct);

		testViewEspresso.perform(makeViewActive(SUBVIEW_3_KEY, true, listener));

		subview1Espresso.check(hasState(INACTIVE, "subview 1"));
		subview2Espresso.check(hasState(INACTIVE, "subview 2"));
		subview3Espresso.check(hasState(ACTIVE, "subview 3"));

		subview1Espresso.check(isInUpPosition(testViewDirect));
		subview2Espresso.check(isInUpPosition(testViewDirect));
		subview3Espresso.check(isInUpPosition(testViewDirect));

		verify(listener, times(1)).onActiveViewChanged(testViewDirect, subview3Direct);
	}

	@Test
	public void testMakeViewActive_multipleTransitions_withoutAnimation() {
		testViewEspresso.perform(makeViewActive(SUBVIEW_2_KEY, false, listener));

		subview1Espresso.check(hasState(INACTIVE, "subview 1"));
		subview2Espresso.check(hasState(ACTIVE, "subview 2"));
		subview3Espresso.check(hasState(INACTIVE, "subview 3"));

		subview1Espresso.check(isInDownPosition(testViewDirect));
		subview2Espresso.check(isInUpPosition(testViewDirect));
		subview3Espresso.check(isInDownPosition(testViewDirect));

		verify(listener, times(1)).onActiveViewChanged(testViewDirect, subview2Direct);

		testViewEspresso.perform(makeViewActive(SUBVIEW_1_KEY, false, listener));

		subview1Espresso.check(hasState(ACTIVE, "subview 1"));
		subview2Espresso.check(hasState(INACTIVE, "subview 2"));
		subview3Espresso.check(hasState(INACTIVE, "subview 3"));

		subview1Espresso.check(isInUpPosition(testViewDirect));
		subview2Espresso.check(isInUpPosition(testViewDirect));
		subview3Espresso.check(isInDownPosition(testViewDirect));

		verify(listener, times(1)).onActiveViewChanged(testViewDirect, subview1Direct);

		testViewEspresso.perform(makeViewActive(SUBVIEW_3_KEY, false, listener));

		subview1Espresso.check(hasState(INACTIVE, "subview 1"));
		subview2Espresso.check(hasState(INACTIVE, "subview 2"));
		subview3Espresso.check(hasState(ACTIVE, "subview 3"));

		subview1Espresso.check(isInUpPosition(testViewDirect));
		subview2Espresso.check(isInUpPosition(testViewDirect));
		subview3Espresso.check(isInUpPosition(testViewDirect));

		verify(listener, times(1)).onActiveViewChanged(testViewDirect, subview3Direct);
	}

	private VertigoFrameLayout createSubview(final VertigoView.State state) {
		final VertigoFrameLayout subview = new VertigoFrameLayout(testHarnessRule.getActivity());
		subview.setLayoutParams(new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
		subview.onStateChanged(state);

		return subview;
	}

	private void checkViewIsInUpPosition(final VertigoView view) {
		final View viewCast = (View) view;
		final float expectedY = testViewDirect.getY();

		assertThat("view has wrong y position.", viewCast.getY(), CoreMatchers.is(expectedY));
	}

	private void checkViewIsInDownPosition(final VertigoView view) {
		final View viewCast = (View) view;
		final float expectedY = testViewDirect.getY() + viewCast.getHeight();

		assertThat("view has wrong y position.", viewCast.getY(), CoreMatchers.is(expectedY));
	}
}