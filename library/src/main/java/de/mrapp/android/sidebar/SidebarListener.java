/*
 * AndroidSidebar Copyright 2014 - 2015 Michael Rapp
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package de.mrapp.android.sidebar;

/**
 * Defines the interface, a class, which should be notified when a sidebar becomes shown or hidden,
 * must implement.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public interface SidebarListener {

    /**
     * The method, which is invoked, when the observed sidebar has been shown.
     *
     * @param sidebar
     *         The observed sidebar as an instance of the class {@link Sidebar}. The sidebar may not
     *         be null
     */
    void onSidebarShown(Sidebar sidebar);

    /**
     * The method, which is invoked, when the observed sidebar has been hidden.
     *
     * @param sidebar
     *         The observed sidebar as an instance of the class {@link Sidebar}. The sidebar may not
     *         be null
     */
    void onSidebarHidden(Sidebar sidebar);

}