package com.matthewtamlin.vertigo.library;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.matthewtamlin.android_utilities.library.testing.Tested;
import com.matthewtamlin.java_utilities.checkers.IntChecker;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import timber.log.Timber;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;
import static com.matthewtamlin.vertigo.library.VertigoView.State.ACTIVE;
import static com.matthewtamlin.vertigo.library.VertigoView.State.INACTIVE;

/**
 * An implementation of the VertigoViewCoordinator interface based on a FrameLayout.
 */
@Tested(testMethod = "manual, automated", requiresInstrumentation = true)
public class SimpleVertigoCoordinator extends FrameLayout implements VertigoViewCoordinator {
	/**
	 * The set of all views which are coordinated by this SlidingCoordinator.
	 */
	private final Map<String, VertigoView> allViews = new HashMap<>();

	/**
	 * The set of all views which are currently in the up position.
	 */
	private final Set<VertigoView> viewsInUpPosition = new HashSet<>();

	/**
	 * The number of animations which are currently being performed on views. An atomic integer is
	 * needed to ensure concurrent safety when using animations.
	 */
	private final AtomicInteger currentAnimationCount = new AtomicInteger(0);

	/**
	 * The length of time to use for each view move animation.
	 */
	private int animationDurationMs = 300;

	/**
	 * Constructs a new SlidingCoordinator.
	 *
	 * @param context
	 * 		the context the view is operating in
	 */
	public SimpleVertigoCoordinator(final Context context) {
		super(context);
	}

	/**
	 * Constructs a new SlidingCoordinator.
	 *
	 * @param context
	 * 		the context the view is operating in
	 * @param attrs
	 * 		configuration attributes, null allowed
	 */
	public SimpleVertigoCoordinator(final Context context, final AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * Constructs a new SlidingCoordinator.
	 *
	 * @param context
	 * 		the context the view is operating in
	 * @param attrs
	 * 		configuration attributes, null allowed
	 * @param defStyleAttr
	 * 		an attribute in the current theme which supplies default attributes, pass 0	to ignore
	 */
	public SimpleVertigoCoordinator(final Context context, final AttributeSet attrs, final int
			defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	/**
	 * Constructs a new SlidingCoordinator.
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
	public SimpleVertigoCoordinator(final Context context, final AttributeSet attrs, final int
			defStyleAttr, final int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	public Set<String> getAllKeys() {
		return Collections.unmodifiableSet(allViews.keySet());
	}

	@Override
	public Set<VertigoView> getAllViews() {
		return Collections.unmodifiableSet(new HashSet<>(allViews.values()));
	}

	@Override
	public VertigoView getView(final String key) {
		if (!allViews.containsKey(key)) {
			return null;
		} else {
			return allViews.get(key);
		}
	}

	@Override
	public void registerViewForCoordination(final VertigoView view, final String key) {
		checkNotNull(view, "view cannot be null.");
		checkNotNull(key, "key cannot be null.");

		if (allViews.values().contains(view)) {
			throw new IllegalArgumentException("The supplied view is already registered with a " +
					"SlidingCoordinator.");
		} else if (!(view instanceof View)) {
			throw new IllegalArgumentException("The supplied view is not a subclass of android" +
					".view.View");
		} else {
			allViews.put(key, view);
			viewsInUpPosition.add(view);
		}
	}

	@Override
	public void unregisterViewForCoordination(final String key) {
		checkNotNull(key, "key cannot be null.");

		if (allViews.keySet().contains(key)) {
			viewsInUpPosition.remove(allViews.get(key));
			allViews.remove(key);
		}
	}

	@Override
	public void makeViewActive(final String key, final boolean animate,
			final ActiveViewChangedListener listener) {
		final VertigoView viewToMakeActive = allViews.get(key);

		// It's simpler to use a stub implementation of the listener than deal with null
		final ActiveViewChangedListener listenerToUse = listener == null ? getDefaultListener() :
				listener;

		if (currentAnimationCount.get() != 0) {
			Timber.w("Cannot make view active, operation already in progress.");
		} else if (viewToMakeActive.getCurrentState() == ACTIVE) {
			Timber.w("View is already active.");
		} else {
			if (viewsInUpPosition.contains(viewToMakeActive)) {
				moveOtherViewsDown(viewToMakeActive, animate, listenerToUse);
			} else {
				((View) viewToMakeActive).bringToFront();
				moveViewUpWithoutMovingOtherViews(viewToMakeActive, animate, listenerToUse);
			}

			makeOnlyActiveViewInteractive();
		}
	}

	@Override
	public void setAnimationDurationMs(final int animationDurationMs) {
		this.animationDurationMs = IntChecker.checkGreaterThan(animationDurationMs, 0,
				"animationDurationMs must be greater than zero");
	}

	@Override
	public int getAnimationDurationMs() {
		return animationDurationMs;
	}

	/**
	 * Returns an implementation of the ActiveViewChangedListener interface which does nothing when
	 * its methods are invoked.
	 *
	 * @return the ActiveViewChangedListener
	 */
	private ActiveViewChangedListener getDefaultListener() {
		return new ActiveViewChangedListener() {
			@Override
			public void onActiveViewChanged(final VertigoViewCoordinator coordinator,
					final VertigoView activeView) {
				// Do nothing
			}
		};
	}

	/**
	 * Moves all views down, except for the supplied view and any views which are already down.
	 *
	 * @param viewToKeepUp
	 * 		a view in the up position which should not be moved down with the other views, not null
	 * @param animate
	 * 		whether or not views should be animated when moved
	 * @param listener
	 * 		the listener to call when the operation completes, may be null
	 * @throws IllegalArgumentException
	 * 		if {@code viewToKeepUp} is null
	 */
	private void moveOtherViewsDown(final VertigoView viewToKeepUp, final boolean animate,
			final ActiveViewChangedListener listener) {
		checkNotNull(viewToKeepUp, "viewToKeepUp cannot be null.");

		// Delivers callback when all necessary views have been moved down, but not if no views
		// are moved down
		final Consolidator consolidator = new Consolidator(viewsInUpPosition.size() - 1,
				listener, viewToKeepUp);

		// Create a copy of the set, so that the original set can be modified while iterating
		final Set<VertigoView> viewsToMoveDown = new HashSet<>(viewsInUpPosition);
		viewsToMoveDown.remove(viewToKeepUp);

		for (final VertigoView view : viewsToMoveDown) {
			if (animate) {
				final Animator moveDownAnimator = createMoveViewDownAnimator(view);

				moveDownAnimator.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(final Animator animation) {
						view.onStateChanged(INACTIVE);
						viewsInUpPosition.remove(view);
						currentAnimationCount.decrementAndGet();
						consolidator.notifyMakeViewInactiveComplete();
					}
				});

				currentAnimationCount.incrementAndGet();
				moveDownAnimator.start();
			} else {
				moveViewDownWithoutAnimating(view);
				view.onStateChanged(INACTIVE);
				viewsInUpPosition.remove(view);
				consolidator.notifyMakeViewInactiveComplete();
			}
		}
	}

	/**
	 * Moves the supplied view up but does not change any of the other views.
	 *
	 * @param viewToMoveUp
	 * 		the view to move up, not null
	 * @param animate
	 * 		whether or not views should be animated when moved
	 * @param listener
	 * 		the listener to call when the operation completes, may be null
	 * @throws IllegalArgumentException
	 * 		if {@code viewToMoveUp} is null
	 */
	private void moveViewUpWithoutMovingOtherViews(final VertigoView viewToMoveUp,
			final boolean animate, final ActiveViewChangedListener listener) {
		checkNotNull(viewToMoveUp, "view cannot be null.");

		if (viewsInUpPosition.contains(viewToMoveUp)) {
			throw new IllegalArgumentException("The supplied view is already in the up position.");
		}

		if (animate) {
			final Animator moveUpAnimation = createMoveViewUpAnimation(viewToMoveUp);

			moveUpAnimation.addListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(final Animator animation) {
					viewToMoveUp.onStateChanged(ACTIVE);
					viewsInUpPosition.add(viewToMoveUp);
					currentAnimationCount.decrementAndGet();
					listener.onActiveViewChanged(SimpleVertigoCoordinator.this, viewToMoveUp);
					notifyOtherViewsOnInactive(viewToMoveUp);
				}
			});

			currentAnimationCount.incrementAndGet();
			moveUpAnimation.start();
		} else {
			moveViewUpWithoutAnimating(viewToMoveUp);
			viewToMoveUp.onStateChanged(ACTIVE);
			viewsInUpPosition.add(viewToMoveUp);
			listener.onActiveViewChanged(SimpleVertigoCoordinator.this, viewToMoveUp);
			notifyOtherViewsOnInactive(viewToMoveUp);
		}
	}

	/**
	 * Sets the active view to be clickable and focusable, and sets all other views to be
	 * unclickable and unfocusable.
	 */
	private void makeOnlyActiveViewInteractive() {
		for (final VertigoView view : allViews.values()) {
			((View) view).setClickable(view.getCurrentState() == ACTIVE);
			((View) view).setFocusable(view.getCurrentState() == ACTIVE);
		}
	}

	/**
	 * Creates an animator which moves the supplied view down, but does not start the animation.
	 *
	 * @param view
	 * 		the view to move down, not null
	 * @return the animator
	 * @throws IllegalArgumentException
	 * 		if {@code view} is null
	 */
	private Animator createMoveViewDownAnimator(final VertigoView view) {
		checkNotNull(view, "view cannot be null.");
		final View castView = (View) view;
		return createYSlideAnimation(castView, castView.getHeight(), animationDurationMs);
	}

	/**
	 * Moves the supplied view down without animating. The effect is instantaneous.
	 *
	 * @param view
	 * 		the view to move down, not null
	 * @throws IllegalArgumentException
	 * 		if {@code view} is null
	 */
	private void moveViewDownWithoutAnimating(final VertigoView view) {
		checkNotNull(view, "view cannot be null.");
		final View castView = (View) view;
		castView.setY(castView.getY() + castView.getHeight());
	}

	/**
	 * Creates an animator which moves the supplied view up, but does not start the animation.
	 *
	 * @param view
	 * 		the view to move up, not null
	 * @return the animator
	 * @throws IllegalArgumentException
	 * 		if {@code view} is null
	 */
	private Animator createMoveViewUpAnimation(final VertigoView view) {
		checkNotNull(view, "view cannot be null");
		final View castView = (View) view;
		return createYSlideAnimation(castView, -castView.getHeight(), animationDurationMs);
	}

	/**
	 * Moves the supplied view up without animating. The effect is instantaneous.
	 *
	 * @param view
	 * 		the view to move up, not null
	 * @throws IllegalArgumentException
	 * 		if {@code view} is null
	 */
	private void moveViewUpWithoutAnimating(final VertigoView view) {
		checkNotNull(view, "view cannot be null");
		final View castView = (View) view;
		castView.setY(castView.getY() - castView.getHeight());
	}

	/**
	 * Calls the {@link VertigoView#onStateChanged(VertigoView.State)} method with INACTIVE on every
	 * view except the supplied view.
	 *
	 * @param view
	 * 		the view to not notify, may be null
	 */
	private void notifyOtherViewsOnInactive(final VertigoView view) {
		for (final VertigoView otherView : allViews.values()) {
			if (otherView != view) {
				otherView.onStateChanged(INACTIVE);
			}
		}
	}

	/**
	 * Creates an animator which smoothly moves a activeView in the Y direction..
	 *
	 * @param view
	 * 		the activeView to animate
	 * @param distancePx
	 * 		the distance to move the activeView, positive downwards, measured in pixels
	 * @param animationDurationMs
	 * 		the length of time to use for the animation, measured in milliseconds
	 * @return the animation being used, not yet started
	 */
	private Animator createYSlideAnimation(final View view, final float distancePx,
			final int animationDurationMs) {
		final float startYPosition = view.getY();

		final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
		animator.setDuration(animationDurationMs);

		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(final ValueAnimator valueAnimator) {
				view.setY(startYPosition + (valueAnimator.getAnimatedFraction() * distancePx));
			}
		});

		animator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationCancel(Animator animation) {
				view.setY(startYPosition);
			}
		});

		animator.start();
		return animator;
	}

	private class Consolidator {
		private final int triggerCount;

		private final ActiveViewChangedListener listener;

		private final VertigoView activeView;

		private final AtomicInteger notificationCount = new AtomicInteger(0);

		public Consolidator(final int triggerCount, final ActiveViewChangedListener listener,
				final VertigoView activeView) {
			this.triggerCount = triggerCount;
			this.listener = listener;
			this.activeView = activeView;
		}

		public void notifyMakeViewInactiveComplete() {
			if (notificationCount.incrementAndGet() == triggerCount && listener != null) {
				activeView.onStateChanged(ACTIVE);
				listener.onActiveViewChanged(SimpleVertigoCoordinator.this, activeView);
			}
		}
	}
}