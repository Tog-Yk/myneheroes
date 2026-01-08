package net.togyk.myneheroes.Item.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.togyk.myneheroes.component.ModDataComponentTypes;

import java.util.List;

public class DyeableAdvancedArmorWithFaceplateItem extends DyeableAdvancedArmorItem {
    private final int maxOpeningTime;

    public DyeableAdvancedArmorWithFaceplateItem(List<Integer> defaultColors, int openingTime, List<Integer> lightLevels, Text titleText, RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(defaultColors, lightLevels, titleText, material, type, settings);
        this.maxOpeningTime = openingTime;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof LivingEntity living && living.getEquippedStack(EquipmentSlot.HEAD) == stack) {
            boolean isOpening = stack.getOrDefault(ModDataComponentTypes.IS_OPENING, true);
            int openingTime = stack.getOrDefault(ModDataComponentTypes.OPENING_TIME, 0);
            if (isOpening) {
                if (openingTime < this.getMaxOpeningTime()) {
                    stack.set(ModDataComponentTypes.OPENING_TIME, openingTime + 1);
                } else if (openingTime > this.maxOpeningTime) {
                    stack.set(ModDataComponentTypes.OPENING_TIME, this.getMaxOpeningTime());
                }
            } else {
                if (openingTime > 0) {
                    stack.set(ModDataComponentTypes.OPENING_TIME, openingTime - 1);
                } else if (openingTime < 0) {
                    stack.set(ModDataComponentTypes.OPENING_TIME, 0);
                }
            }
        }
    }

    public int getMaxOpeningTime() {
        return this.maxOpeningTime;
    }

    public float getOpenProgress(ItemStack stack) {
        int openingTime = stack.getOrDefault(ModDataComponentTypes.OPENING_TIME, 0);
        return (float) openingTime / this.maxOpeningTime;
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (otherStack.isEmpty() && clickType == ClickType.RIGHT) {
            boolean isOpening = stack.getOrDefault(ModDataComponentTypes.IS_OPENING, true);
            stack.set(ModDataComponentTypes.IS_OPENING, !isOpening);
            return true;
        }
        return super.onClicked(stack, otherStack, slot, clickType, player, cursorStackReference);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        tooltip.add(Text.literal("Faceplate:").formatted(Formatting.GRAY));
        boolean isOpening = stack.getOrDefault(ModDataComponentTypes.IS_OPENING, true);
        if (isOpening) {
            tooltip.add(Text.literal(" Open").formatted(Formatting.BLUE));
        } else {
            tooltip.add(Text.literal(" Closed").formatted(Formatting.BLUE));
        }
    }
}
