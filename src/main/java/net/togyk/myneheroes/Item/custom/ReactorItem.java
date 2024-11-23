package net.togyk.myneheroes.Item.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.togyk.myneheroes.MyneHeroes;

public class ReactorItem extends Item {
    private final int maxPower;
    private static int storedPower;

    public ReactorItem( int maxBattery, Settings settings) {
        super(settings);
        this.maxPower = maxBattery;
    }

    /**
     * Should be called every tick
     * but doesn't
     */
    public void inventoryTick(ItemStack stack, World world, PlayerEntity player, int slot, boolean selected) {
        if (!world.isClient) { // Only update on the server
            if (storedPower < maxPower) {
                storedPower += 1;
            }
        }
        super.inventoryTick(stack, world, player, slot, selected);
    }

    /**
     * Gets the maximum battery value.
     */
    public int getMaxPower() {
        return maxPower;
    }
}