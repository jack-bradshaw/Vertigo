package com.matthewtamlin.vertigo.library_tests;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.matthewtamlin.android_utilities.library.testing.ControlsOverViewTestHarness;
import com.matthewtamlin.vertigo.library.SimpleVertigoCoordinator;
import com.matthewtamlin.vertigo.library.VertigoFrameLayout;
import com.matthewtamlin.vertigo.library.VertigoView.State;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.matthewtamlin.vertigo.library.VertigoView.State.ACTIVE;
import static com.matthewtamlin.vertigo.library.VertigoView.State.INACTIVE;

/**
 * Test harness for the SimpleVertigoCoordinator.
 */
@SuppressLint("SetTextI18n") // Not relevant to testing
public class SimpleVertigoCoordinatorTestHarness extends ControlsOverViewTestHarness<SimpleVertigoCoordinator> {
	/**
	 * Coordinator key for the first subview.
	 */
	private static final String VIEW_1_KEY = "view 1";

	/**
	 * Coordinator key for the second subview.
	 */
	private static final String VIEW_2_KEY = "view 2";

	/**
	 * Coordinator key for the third subview.
	 */
	private static final String VIEW_3_KEY = "view 3";

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

		getControlsContainer().addView(createRegisterViewsButton());
		getControlsContainer().addView(createUnregisterView1Button());
		getControlsContainer().addView(createUnregisterView2Button());
		getControlsContainer().addView(createUnregisterView3Button());
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
			testView.addView(getSubview1());
			testView.addView(getSubview2());
			testView.addView(getSubview3());
		}

		return testView;
	}

	private VertigoFrameLayout getSubview1() {
		if (subview1 == null) {
			subview1 = new VertigoFrameLayout(this);
			subview1.setBackgroundColor(Color.GREEN);
			subview1.onStateChanged(INACTIVE);
		}

		return subview1;
	}

	private VertigoFrameLayout getSubview2() {
		if (subview2 == null) {
			subview2 = new VertigoFrameLayout(this);
			subview2.setBackgroundColor(Color.YELLOW);
			subview1.onStateChanged(INACTIVE);
		}

		return subview2;
	}

	private VertigoFrameLayout getSubview3() {
		if (subview3 == null) {
			subview3 = new VertigoFrameLayout(this);
			subview3.setBackgroundColor(Color.RED);
			subview1.onStateChanged(ACTIVE);
		}

		return subview3;
	}

	private Button createRegisterViewsButton() {
		final Button b = new Button(this);
		b.setText("Add views");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				testView.registerViewForCoordination(subview1, VIEW_1_KEY);
				testView.registerViewForCoordination(subview2, VIEW_2_KEY);
				testView.registerViewForCoordination(subview3, VIEW_3_KEY);
			}
		});

		return b;
	}

	private Button createUnregisterView1Button() {
		final Button b = new Button(this);
		b.setText("Unregister view 1");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().unregisterViewForCoordination(VIEW_1_KEY);
			}
		});

		return b;
	}

	private Button createUnregisterView2Button() {
		final Button b = new Button(this);
		b.setText("Unregister view 2");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().unregisterViewForCoordination(VIEW_2_KEY);
			}
		});

		return b;
	}

	private Button createUnregisterView3Button() {
		final Button b = new Button(this);
		b.setText("Unregister view 3");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().unregisterViewForCoordination(VIEW_3_KEY);
			}
		});

		return b;
	}

	private Button createMakeView1ActiveWithAnimationButton() {
		final Button b = new Button(this);
		b.setText("Make view 1 active (animated)");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().makeViewActive(VIEW_1_KEY, true, null);
			}
		});

		return b;
	}

	private Button createMakeView2ActiveWithAnimationButton() {
		final Button b = new Button(this);
		b.setText("Make view 2 active (animated)");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().makeViewActive(VIEW_2_KEY, true, null);
			}
		});

		return b;
	}

	private Button createMakeView3ActiveWithAnimationButton() {
		final Button b = new Button(this);
		b.setText("Make view 3 active (animated)");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().makeViewActive(VIEW_3_KEY, true, null);
			}
		});

		return b;
	}

	private Button createMakeView1ActiveWithoutAnimationButton() {
		final Button b = new Button(this);
		b.setText("Make view 1 active (not animated)");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().makeViewActive(VIEW_1_KEY, true, null);
			}
		});

		return b;
	}

	private Button createMakeView2ActiveWithoutAnimationButton() {
		final Button b = new Button(this);
		b.setText("Make view 2 active (not animated)");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().makeViewActive(VIEW_2_KEY, true, null);
			}
		});

		return b;
	}

	private Button createMakeView3ActiveWithoutAnimationButton() {
		final Button b = new Button(this);
		b.setText("Make view 3 active (not animated)");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().makeViewActive(VIEW_3_KEY, true, null);
			}
		});

		return b;
	}
}