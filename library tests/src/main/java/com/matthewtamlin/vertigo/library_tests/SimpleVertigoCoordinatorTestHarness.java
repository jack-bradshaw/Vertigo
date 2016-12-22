package com.matthewtamlin.vertigo.library_tests;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.matthewtamlin.android_utilities.library.testing.ControlsOverViewTestHarness;
import com.matthewtamlin.vertigo.library.SimpleVertigoCoordinator;
import com.matthewtamlin.vertigo.library.VertigoFrameLayout;

import static com.matthewtamlin.vertigo.library.VertigoView.State.ACTIVE;
import static com.matthewtamlin.vertigo.library.VertigoView.State.INACTIVE;

/**
 * Test harness for the SimpleVertigoCoordinator.
 */
@SuppressLint("SetTextI18n") // Not relevant to testing
public class SimpleVertigoCoordinatorTestHarness
		extends ControlsOverViewTestHarness<SimpleVertigoCoordinator> {
	/**
	 * Coordinator key for the first subview.
	 */
	private static final String SUBVIEW_1_KEY = "view 1";

	/**
	 * Coordinator key for the second subview.
	 */
	private static final String SUBVIEW_2_KEY = "view 2";

	/**
	 * Coordinator key for the third subview.
	 */
	private static final String SUBVIEW_3_KEY = "view 3";

	/**
	 * The view under test.
	 */
	private SimpleVertigoCoordinator testView;

	/**
	 * The first view in the coordinator.
	 */
	private VertigoFrameLayout subview1;

	/**
	 * The second view in the coordinator.
	 */
	private VertigoFrameLayout subview2;

	/**
	 * The third view in the coordinator.
	 */
	private VertigoFrameLayout subview3;

	@Override
	protected void onCreate(final @Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getControlsContainer().addView(createAddSubviewsButton());
		getControlsContainer().addView(createRegisterSubviewsButton());
		getControlsContainer().addView(createUnregisterSubviewsButton());
		getControlsContainer().addView(createMakeView1ActiveWithAnimationButton());
		getControlsContainer().addView(createMakeView2ActiveWithAnimationButton());
		getControlsContainer().addView(createMakeView3ActiveWithAnimationButton());
		getControlsContainer().addView(createMakeView1ActiveWithoutAnimationButton());
		getControlsContainer().addView(createMakeView2ActiveWithoutAnimationButton());
		getControlsContainer().addView(createMakeView3ActiveWithoutAnimationButton());
	}

	@Override
	public SimpleVertigoCoordinator getTestView() {
		if (testView == null) {
			testView = new SimpleVertigoCoordinator(this);
		}

		return testView;
	}

	/**
	 * Returns subview 1, creating it if necessary.
	 *
	 * @return subview1, not null
	 */
	private VertigoFrameLayout getSubview1() {
		if (subview1 == null) {
			subview1 = new VertigoFrameLayout(this);
			subview1.setBackgroundColor(Color.GREEN);
			subview1.onStateChanged(INACTIVE);
		}

		return subview1;
	}

	/**
	 * Returns subview 2, creating it if necessary.
	 *
	 * @return subview2, not null
	 */
	private VertigoFrameLayout getSubview2() {
		if (subview2 == null) {
			subview2 = new VertigoFrameLayout(this);
			subview2.setBackgroundColor(Color.YELLOW);
			subview1.onStateChanged(INACTIVE);
		}

		return subview2;
	}

	/**
	 * Returns subview 3, creating it if necessary.
	 *
	 * @return subview3, not null
	 */
	private VertigoFrameLayout getSubview3() {
		if (subview3 == null) {
			subview3 = new VertigoFrameLayout(this);
			subview3.setBackgroundColor(Color.RED);
			subview1.onStateChanged(ACTIVE);
		}

		return subview3;
	}

	/**
	 * @return a Button which adds the subviews to the test view when clicked
	 */
	private Button createAddSubviewsButton() {
		final Button b = new Button(this);
		b.setText("Add subviews");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				testView.addView(getSubview1());
				testView.addView(getSubview2());
				testView.addView(getSubview3());
			}
		});

		return b;
	}

	/**
	 * @return a Button which registers the subviews when clicked
	 */
	private Button createRegisterSubviewsButton() {
		final Button b = new Button(this);
		b.setText("Register subviews");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				testView.registerViewForCoordination(subview1, SUBVIEW_1_KEY);
				testView.registerViewForCoordination(subview2, SUBVIEW_2_KEY);
				testView.registerViewForCoordination(subview3, SUBVIEW_3_KEY);
			}
		});

		return b;
	}

	/**
	 * @return a Button which unregisters the subviews when clicked
	 */
	private Button createUnregisterSubviewsButton() {
		final Button b = new Button(this);
		b.setText("Unregister subviews");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().unregisterViewForCoordination(SUBVIEW_1_KEY);
				getTestView().unregisterViewForCoordination(SUBVIEW_2_KEY);
				getTestView().unregisterViewForCoordination(SUBVIEW_3_KEY);
			}
		});

		return b;
	}

	/**
	 * @return a Button which makes view 1 active when clicked (using animations)
	 */
	private Button createMakeView1ActiveWithAnimationButton() {
		final Button b = new Button(this);
		b.setText("Make subview 1 active (animated)");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().makeViewActive(SUBVIEW_1_KEY, true, null);
			}
		});

		return b;
	}

	/**
	 * @return a Button which makes view 2 active when clicked (using animations)
	 */
	private Button createMakeView2ActiveWithAnimationButton() {
		final Button b = new Button(this);
		b.setText("Make subview 2 active (animated)");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().makeViewActive(SUBVIEW_2_KEY, true, null);
			}
		});

		return b;
	}

	/**
	 * @return a Button which makes view 3 active when clicked (using animations)
	 */
	private Button createMakeView3ActiveWithAnimationButton() {
		final Button b = new Button(this);
		b.setText("Make subview 3 active (animated)");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().makeViewActive(SUBVIEW_3_KEY, true, null);
			}
		});

		return b;
	}

	/**
	 * @return a Button which makes view 1 active when clicked (without using animations)
	 */
	private Button createMakeView1ActiveWithoutAnimationButton() {
		final Button b = new Button(this);
		b.setText("Make subview 1 active (not animated)");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().makeViewActive(SUBVIEW_1_KEY, true, null);
			}
		});

		return b;
	}

	/**
	 * @return a Button which makes view 2 active when clicked (without using animations)
	 */
	private Button createMakeView2ActiveWithoutAnimationButton() {
		final Button b = new Button(this);
		b.setText("Make subview 2 active (not animated)");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().makeViewActive(SUBVIEW_2_KEY, true, null);
			}
		});

		return b;
	}

	/**
	 * @return a Button which makes view 3 active when clicked (without using animations)
	 */
	private Button createMakeView3ActiveWithoutAnimationButton() {
		final Button b = new Button(this);
		b.setText("Make subview 3 active (not animated)");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().makeViewActive(SUBVIEW_3_KEY, true, null);
			}
		});

		return b;
	}
}