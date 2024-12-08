package net.togyk.myneheroes.Item.custom;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.togyk.myneheroes.MyneHeroes;

import java.util.List;

public class ReactorItem extends Item {
    private final int maxPower;
    private final int maxFuel;

    public ReactorItem(int maxBattery,int maxFuel, Settings settings) {
        super(settings);
        this.maxPower = maxBattery;
        this.maxFuel = maxFuel;
    }

    /**
     * Should be called every tick
     * but doesn't
     */
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient) { // Only update on the server
            int storedFuel = getStoredFuelOrDefault(stack,0);
            int storedPower = this.getStoredPowerOrDefault(stack, 0);
            if (storedFuel >= 1 && maxPower != storedPower) {
                setStoredPower(stack, storedPower + 5);
                setStoredFuel(stack, storedFuel-1);
            }
        }
        super.inventoryTick(stack, world, entity ,slot, selected);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (type.isAdvanced()) {
            int storedFuel = this.getStoredFuelOrDefault(stack, 0);
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
    public int getStoredFuelOrDefault(ItemStack stack, int fallback) {
        NbtCompound nbt = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).getNbt();
        if (nbt.contains(MyneHeroes.MOD_ID, NbtElement.COMPOUND_TYPE)) {
            var modNbt = nbt.getCompound(MyneHeroes.MOD_ID);
            return modNbt.getInt("reactor_fuel");
        }
        return fallback;
    }

    public void setStoredFuel(ItemStack stack, int amount) {
        //making sure that it won't go above the max
        if (amount > maxFuel) {
            amount = maxFuel;
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
}