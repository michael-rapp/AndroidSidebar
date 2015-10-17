/*
 * AndroidSidebar Copyright 2014 Michael Rapp
 *
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Lesser General Public License as published 
 * by the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/>. 
 */
package de.mrapp.android.sidebar.inflater;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import de.mrapp.android.sidebar.R;

/**
 * Tests the functionality of the class {@link InflaterFactory}.
 * 
 * @author Michael Rapp
 */
public class InflaterFactoryTest extends InstrumentationTestCase {

	/**
	 * Tests the functionality of the createInflater-method, if it is used to
	 * create an inflater, which allows to inflate view, which are present as an
	 * instance of the class {@link View}.
	 */
	public final void testCreateViewInflaterByViewInstance() {
		Context context = getInstrumentation().getContext();
		View view = new View(context);
		ViewGroup viewGroup = new LinearLayout(context);
		Inflater inflater = InflaterFactory.createInflater(view);
		View inflatedView = inflater.inflate(context, viewGroup, false);
		assertEquals(view, inflatedView);
	}

	/**
	 * Tests the functionality of the createInflater-method, if it is used to
	 * create an inflater, which allows to inflate views, which may be
	 * referenced by a resource id.
	 */
	public final void testCreateViewInflaterByViewId() {
		int viewId = R.layout.view;
		ViewGroup viewGroup = new LinearLayout(getInstrumentation()
				.getContext());
		Inflater inflater = InflaterFactory.createInflater(viewId);
		View inflatedView = inflater.inflate(getInstrumentation().getContext(),
				viewGroup, false);
		assertNotNull(inflatedView);
	}

}