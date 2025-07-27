package net.togyk.myneheroes.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.togyk.myneheroes.util.SimpleEventResult;

public interface MissedInteractionCallback {
    // Create an event that passes the player and the hand they swung
    Event<MissedInteractionCallback> EVENT = EventFactory.createArrayBacked(MissedInteractionCallback.class,
            (listeners) -> (player) -> {
                for (MissedInteractionCallback listener : listeners) {
                    if (listener.onMissedInteraction(player) == SimpleEventResult.SUCCESS) {
                        break;
                    }
                }
                return null;
            }
    );

    SimpleEventResult onMissedInteraction(PlayerEntity player);
}
