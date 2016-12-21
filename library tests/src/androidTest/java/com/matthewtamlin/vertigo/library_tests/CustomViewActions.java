package com.matthewtamlin.vertigo.library_tests;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;

import com.matthewtamlin.vertigo.library.SimpleVertigoCoordinator;
import com.matthewtamlin.vertigo.library.VertigoCoordinator.ActiveViewChangedListener;
import com.matthewtamlin.vertigo.library.VertigoView;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;

public class CustomViewActions {
	private static final int DELAY_MS = 100;

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

	public static ViewAction addViewAndRegister(final VertigoView vertigoView,
			final String viewKey) {
		return new ViewAction() {
			@Override
			public Matcher<View> getConstraints() {
				return isAssignableFrom(SimpleVertigoCoordinator.class);
			}

			@Override
			public String getDescription() {
				return "adding and registering view with view key " + viewKey;
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