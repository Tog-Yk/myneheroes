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

    public ReactorItem(int maxBattery, Settings settings) {
        super(settings);
        this.maxPower = maxBattery;
    }

    /**
     * Should be called every tick
     * but doesn't
     */
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient) { // Only update on the server
            int storedPower = this.getStoredPowerOrDefault(stack,0);
            if (maxPower - storedPower >= 1) {
                setStoredPower(stack, storedPower + 1);
            }
        }
        super.inventoryTick(stack, world, entity ,slot, selected);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        int storedPower = this.getStoredPowerOrDefault(stack, 0);
        tooltip.add(Text.literal("Power: " + storedPower + "/" + maxPower).formatted(Formatting.YELLOW));
        super.appendTooltip(stack, context, tooltip, type);
    }
    /**
     * Gets the maximum battery value.
     */
    public int getMaxPower() {
        return this.maxPower;
    }
    /**
     * Gets the stored battery value.
     */
    public int getStoredPowerOrDefault(ItemStack stack, int fallback) {
        NbtCompound nbt = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).getNbt();
        if (nbt.contains(MyneHeroes.MOD_ID, NbtElement.COMPOUND_TYPE)) {
            var modNbt = nbt.getCompound(MyneHeroes.MOD_ID);
            return modNbt.getInt("reactor_battery");
        }
        return fallback;
    }

    public void setStoredPower(ItemStack stack, int amount) {
        NbtCompound nbt = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).getNbt();
        var modidData = new NbtCompound();
        modidData.putInt("reactor_battery", amount);
        nbt.put(MyneHeroes.MOD_ID, modidData);
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
    }
}