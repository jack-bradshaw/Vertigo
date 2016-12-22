package com.matthewtamlin.vertigo.library_tests;

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
	private static final String BACK_SUBVIEW_KEY = "back subview";

	private static final String MIDDLE_SUBVIEW_KEY = "middle subview";

	private static final String FRONT_SUBVIEW_KEY = "front subview";

	/**
	 * Hosts the view being tested.
	 */
	@Rule
	public final ActivityTestRule<SimpleVertigoCoordinatorTestHarness> testHarnessRule = new
			ActivityTestRule<>(SimpleVertigoCoordinatorTestHarness.class);

	private SimpleVertigoCoordinator testViewDirect;

	private ViewInteraction testViewEspresso;

	private ActiveViewChangedListener listener;

	private VertigoFrameLayout backSubviewDirect;

	private VertigoFrameLayout middleSubviewDirect;

	private VertigoFrameLayout frontSubviewDirect;

	private ViewInteraction backSubviewEspresso;

	private ViewInteraction middleSubviewEspresso;

	private ViewInteraction frontSubviewEspresso;

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

	private VertigoFrameLayout createSubview(final VertigoView.State state) {
		final VertigoFrameLayout subview = new VertigoFrameLayout(testHarnessRule.getActivity());
		subview.setLayoutParams(new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
		subview.onStateChanged(state);

		return subview;
	}
}