package com.matthewtamlin.vertigo.library_tests;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.matthewtamlin.android_utilities.library.testing.ControlsOverViewTestHarness;
import com.matthewtamlin.vertigo.library.SimpleVertigoCoordinator;
import com.matthewtamlin.vertigo.library.VertigoFrameLayout;
import com.matthewtamlin.vertigo.library.VertigoView;
import com.matthewtamlin.vertigo.library.VertigoView.State;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.matthewtamlin.vertigo.library.VertigoView.State.ACTIVE;
import static com.matthewtamlin.vertigo.library.VertigoView.State.INACTIVE;

@SuppressLint("SetTextI18n") // Not relevant to testing
public class SimpleVertigoCoordinatorTestHarness extends ControlsOverViewTestHarness<SimpleVertigoCoordinator> {
	private static final String VIEW_1_KEY = "view 1";

	private static final String VIEW_2_KEY = "view 2";

	private static final String VIEW_3_KEY = "view 3";

	private SimpleVertigoCoordinator testView;

	private VertigoFrameLayout subview1;

	private VertigoFrameLayout subview2;

	private VertigoFrameLayout subview3;

	@Override
	protected void onCreate(final @Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getControlsContainer().addView(createRegisterView1Button());
		getControlsContainer().addView(createRegisterView2Button());
		getControlsContainer().addView(createRegisterView3Button());
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

			subview1 = createSubview(INACTIVE);
			subview2 = createSubview(INACTIVE);
			subview3 = createSubview(ACTIVE);
			subview1.setBackgroundColor(Color.GREEN);
			subview2.setBackgroundColor(Color.YELLOW);
			subview3.setBackgroundColor(Color.RED);
			testView.addView(subview1);
			testView.addView(subview2);
			testView.addView(subview3);
		}

		return testView;
	}

	private Button createRegisterView1Button() {
		final Button b = new Button(this);
		b.setText("Register view 1");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().registerViewForCoordination(subview1, VIEW_1_KEY);
			}
		});

		return b;
	}

	private Button createRegisterView2Button() {
		final Button b = new Button(this);
		b.setText("Register view 2");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().registerViewForCoordination(subview2, VIEW_2_KEY);
			}
		});

		return b;
	}

	private Button createRegisterView3Button() {
		final Button b = new Button(this);
		b.setText("Register view 3");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getTestView().registerViewForCoordination(subview3, VIEW_3_KEY);
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

	private VertigoFrameLayout createSubview(final State state) {
		final VertigoFrameLayout subview = new VertigoFrameLayout(this);
		subview.setLayoutParams(new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
		subview.onStateChanged(state);

		return subview;
	}
}