package de.mrapp.android.sidebar.savedstate;

import android.os.Parcel;
import android.os.Parcelable;
import android.preference.Preference.BaseSavedState;
import de.mrapp.android.sidebar.DragMode;
import de.mrapp.android.sidebar.Location;
import de.mrapp.android.sidebar.Sidebar;

public class SidebarSavedState extends BaseSavedState {

	private Location location;

	private float animationSpeed;

	private float sidebarWidth;

	private int maxSidebarWidth;

	private float sidebarOffset;

	private int maxSidebarOffset;

	private float scrollRatio;

	private float dragThreshold;

	private float dragSensitivity;

	private DragMode dragModeWhenHidden;

	private DragMode dragModeWhenShown;

	private boolean hideOnBackButton;

	private boolean hideOnContentClick;

	private boolean showOnSidebarClick;

	private int contentOverlayColor;

	private float contentOverlayTransparency;

	private int shadowWidth;

	private int shadowColor;

	private boolean shown;

	/**
	 * Creates a new data structure, which allows to store the internal state of
	 * a {@link Sidebar}. This constructor is called by derived classes when
	 * saving their states.
	 * 
	 * @param superState
	 *            The state of the superclass of this view, as an instance of
	 *            the type {@link Parcelable}
	 */
	public SidebarSavedState(final Parcelable superState) {
		super(superState);
	}

	/**
	 * Creates a new data structure, which allows to store the internal state of
	 * a {@link Sidebar}. This constructor is used when reading from a parcel.
	 * It reads the state of the superclass.
	 * 
	 * @param source
	 *            The parcel to read read from as a instance of the class
	 *            {@link Parcel}
	 */
	public SidebarSavedState(final Parcel source) {
		super(source);
		location = Location.fromValue(source.readInt());
		animationSpeed = source.readFloat();
		sidebarWidth = source.readFloat();
		maxSidebarWidth = source.readInt();
		sidebarOffset = source.readFloat();
		maxSidebarOffset = source.readInt();
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
		shadowWidth = source.readInt();
		shadowColor = source.readInt();
		shown = source.readByte() != 0;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public float getAnimationSpeed() {
		return animationSpeed;
	}

	public void setAnimationSpeed(float animationSpeed) {
		this.animationSpeed = animationSpeed;
	}

	public float getSidebarWidth() {
		return sidebarWidth;
	}

	public void setSidebarWidth(float sidebarWidth) {
		this.sidebarWidth = sidebarWidth;
	}

	public int getMaxSidebarWidth() {
		return maxSidebarWidth;
	}

	public void setMaxSidebarWidth(int maxSidebarWidth) {
		this.maxSidebarWidth = maxSidebarWidth;
	}

	public float getSidebarOffset() {
		return sidebarOffset;
	}

	public void setSidebarOffset(float sidebarOffset) {
		this.sidebarOffset = sidebarOffset;
	}

	public int getMaxSidebarOffset() {
		return maxSidebarOffset;
	}

	public void setMaxSidebarOffset(int maxSidebarOffset) {
		this.maxSidebarOffset = maxSidebarOffset;
	}

	public float getScrollRatio() {
		return scrollRatio;
	}

	public void setScrollRatio(float scrollRatio) {
		this.scrollRatio = scrollRatio;
	}

	public float getDragThreshold() {
		return dragThreshold;
	}

	public void setDragThreshold(float dragThreshold) {
		this.dragThreshold = dragThreshold;
	}

	public float getDragSensitivity() {
		return dragSensitivity;
	}

	public void setDragSensitivity(float dragSensitivity) {
		this.dragSensitivity = dragSensitivity;
	}

	public DragMode getDragModeWhenHidden() {
		return dragModeWhenHidden;
	}

	public void setDragModeWhenHidden(DragMode dragModeWhenHidden) {
		this.dragModeWhenHidden = dragModeWhenHidden;
	}

	public DragMode getDragModeWhenShown() {
		return dragModeWhenShown;
	}

	public void setDragModeWhenShown(DragMode dragModeWhenShown) {
		this.dragModeWhenShown = dragModeWhenShown;
	}

	public boolean isHideOnBackButton() {
		return hideOnBackButton;
	}

	public void setHideOnBackButton(boolean hideOnBackButton) {
		this.hideOnBackButton = hideOnBackButton;
	}

	public boolean isHideOnContentClick() {
		return hideOnContentClick;
	}

	public void setHideOnContentClick(boolean hideOnContentClick) {
		this.hideOnContentClick = hideOnContentClick;
	}

	public boolean isShowOnSidebarClick() {
		return showOnSidebarClick;
	}

	public void setShowOnSidebarClick(boolean showOnSidebarClick) {
		this.showOnSidebarClick = showOnSidebarClick;
	}

	public int getContentOverlayColor() {
		return contentOverlayColor;
	}

	public void setContentOverlayColor(int contentOverlayColor) {
		this.contentOverlayColor = contentOverlayColor;
	}

	public float getContentOverlayTransparency() {
		return contentOverlayTransparency;
	}

	public void setContentOverlayTransparency(float contentOverlayTransparency) {
		this.contentOverlayTransparency = contentOverlayTransparency;
	}

	public int getShadowWidth() {
		return shadowWidth;
	}

	public void setShadowWidth(int shadowWidth) {
		this.shadowWidth = shadowWidth;
	}

	public int getShadowColor() {
		return shadowColor;
	}

	public void setShadowColor(int shadowColor) {
		this.shadowColor = shadowColor;
	}

	public boolean isShown() {
		return shown;
	}

	public void setShown(boolean shown) {
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
		destination.writeInt(shadowWidth);
		destination.writeInt(shadowColor);
		destination.writeByte((byte) (shown ? 1 : 0));
	}

}