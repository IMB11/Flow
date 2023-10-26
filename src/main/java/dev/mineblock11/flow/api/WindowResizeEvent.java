package dev.mineblock11.flow.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * This event is fired when the window is resized.
 */
@FunctionalInterface
public interface WindowResizeEvent {
    /**
     * This event is fired when the window is resized.
     */
    Event<WindowResizeEvent> EVENT = EventFactory.createArrayBacked(WindowResizeEvent.class,
            (listeners) -> (width, height) -> {
                for (WindowResizeEvent listener : listeners) {
                    listener.invoke(width, height);
                }
    });

    /**
     * This method is called when the window is resized.
     * @param width The new width of the window.
     * @param height The new height of the window.
     */
    void invoke(int width, int height);
}