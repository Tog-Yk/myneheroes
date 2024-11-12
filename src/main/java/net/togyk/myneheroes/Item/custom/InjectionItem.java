package net.togyk.myneheroes.Item.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.togyk.myneheroes.power.ModPowers;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.power.PowerHolder;

public class InjectionItem extends Item {
    private static Power storedpower = null;
    public InjectionItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (world.isClient) return TypedActionResult.pass(player.getStackInHand(hand));

        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        ItemStack stack = player.getStackInHand(hand);
        PowerHolder playerMixin = (PowerHolder) serverPlayer;

        // If the item contains a stored power, inject it into the player
        if (this.storedpower != null) {
            if (this.storedpower != null && playerMixin.getPower() == null) {
                playerMixin.setPower(storedpower);
                storedpower = null;
                return TypedActionResult.success(stack);
            }
        } else { // If the item is empty, extract a power from the player
            Power activePower = playerMixin.getPower();
            if (activePower != null) {
                Identifier powerId = ModPowers.POWER.getId(activePower);
                storedpower = activePower;
                playerMixin.setPower(null);
                return TypedActionResult.success(stack);
            }
        }

        return TypedActionResult.fail(stack);
    }
}
