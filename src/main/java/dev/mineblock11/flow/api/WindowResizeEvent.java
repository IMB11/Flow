package dev.mineblock11.flow.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

@FunctionalInterface
public interface WindowResizeEvent {
    Event<WindowResizeEvent> EVENT = EventFactory.createArrayBacked(WindowResizeEvent.class,
            (listeners) -> (width, height) -> {
                for (WindowResizeEvent listener : listeners) {
                    listener.invoke(width, height);
                }
    });
    void invoke(int width, int height);
}