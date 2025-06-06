package net.togyk.myneheroes.Item.custom;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.util.ModTags;

import java.util.List;

public class ReactorItem extends Item {
    private final int maxPower;
    private final int maxFuel;
    private final int defaultFuel;

    public ReactorItem(int maxBattery, int maxFuel, int defaultFuel, Settings settings) {
        super(settings);
        this.maxPower = maxBattery;
        this.maxFuel = maxFuel;
        this.defaultFuel = defaultFuel;
    }

    /**
     * Should be called every tick
     * but doesn't
     */
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient) { // Only update on the server
            int storedFuel = getStoredFuelOrDefault(stack);
            int storedPower = this.getStoredPowerOrDefault(stack, 0);
            if (storedFuel >= 1 && maxPower - 5 >= storedPower) {
                setStoredPower(stack, storedPower + 5);
                setStoredFuel(stack, storedFuel-1);
            }
        }
        super.inventoryTick(stack, world, entity ,slot, selected);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (type.isAdvanced()) {
            int storedFuel = this.getStoredFuelOrDefault(stack);
            tooltip.add(Text.literal("Fuel: " + storedFuel + "/" + maxFuel).formatted(Formatting.AQUA));
            int storedPower = this.getStoredPowerOrDefault(stack, 0);
            tooltip.add(Text.literal("Power: " + storedPower + "/" + maxPower).formatted(Formatting.DARK_RED));
        }
        super.appendTooltip(stack, context, tooltip, type);
    }
    public int getMaxPower() {
        return this.maxPower;
    }
    public int getStoredPowerOrDefault(ItemStack stack, int fallback) {
        NbtCompound nbt = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).getNbt();
        if (nbt.contains(MyneHeroes.MOD_ID, NbtElement.COMPOUND_TYPE)) {
            var modNbt = nbt.getCompound(MyneHeroes.MOD_ID);
            return modNbt.getInt("reactor_battery");
        }
        return fallback;
    }

    public void setStoredPower(ItemStack stack, int amount) {
        //making sure that it won't go above the max
        if (amount > maxPower) {
            amount = maxPower;
        }

        NbtCompound nbt = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).getNbt();
        var modidData = new NbtCompound();
        if (nbt.contains(MyneHeroes.MOD_ID)) {
            modidData = nbt.getCompound(MyneHeroes.MOD_ID);
        }
        modidData.putInt("reactor_battery", amount);
        nbt.put(MyneHeroes.MOD_ID, modidData);
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
    }

    public int getMaxFuel() {
        return this.maxFuel;
    }

    public int getStoredFuelOrDefault(ItemStack stack) {
        NbtCompound nbt = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).getNbt();
        if (nbt.contains(MyneHeroes.MOD_ID, NbtElement.COMPOUND_TYPE)) {
            var modNbt = nbt.getCompound(MyneHeroes.MOD_ID);
            return modNbt.getInt("reactor_fuel");
        }
        return defaultFuel;
    }

    public void setStoredFuel(ItemStack stack, int amount) {
        //making sure that it won't go above the max
        if (amount > this.getMaxFuel()) {
            amount = this.getMaxFuel();
        }

        NbtCompound nbt = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).getNbt();
        var modidData = new NbtCompound();
        if (nbt.contains(MyneHeroes.MOD_ID)) {
            modidData = nbt.getCompound(MyneHeroes.MOD_ID);
        }
        modidData.putInt("reactor_fuel", amount);
        nbt.put(MyneHeroes.MOD_ID, modidData);
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType == ClickType.RIGHT && slot.canTakePartial(player)) {
            if (otherStack.isIn(ModTags.Items.REACTOR_FUEL)) {
                int currentFuel = this.getStoredFuelOrDefault(stack);
                if (currentFuel != this.getMaxFuel()) {
                    if (!player.isCreative()) {
                        otherStack.decrement(1);
                    }
                    this.setStoredFuel(stack, currentFuel + 1600);
                    return true;
                }
            }
        }
        return false;
    }
}