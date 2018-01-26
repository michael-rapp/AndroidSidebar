/*
 * Copyright 2014 - 2018 Michael Rapp
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
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