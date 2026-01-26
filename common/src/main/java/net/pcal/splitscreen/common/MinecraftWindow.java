package net.pcal.splitscreen.common;

/**
 * Encapsulates some messiness in the mixin code for querying and
 * repositioning the minecraft window and the screen it's on.
 */
public interface MinecraftWindow {

    /**
     * @return the bounding rectangle for the window
     */
    Rectangle getWindowBounds();

    /**
     * @return the bounding rectangle for the screen the window is on.
     */
    Rectangle getScreenBounds();


    /**
     * Reposition the window according to the given style and bounds.
     */
    void reposition(WindowStyle style, Rectangle newBounds);

    record Rectangle(int x, int y, int width, int height) {}
}
