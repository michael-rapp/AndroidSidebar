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

import android.view.View;

/**
 * An utility class, which offers factory methods, which allow to initialize
 * instances of the type {@link Inflater}.
 * 
 * @author Michael Rapp
 * 
 * @since 1.0.0
 */
public final class InflaterFactory {

	/**
	 * Creates a new utility class, which offers factory methods, which allow to
	 * initialize instances of the type {@link Inflater}.
	 */
	private InflaterFactory() {

	}

	/**
	 * Creates and returns an inflater, which allows to inflate view, which are
	 * present as an instance of the class {@link View}.
	 * 
	 * @param view
	 *            The view, which should be inflated, as an instance of the
	 *            class {@link View}. The view may not be null
	 * @return The inflater, which has been created, as an instance of the type
	 *         {@link Inflater}. The inflater may not be null
	 */
	public static Inflater createInflater(final View view) {
		return new InstanceInflater(view);
	}

	/**
	 * Creates and returns an inflater, which allows to inflate views, which may
	 * be referenced by a specific resource id.
	 * 
	 * @param viewId
	 *            The resource id of the view, which should be inflated by the
	 *            inflater, as an {@link Integer} value. The id must correspond
	 *            to a valid view resource
	 * @return The inflater, which has been created, as an instance of the type
	 *         {@link Inflater}. The inflater may not be null
	 */
	public static Inflater createInflater(final int viewId) {
		return new IdInflater(viewId);
	}

}