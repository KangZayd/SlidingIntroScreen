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

package com.matthewtamlin.sliding_intro_screen_manual_testing;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.matthewtamlin.sliding_intro_screen_library.core.LockableViewPager;

import java.util.ArrayList;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests the ability to lock the page to either touch, commands or both. The tests also ensure that
 * locking to touch does not prevent elements in the current page from receiving touch events.
 */
public class TestPageLock extends ThreePageTestBase {
	/**
	 * The index of the page to navigate to when the "go to specific page" button is clicked.
	 */
	private static final int SPECIFIC_PAGE = 1;

	/**
	 * Used to identify this class during testing.
	 */
	@SuppressWarnings("unused")
	private static final String TAG = "[TestPageLock]";

	/**
	 * The number of pages to display, must match the number of pages in the superclass to that the
	 * superclass background manager can be used.
	 */
	private static final int NUMBER_OF_PAGES = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create a layout to display the control buttons over the ViewPager
		final LinearLayout controlButtonHolder = new LinearLayout(this);
		controlButtonHolder.setOrientation(LinearLayout.HORIZONTAL);
		getRootView().addView(controlButtonHolder);

		// Create a layout to display buttons for changing the page
		final LinearLayout pageChangeButtonHolder = new LinearLayout(this);
		pageChangeButtonHolder.setOrientation(LinearLayout.VERTICAL);
		controlButtonHolder.addView(pageChangeButtonHolder);

		// Create a layout to display buttons for locking the ViewPager
		final LinearLayout pageLockButtonHolder = new LinearLayout(this);
		pageLockButtonHolder.setOrientation(LinearLayout.VERTICAL);
		controlButtonHolder.addView(pageLockButtonHolder);

		// Create the button for changing the page
		pageChangeButtonHolder.addView(createGoToFirstPageButton());
		pageChangeButtonHolder.addView(createGoToLastPageButton());
		pageChangeButtonHolder.addView(createGoToPreviousPageButton());
		pageChangeButtonHolder.addView(createGoToNextPageButton());
		pageChangeButtonHolder.addView(createGoToSpecificPageButton(SPECIFIC_PAGE));

		// Create the buttons for locking the page
		pageLockButtonHolder.addView(createLockTouchButton());
		pageLockButtonHolder.addView(createLockCommandButton());
		pageLockButtonHolder.addView(createLockAllButton());
		pageLockButtonHolder.addView(createUnlockButton());
	}

	/**
	 * @return creates a Button which navigates to the first page
	 */
	private Button createGoToFirstPageButton() {
		final Button button = new Button(this);
		button.setText("Go to first page");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final int startIndex = getIndexOfCurrentPage();
				goToFirstPage();
				validateCommandPageChange(startIndex, 0);
			}
		});

		return button;
	}

	/**
	 * @return creates a Button which navigates to the last page
	 */
	private Button createGoToLastPageButton() {
		final Button button = new Button(this);
		button.setText("Go to last page");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final int startIndex = getIndexOfCurrentPage();
				goToLastPage();
				validateCommandPageChange(startIndex, numberOfPages() - 1);
			}
		});

		return button;
	}

	/**
	 * @return creates a Button which navigates to the previous page
	 */
	private Button createGoToPreviousPageButton() {
		final Button button = new Button(this);
		button.setText("Go to previous page");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final int startIndex = getIndexOfCurrentPage();
				goToPreviousPage();
				validateCommandPageChange(startIndex, (startIndex == 0) ? 0 : startIndex - 1);
			}
		});

		return button;
	}

	/**
	 * @return creates a Button which navigates to the next page
	 */
	private Button createGoToNextPageButton() {
		final Button button = new Button(this);
		button.setText("Go to next page");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final int startIndex = getIndexOfCurrentPage();
				goToNextPage();
				validateCommandPageChange(startIndex, (startIndex == NUMBER_OF_PAGES - 1) ?
						NUMBER_OF_PAGES - 1 :
						startIndex + 1);
			}
		});

		return button;
	}

	/**
	 * @param pageIndex
	 * 		the index of the page to change to
	 * @return creates a Button which navigates to a specific page
	 */
	private Button createGoToSpecificPageButton(final int pageIndex) {
		final Button button = new Button(this);
		button.setText("Go to page " + (pageIndex + 1));

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final int startIndex = getIndexOfCurrentPage();
				goToPage(pageIndex);
				validateCommandPageChange(startIndex, pageIndex);
			}
		});

		return button;
	}

	/**
	 * @return creates a Button which locks the ViewPager from touch events
	 */
	private Button createLockTouchButton() {
		final Button button = new Button(this);
		button.setText("Lock to touch");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setPagingLockMode(LockableViewPager.LockMode.TOUCH_LOCKED);
			}
		});

		return button;
	}

	/**
	 * @return creates a Button which locks the ViewPager from commands
	 */
	private Button createLockCommandButton() {
		final Button button = new Button(this);
		button.setText("Lock to commands");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setPagingLockMode(LockableViewPager.LockMode.COMMAND_LOCKED);
			}
		});

		return button;
	}

	/**
	 * @return creates a Button which locks the ViewPager form touch events and commands
	 */
	private Button createLockAllButton() {
		final Button button = new Button(this);
		button.setText("Lock all");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setPagingLockMode(LockableViewPager.LockMode.FULLY_LOCKED);
			}
		});

		return button;
	}

	/**
	 * @return creates a Button which unlocks the ViewPager
	 */
	private Button createUnlockButton() {
		final Button button = new Button(this);
		button.setText("Unlock");

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setPagingLockMode(LockableViewPager.LockMode.UNLOCKED);
			}
		});

		return button;
	}

	/**
	 * Checks that the page changed correctly when using commands, taking into account the current
	 * locking conditions.
	 *
	 * @param originalPage
	 * 		the index of the page which was active when the change page request occurred
	 * @param triedChangingTo
	 * 		the index of the page the change attempted to move to
	 */
	private void validateCommandPageChange(final int originalPage, final int triedChangingTo) {
		final boolean didChange = (originalPage != getIndexOfCurrentPage());
		final boolean shouldHaveChanged;

		if (triedChangingTo == originalPage) {
			// The page can never change if the requested page is the current page
			shouldHaveChanged = false;
		} else {
			// The page should only have changed if the locking mode allows it to
			shouldHaveChanged = getPagingLockMode().allowsCommands();
		}

		assertThat("locking conditions passed", didChange, is(shouldHaveChanged));
	}

	/*
	 * This method is overridden to return fragments with an interactive element. This allows
	 * testing to ensure that elements in the page still receive touch events when the page is
	 * touch locked.
	 */
	@Override
	protected Collection<Fragment> generatePages(Bundle savedInstanceState) {
		final Collection<Fragment> fragments = new ArrayList<>();

		for (int i = 0; i < NUMBER_OF_PAGES; i++) {
			fragments.add(new ButtonFragment());
		}

		return fragments;
	}
}