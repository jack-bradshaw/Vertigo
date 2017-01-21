/*
 * Copyright 2017 Matthew Tamlin
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

package com.matthewtamlin.vertigo.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.matthewtamlin.vertigo.library.SimpleVertigoCoordinator;
import com.matthewtamlin.vertigo.library.VertigoCoordinator;
import com.matthewtamlin.vertigo.library.VertigoView;

/**
 * Displays three views as well as buttons for controlling the view positions.
 */
public class MainActivity extends AppCompatActivity {
	/**
	 * Key for referring to view 1 in the coordinator.
	 */
	private static final String VIEW_1_KEY = "view 1";

	/**
	 * Key for referring to view 2 in the coordinator.
	 */
	private static final String VIEW_2_KEY = "view 2";

	/**
	 * Key for referring to view 3 in the coordinator.
	 */
	private static final String VIEW_3_KEY = "view_3";

	/**
	 * Coordinates the views by sliding them up and down.
	 */
	private VertigoCoordinator coordinator;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupCoordinationBehaviour();
	}

	/**
	 * Sets up the three views in the coordinator.
	 */
	private void setupCoordinationBehaviour() {
		coordinator = (SimpleVertigoCoordinator) findViewById(R.id.activity_main_coordinator);

		final VertigoView view1 = (VertigoView) findViewById(R.id.activity_main_view_1);
		final VertigoView view2 = (VertigoView) findViewById(R.id.activity_main_view_2);
		final VertigoView view3 = (VertigoView) findViewById(R.id.activity_main_view_3);

		view1.onStateChanged(VertigoView.State.INACTIVE);
		view2.onStateChanged(VertigoView.State.INACTIVE);
		view3.onStateChanged(VertigoView.State.ACTIVE);

		coordinator.registerViewForCoordination(view1, VIEW_1_KEY);
		coordinator.registerViewForCoordination(view2, VIEW_2_KEY);
		coordinator.registerViewForCoordination(view3, VIEW_3_KEY);
	}

	/**
	 * On-click listener method for button 1. When called, view 1 is made active in the
	 * coordinator.
	 *
	 * @param clickedView
	 * 		the clicked view
	 */
	public void showView1(final View clickedView) {
		coordinator.makeViewActive(VIEW_1_KEY, true, null);
	}

	/**
	 * On-click listener method for button 2. When called, view 2 is made active in the
	 * coordinator.
	 *
	 * @param clickedView
	 * 		the clicked view
	 */
	public void showView2(final View clickedView) {
		coordinator.makeViewActive(VIEW_2_KEY, true, null);
	}

	/**
	 * On-click listener method for button 3. When called, view 3 is made active in the
	 * coordinator.
	 *
	 * @param clickedView
	 * 		the clicked view
	 */
	public void showView3(final View clickedView) {
		coordinator.makeViewActive(VIEW_3_KEY, true, null);
	}
}