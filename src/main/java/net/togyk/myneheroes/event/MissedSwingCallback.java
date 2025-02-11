package net.togyk.myneheroes.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

public interface MissedSwingCallback {
    // Create an event that passes the player and the hand they swung
    Event<MissedSwingCallback> EVENT = EventFactory.createArrayBacked(MissedSwingCallback.class,
            (listeners) -> (player, hand) -> {
                for (MissedSwingCallback listener : listeners) {
                    listener.onMissedSwing(player, hand);
                }
            }
    );

    void onMissedSwing(PlayerEntity player, Hand hand);
}
