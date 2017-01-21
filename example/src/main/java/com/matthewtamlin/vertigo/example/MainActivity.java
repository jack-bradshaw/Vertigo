package com.matthewtamlin.vertigo.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.matthewtamlin.vertigo.library.SimpleVertigoCoordinator;
import com.matthewtamlin.vertigo.library.VertigoCoordinator;
import com.matthewtamlin.vertigo.library.VertigoView;

public class MainActivity extends AppCompatActivity {
	private static final String VIEW_1_KEY = "view 1";

	private static final String VIEW_2_KEY = "view 2";

	private static final String VIEW_3_KEY = "view_3";

	private VertigoCoordinator coordinator;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupCoordinationBehaviour();
	}

	private void setupCoordinationBehaviour() {
		coordinator = (SimpleVertigoCoordinator) findViewById(R.id.activity_main_coordinator);

		final VertigoView view1 = (VertigoView) findViewById(R.id.activity_main_view_1);
		final VertigoView view2 = (VertigoView) findViewById(R.id.activity_main_view_2);
		final VertigoView view3 = (VertigoView) findViewById(R.id.activity_main_view_3);

		coordinator.registerViewForCoordination(view1, VIEW_1_KEY);
		coordinator.registerViewForCoordination(view2, VIEW_2_KEY);
		coordinator.registerViewForCoordination(view3, VIEW_3_KEY);
	}

	public void showView1(final View clickedView) {
		coordinator.makeViewActive(VIEW_1_KEY, true, null);
	}

	public void showView2(final View clickedView) {
		coordinator.makeViewActive(VIEW_2_KEY, true, null);
	}

	public void showView3(final View clickedView) {
		coordinator.makeViewActive(VIEW_3_KEY, true, null);
	}
}