package net.togyk.myneheroes.Item.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public interface StationaryItem {
    ActionResult interactEntity(PlayerEntity player, Hand hand);
}
