package com.matthewtamlin.vertigo.library_tests;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.view.View;

import com.matthewtamlin.vertigo.library.SimpleVertigoCoordinator;
import com.matthewtamlin.vertigo.library.VertigoView;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CustomViewAssertions {
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