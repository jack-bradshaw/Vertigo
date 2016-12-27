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

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Entry point for the manual test harnesses.
 */
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

	/**
	 * @return a Button which launches the VertigoViewCoordinatorTestHarness
	 */
	private Button createLaunchVertigoViewCoordinatorTestHarnessButton() {
		final Button b = new Button(this);
		b.setText("Launch VertigoCoordinator test harness");
		b.setAllCaps(false);

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				startActivity(new Intent(TestLauncher.this,
						SimpleVertigoCoordinatorTestHarness.class));
			}
		});

		return b;
	}
}