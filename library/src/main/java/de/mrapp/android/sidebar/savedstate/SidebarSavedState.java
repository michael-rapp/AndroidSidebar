/*
 * AndroidSidebar Copyright 2014 - 2016 Michael Rapp
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
package de.mrapp.android.sidebar.savedstate;

import android.os.Parcel;
import android.os.Parcelable;
import android.preference.Preference.BaseSavedState;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import de.mrapp.android.sidebar.ContentMode;
import de.mrapp.android.sidebar.DragMode;
import de.mrapp.android.sidebar.Location;
import de.mrapp.android.sidebar.Sidebar;

/**
 * A data structure, which allows to save the internal state of a {@link Sidebar}.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class SidebarSavedState extends BaseSavedState {

    /**
     * A creator, which allows to create instances of the class {@link SidebarSavedState} from
     * parcels.
     */
    public static final Parcelable.Creator<SidebarSavedState> CREATOR =
            new Parcelable.Creator<SidebarSavedState>() {

                @Override
                public SidebarSavedState createFromParcel(final Parcel in) {
                    return new SidebarSavedState(in);
                }

                @Override
                public SidebarSavedState[] newArray(final int size) {
                    return new SidebarSavedState[size];
                }

            };

    /**
     * The saved value of the attribute "location".
     */
    private Location location;

    /**
     * The saved value of the attribute "animationSpeed".
     */
    private float animationSpeed;

    /**
     * The saved value of the attribute "sidebarWidth".
     */
    private float sidebarWidth;

    /**
     * The saved value of the attribute "maxSidebarWidth".
     */
    private int maxSidebarWidth;

    /**
     * The saved value of the attribute "sidebarOffset".
     */
    private float sidebarOffset;

    /**
     * The saved value of the attribute "maxSidebarOffset".
     */
    private int maxSidebarOffset;

    /**
     * The saved value of the attribute "contentMode".
     */
    private ContentMode contentMode;

    /**
     * The saved value of the attribute "scrollRatio".
     */
    private float scrollRatio;

    /**
     * The saved value of the attribute "dragThreshold".
     */
    private float dragThreshold;

    /**
     * The saved value of the attribute "dragSensitivity".
     */
    private float dragSensitivity;

    /**
     * The saved value of the attribute "dragModeWhenHidden".
     */
    private DragMode dragModeWhenHidden;

    /**
     * The saved value of the attribute "dragModeWhenShown".
     */
    private DragMode dragModeWhenShown;

    /**
     * The saved value of the attribute "hideOnBackButton".
     */
    private boolean hideOnBackButton;

    /**
     * The saved value of the attribute "hideOnContentClick".
     */
    private boolean hideOnContentClick;

    /**
     * The saved value of the attribute "showOnSidebarClick".
     */
    private boolean showOnSidebarClick;

    /**
     * The saved value of the attribute "contentOverlayColor".
     */
    private int contentOverlayColor;

    /**
     * The saved value of the attribute "contentOverlayTransparency".
     */
    private float contentOverlayTransparency;

    /**
     * The saved value of the attribute "sidebarElevation".
     */
    private int sidebarElevation;

    /**
     * The saved value of the attribute "shown".
     */
    private boolean shown;

    /**
     * Creates a new data structure, which allows to store the internal state of a {@link Sidebar}.
     * This constructor is called by derived classes when saving their states.
     *
     * @param superState
     *         The state of the superclass of this view, as an instance of the type {@link
     *         Parcelable}
     */
    public SidebarSavedState(@Nullable final Parcelable superState) {
        super(superState);
    }

    /**
     * Creates a new data structure, which allows to store the internal state of a {@link Sidebar}.
     * This constructor is used when reading from a parcel. It reads the state of the superclass.
     *
     * @param source
     *         The parcel to read read from as a instance of the class {@link Parcel}. The parcel
     *         may not be null
     */
    public SidebarSavedState(@NonNull final Parcel source) {
        super(source);
        location = Location.fromValue(source.readInt());
        animationSpeed = source.readFloat();
        sidebarWidth = source.readFloat();
        maxSidebarWidth = source.readInt();
        sidebarOffset = source.readFloat();
        maxSidebarOffset = source.readInt();
        contentMode = ContentMode.fromValue(source.readInt());
        scrollRatio = source.readFloat();
        dragThreshold = source.readFloat();
        dragSensitivity = source.readFloat();
        dragModeWhenHidden = DragMode.fromValue(source.readInt());
        dragModeWhenShown = DragMode.fromValue(source.readInt());
        hideOnBackButton = source.readByte() != 0;
        hideOnContentClick = source.readByte() != 0;
        showOnSidebarClick = source.readByte() != 0;
        contentOverlayColor = source.readInt();
        contentOverlayTransparency = source.readFloat();
        sidebarElevation = source.readInt();
        shown = source.readByte() != 0;
    }

    /**
     * Returns the saved value of the attribute "location".
     *
     * @return The saved value of the attribute "location" as a value of the enum {@link Location}
     */
    public final Location getLocation() {
        return location;
    }

    /**
     * Sets the saved value of the attribute "location".
     *
     * @param location
     *         The saved value of the attribute "location", which should be set, as a value of the
     *         enum {@link Location}
     */
    public final void setLocation(final Location location) {
        this.location = location;
    }

    /**
     * Returns the saved value of the attribute "animationSpeed".
     *
     * @return The saved value of the attribute "animationSpeed" as a {@link Float} value
     */
    public final float getAnimationSpeed() {
        return animationSpeed;
    }

    /**
     * Sets the saved value of the attribute "animationSpeed".
     *
     * @param animationSpeed
     *         The saved value of the attribute "animationSpeed", which should be set, as a {@link
     *         Float} value
     */
    public final void setAnimationSpeed(final float animationSpeed) {
        this.animationSpeed = animationSpeed;
    }

    /**
     * Returns the saved value of the attribute "sidebarWidth".
     *
     * @return The saved value of the attribute "sidebarWidth" as a {@link Float} value
     */
    public final float getSidebarWidth() {
        return sidebarWidth;
    }

    /**
     * Sets the saved value of the attribute "sidebarWidth".
     *
     * @param sidebarWidth
     *         The saved value of the attribute "sidebarWidth", which should be set, as a {@link
     *         Float} value
     */
    public final void setSidebarWidth(final float sidebarWidth) {
        this.sidebarWidth = sidebarWidth;
    }

    /**
     * Returns the saved value of the attribute "maxSidebarWidth".
     *
     * @return The saved value of the attribute "maxSidebarWidth" as an {@link Integer} value
     */
    public final int getMaxSidebarWidth() {
        return maxSidebarWidth;
    }

    /**
     * Sets the saved value of the attribute "maxSidebarWidth".
     *
     * @param maxSidebarWidth
     *         The saved value of the attribute "maxSidebarWidth", which should be set, as an {@link
     *         Integer} value
     */
    public final void setMaxSidebarWidth(final int maxSidebarWidth) {
        this.maxSidebarWidth = maxSidebarWidth;
    }

    /**
     * Returns the saved value of the attribute "sidebarOffset".
     *
     * @return The saved value of the attribute "sidebarOffset" as a {@link Float} value
     */
    public final float getSidebarOffset() {
        return sidebarOffset;
    }

    /**
     * Sets the saved value of the attribute "sidebarOffset".
     *
     * @param sidebarOffset
     *         The saved value of the attribute "sidebarOffset", which should be set, as a {@link
     *         Float} value
     */
    public final void setSidebarOffset(final float sidebarOffset) {
        this.sidebarOffset = sidebarOffset;
    }

    /**
     * Returns the saved value of the attribute "maxSidebarOffset".
     *
     * @return The saved value of the attribute "maxSidebarOffset" as an {@link Integer} value
     */
    public final int getMaxSidebarOffset() {
        return maxSidebarOffset;
    }

    /**
     * Sets the saved value of the attribute "maxSidebarOffset".
     *
     * @param maxSidebarOffset
     *         The saved value of the attribute "maxSidebarOffset", which should be set, as an
     *         {@link Integer} value
     */
    public final void setMaxSidebarOffset(final int maxSidebarOffset) {
        this.maxSidebarOffset = maxSidebarOffset;
    }

    /**
     * Returns the saved value of the attribute "contentMode".
     *
     * @return The saved value of the attribute "contentMode" as a value of the enum {@link
     * ContentMode}
     */
    public final ContentMode getContentMode() {
        return contentMode;
    }

    /**
     * Sets the saved value of the attribute "contentMode".
     *
     * @param contentMode
     *         The saved value of the attribute "contentMode", which should be set, as a value of
     *         the enum {@link ContentMode}
     */
    public final void setContentMode(final ContentMode contentMode) {
        this.contentMode = contentMode;
    }

    /**
     * Returns the saved value of the attribute "scrollRatio".
     *
     * @return The saved value of the attribute "scrollRatio" as a {@link Float} value
     */
    public final float getScrollRatio() {
        return scrollRatio;
    }

    /**
     * Sets the saved value of the attribute "scrollRatio".
     *
     * @param scrollRatio
     *         The saved value of the attribute "scrollRatio", which should be set, as a {@link
     *         Float} value
     */
    public final void setScrollRatio(final float scrollRatio) {
        this.scrollRatio = scrollRatio;
    }

    /**
     * Returns the saved value of the attribute "dragThreshold".
     *
     * @return The saved value of the attribute "dragThreshold" as a {@link Float} value
     */
    public final float getDragThreshold() {
        return dragThreshold;
    }

    /**
     * Sets the saved value of the attribute "dragThreshold".
     *
     * @param dragThreshold
     *         The saved value of the attribute "dragThreshold", which should be set, as a {@link
     *         Float} value
     */
    public final void setDragThreshold(final float dragThreshold) {
        this.dragThreshold = dragThreshold;
    }

    /**
     * Returns the saved value of the attribute "dragSensitivity".
     *
     * @return The saved value of the attribute "dragSensitivity" as a {@link Float} value
     */
    public final float getDragSensitivity() {
        return dragSensitivity;
    }

    /**
     * Sets the saved value of the attribute "dragSensitivity".
     *
     * @param dragSensitivity
     *         The saved value of the attribute "dragSensitivity", which should be set, as a {@link
     *         Float} value
     */
    public final void setDragSensitivity(final float dragSensitivity) {
        this.dragSensitivity = dragSensitivity;
    }

    /**
     * Returns the saved value of the attribute "dragModeWhenHidden".
     *
     * @return The saved value of the attribute "dragModeWhenHidden" as a value of the enum {@link
     * DragMode}
     */
    public final DragMode getDragModeWhenHidden() {
        return dragModeWhenHidden;
    }

    /**
     * Sets the saved value of the attribute "dragModeWhenHidden".
     *
     * @param dragModeWhenHidden
     *         The saved value of the attribute "dragModeWhenHidden", which should be set, as a
     *         value of the enum {@link DragMode}
     */
    public final void setDragModeWhenHidden(final DragMode dragModeWhenHidden) {
        this.dragModeWhenHidden = dragModeWhenHidden;
    }

    /**
     * Returns the saved value of the attribute "dragModeWhenShown".
     *
     * @return The saved value of the attribute "dragModeWhenShown" as a value of the enum {@link
     * DragMode}
     */
    public final DragMode getDragModeWhenShown() {
        return dragModeWhenShown;
    }

    /**
     * Sets the saved value of the attribute "dragModeWhenShown".
     *
     * @param dragModeWhenShown
     *         The saved value of the attribute "dragModeWhenShown", which should be set, as a value
     *         of the enum {@link DragMode}
     */
    public final void setDragModeWhenShown(final DragMode dragModeWhenShown) {
        this.dragModeWhenShown = dragModeWhenShown;
    }

    /**
     * Returns the saved value of the attribute "hideOnBackButton".
     *
     * @return The saved value of the attribute "hideOnBackButton" as a {@link Boolean} value
     */
    public final boolean isHideOnBackButton() {
        return hideOnBackButton;
    }

    /**
     * Sets the saved value of the attribute "hideOnBackButton".
     *
     * @param hideOnBackButton
     *         The saved value of the attribute "hideOnBackButton", which should be set, as a {@link
     *         Boolean} value
     */
    public final void setHideOnBackButton(final boolean hideOnBackButton) {
        this.hideOnBackButton = hideOnBackButton;
    }

    /**
     * Returns the saved value of the attribute "hideOnContentClick".
     *
     * @return The saved value of the attribute "hideOnContentClick" as a {@link Boolean} value
     */
    public final boolean isHideOnContentClick() {
        return hideOnContentClick;
    }

    /**
     * Sets the saved value of the attribute "hideOnContentClick".
     *
     * @param hideOnContentClick
     *         The saved value of the attribute "hideOnContentClick", which should be set, as a
     *         {@link Boolean} value
     */
    public final void setHideOnContentClick(final boolean hideOnContentClick) {
        this.hideOnContentClick = hideOnContentClick;
    }

    /**
     * Returns the saved value of the attribute "showOnSidebarClick".
     *
     * @return The saved value of the attribute "showOnSidebarClick" as a {@link Boolean} value
     */
    public final boolean isShowOnSidebarClick() {
        return showOnSidebarClick;
    }

    /**
     * Sets the saved value of the attribute "showOnSidebarClick".
     *
     * @param showOnSidebarClick
     *         The saved value of the attribute "showOnSidebarClick", which should be set, as a
     *         {@link Boolean} value
     */
    public final void setShowOnSidebarClick(final boolean showOnSidebarClick) {
        this.showOnSidebarClick = showOnSidebarClick;
    }

    /**
     * Returns the saved value of the attribute "contentOverlayColor".
     *
     * @return The saved value of the attribute "contentOverlayColor" as an {@link Integer} value
     */
    public final int getContentOverlayColor() {
        return contentOverlayColor;
    }

    /**
     * Sets the saved value of the attribute "contentOverlayColor".
     *
     * @param contentOverlayColor
     *         The saved value of the attribute "contentOverlayColor", which should be set, as an
     *         {@link Integer} value
     */
    public final void setContentOverlayColor(final int contentOverlayColor) {
        this.contentOverlayColor = contentOverlayColor;
    }

    /**
     * Returns the saved value of the attribute "contentOverlayTransparency".
     *
     * @return The saved value of the attribute "contentOverlayTransparency" as a {@link Float}
     * value
     */
    public final float getContentOverlayTransparency() {
        return contentOverlayTransparency;
    }

    /**
     * Sets the saved value of the attribute "contentOverlayTransparency".
     *
     * @param contentOverlayTransparency
     *         The saved value of the attribute "contentOverlayTransparency", which should be set,
     *         as a {@link Float} value
     */
    public final void setContentOverlayTransparency(final float contentOverlayTransparency) {
        this.contentOverlayTransparency = contentOverlayTransparency;
    }

    /**
     * Returns the saved value of the attribute "sidebarElevation".
     *
     * @return The saved value of the attribute "sidebarElevation" as an {@link Integer} value
     */
    public final int getSidebarElevation() {
        return sidebarElevation;
    }

    /**
     * Sets the saved value of the attribute "sidebarElevation".
     *
     * @param sidebarElevation
     *         The saved value of the attribute "sidebarElevation", which should be set, as an
     *         {@link Integer} value
     */
    public final void setSidebarElevation(final int sidebarElevation) {
        this.sidebarElevation = sidebarElevation;
    }

    /**
     * Returns the saved value of the attribute "shown".
     *
     * @return The saved value of the attribute "shown" as a {@link Boolean} value
     */
    public final boolean isShown() {
        return shown;
    }

    /**
     * Sets the saved value of the attribute "shown".
     *
     * @param shown
     *         The saved value of the attribute "shown", which should be set, as a {@link Boolean}
     *         value
     */
    public final void setShown(final boolean shown) {
        this.shown = shown;
    }

    @Override
    public final void writeToParcel(final Parcel destination, final int flags) {
        super.writeToParcel(destination, flags);
        destination.writeInt(location.getValue());
        destination.writeFloat(animationSpeed);
        destination.writeFloat(sidebarWidth);
        destination.writeInt(maxSidebarWidth);
        destination.writeFloat(sidebarOffset);
        destination.writeInt(maxSidebarOffset);
        destination.writeInt(contentMode.getValue());
        destination.writeFloat(scrollRatio);
        destination.writeFloat(dragThreshold);
        destination.writeFloat(dragSensitivity);
        destination.writeInt(dragModeWhenHidden.getValue());
        destination.writeInt(dragModeWhenShown.getValue());
        destination.writeByte((byte) (hideOnBackButton ? 1 : 0));
        destination.writeByte((byte) (hideOnContentClick ? 1 : 0));
        destination.writeByte((byte) (showOnSidebarClick ? 1 : 0));
        destination.writeInt(contentOverlayColor);
        destination.writeFloat(contentOverlayTransparency);
        destination.writeInt(sidebarElevation);
        destination.writeByte((byte) (shown ? 1 : 0));
    }

}