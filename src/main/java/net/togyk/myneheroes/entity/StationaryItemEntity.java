package net.togyk.myneheroes.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class StationaryItemEntity extends ItemEntity {
    public StationaryItemEntity(EntityType<? extends ItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        //Method Is not called properly
        player.sendMessage(Text.literal("You dare touch me?!"));
        return super.interact(player, hand);
    }
}
