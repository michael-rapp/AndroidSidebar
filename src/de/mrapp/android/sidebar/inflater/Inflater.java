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
import android.view.View;
import android.view.ViewGroup;

/**
 * Defines the interface, a class, which should allow to inflate views, must
 * implement.
 * 
 * @author Michael Rapp
 * 
 * @since 1.0.0
 */
public interface Inflater {

	/**
	 * Inflates and returns the view.
	 * 
	 * @param context
	 *            The context, which should be used, as an instance of the class
	 *            {@link Context}. The context may not be null
	 * @param parent
	 *            The parent, that this view will eventually be attached to, as
	 *            an instance of the class {@link ViewGroup}. The parent may not
	 *            be null
	 * @return The view, which has been inflated, as an instance of the class
	 *         {@link View} The view may not be null
	 */
	View inflate(Context context, ViewGroup parent);

}