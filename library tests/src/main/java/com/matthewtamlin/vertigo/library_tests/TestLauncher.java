package com.matthewtamlin.vertigo.library_tests;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

@SuppressLint("SetTextI18n") // Not relevant to testing
public class TestLauncher extends AppCompatActivity {
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final LinearLayout rootView = new LinearLayout(this);
		rootView.setOrientation(LinearLayout.VERTICAL);
		setContentView(rootView, new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));

		rootView.addView(createLaunchVertigoViewCoordinatorTestHarnessButton());
	}

	private Button createLaunchVertigoViewCoordinatorTestHarnessButton() {
		final Button b = new Button(this);
		b.setText("Launch VertigoViewCoordinator test harness");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				startActivity(new Intent(TestLauncher.this,
						FrameVertigoViewCoordinatorTestHarness.class));
			}
		});

		return b;
	}
}
