package de.mrapp.android.sidebar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SidebarActivity extends Activity implements SidebarListener {

	private Sidebar sidebar;

	private CharSequence title;

	private CharSequence savedTitle;

	private boolean showBackButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sidebar = new Sidebar(this);
		sidebar.addSidebarListener(this);

		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		addContentView(sidebar, layoutParams);
	}

	public final Sidebar getSidebar() {
		return sidebar;
	}

	public final void setTitleWhenSidebarIsShown(CharSequence title) {
		this.title = title;
	}

	public final void setTitleWhenSidebarIsShown(int resourceId) {
		this.title = getResources().getString(resourceId);
	}

	public final void showBackButtonWhenSidebarIsShown(
			final boolean showBackButton) {
		this.showBackButton = true;
	}

	@Override
	public void onSidebarShown(Sidebar sidebar) {
		if (title != null) {
			savedTitle = getTitle();
			setTitle(title);
		}

		if (showBackButton) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public void onSidebarHidden(Sidebar sidebar) {
		if (savedTitle != null) {
			setTitle(savedTitle);
		}

		if (showBackButton) {
			getActionBar().setDisplayHomeAsUpEnabled(false);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (showBackButton && getSidebar().isSidebarShown()) {
				getSidebar().hideSidebar();
				return true;
			}
		}

		return super.onOptionsItemSelected(item);
	}

}