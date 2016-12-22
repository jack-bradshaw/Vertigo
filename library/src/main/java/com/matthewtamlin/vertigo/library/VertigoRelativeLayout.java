package com.matthewtamlin.vertigo.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;
import static com.matthewtamlin.vertigo.library.VertigoView.State.ACTIVE;

/**
 * A RelativeLayout which implements the VertigoView interface.
 */
public class VertigoRelativeLayout extends RelativeLayout implements VertigoView {
	/**
	 * The current Vertigo state of this view.
	 */
	private State state = ACTIVE;

	/**
	 * Constructs a new VertigoRelativeLayout.
	 *
	 * @param context
	 * 		the context the view is operating in
	 */
	public VertigoRelativeLayout(final Context context) {
		super(context);
	}

	/**
	 * Constructs a new VertigoRelativeLayout.
	 *
	 * @param context
	 * 		the context the view is operating in
	 * @param attrs
	 * 		configuration attributes, null allowed
	 */
	public VertigoRelativeLayout(final Context context, final AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * Constructs a new VertigoRelativeLayout.
	 *
	 * @param context
	 * 		the context the view is operating in
	 * @param attrs
	 * 		configuration attributes, null allowed
	 * @param defStyleAttr
	 * 		an attribute in the current theme which supplies default attributes, pass 0	to ignore
	 */
	public VertigoRelativeLayout(final Context context, final AttributeSet attrs, final int
			defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	/**
	 * Constructs a new VertigoRelativeLayout.
	 *
	 * @param context
	 * 		the context the view is operating in
	 * @param attrs
	 * 		configuration attributes, null allowed
	 * @param defStyleAttr
	 * 		an attribute in the current theme which supplies default attributes, pass 0	to ignore
	 * @param defStyleRes
	 * 		a resource which supplies default attributes, only used if {@code defStyleAttr}	is 0, pass
	 * 		0 to ignore
	 */
	@RequiresApi(21) // For caller
	@TargetApi(21) // For lint
	public VertigoRelativeLayout(final Context context, final AttributeSet attrs, final int
			defStyleAttr, final int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	public State getCurrentState() {
		return state;
	}

	@Override
	public void onStateChanged(final State state) {
		this.state = checkNotNull(state, "state cannot be null.");
	}
}