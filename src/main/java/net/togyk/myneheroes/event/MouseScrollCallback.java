package net.togyk.myneheroes.event;


import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;

public interface MouseScrollCallback {
    Event<MouseScrollCallback> EVENT =
            EventFactory.createArrayBacked(MouseScrollCallback.class,
                    (listeners) -> (client, mouseX, mouseY, hScroll, vScroll) -> {
                        for (MouseScrollCallback listener : listeners) {
                            if (listener.onMouseScroll(client, mouseX, mouseY, hScroll, vScroll)) {
                                return true;
                            }
                        }
                        return false;
                    });

    //returns if the original code should run
    boolean onMouseScroll(MinecraftClient client, double mouseX, double mouseY, double hScroll, double vScroll);
}
