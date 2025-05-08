package net.togyk.myneheroes.mixin;

import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.custom.AdvancedArmorItem;
import net.togyk.myneheroes.Item.custom.UpgradeItem;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.List;

@Mixin(AnvilScreenHandler.class)
public abstract class CustomAnvilMixin {
    @Shadow @Final public static int INPUT_1_ID;
    @Shadow @Final public static int INPUT_2_ID;

    @Shadow private int repairItemUsage;

    @Shadow @Final private Property levelCost;

    @Inject(method = "updateResult", at = @At("HEAD"), cancellable = true)
    private void onUpdateResult(CallbackInfo ci) {
        Inventory inputSlots = ((ForgingScreenHandlerAccessor) (Object) this).getInput();
        ItemStack leftStack = inputSlots.getStack(INPUT_1_ID);
        ItemStack rightStack = inputSlots.getStack(INPUT_2_ID);

        // Check that both items are present
        if (!leftStack.isEmpty() && !rightStack.isEmpty()) {
            if (leftStack.getItem() instanceof AdvancedArmorItem armor && rightStack.getItem() instanceof UpgradeItem upgradeItem && !upgradeItem.getAbilities().isEmpty()) {
                if (upgradeItem.getEquipmentSlot() == null || upgradeItem.getEquipmentSlot() == armor.getType()) {
                    // Create a copy to work with the result item
                    ItemStack resultStack = leftStack.copy();

                    List<Ability> abilities = upgradeItem.getAbilities();
                    List<Ability> armorAbilities = armor.getAbilities(leftStack);
                    List<Identifier> armorAbilitiesIds = armorAbilities.stream().map(Ability::getId).toList();

                    MyneHeroes.LOGGER.info(armorAbilities.toString());
                    MyneHeroes.LOGGER.info(armorAbilitiesIds.toString());


                    for (Ability ability : abilities) {
                        MyneHeroes.LOGGER.info(String.valueOf(armorAbilitiesIds.contains(ability.getId())));
                        if (!armorAbilitiesIds.contains(ability.getId())) {
                            armor.saveAbility(resultStack, ability);
                        }
                    }

                    // Set the output to your new custom item
                    CraftingResultInventory resultInventory = ((ForgingScreenHandlerAccessor) (Object) this).getOutput();
                    resultInventory.setStack(0, resultStack);

                    levelCost.set(upgradeItem.getLevelCost());

                    repairItemUsage = 1;

                    resultInventory.markDirty();
                }
                ci.cancel(); // prevent further vanilla processing
            }
        }
    }
}
