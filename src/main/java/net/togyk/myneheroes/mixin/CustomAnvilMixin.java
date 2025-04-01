package net.togyk.myneheroes.mixin;

import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.Property;
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

@Mixin(AnvilScreenHandler.class)
public abstract class CustomAnvilMixin {
    @Shadow @Final public static int INPUT_1_ID;
    @Shadow @Final public static int INPUT_2_ID;

    @Shadow @Final private Property levelCost;

    @Inject(method = "updateResult", at = @At("HEAD"), cancellable = true)
    private void onUpdateResult(CallbackInfo ci) {
        Inventory inputSlots = ((ForgingScreenHandlerAccessor) (Object) this).getInput();
        ItemStack leftStack = inputSlots.getStack(INPUT_1_ID);
        ItemStack rightStack = inputSlots.getStack(INPUT_2_ID);

        // Check that both items are present
        if (!leftStack.isEmpty() && !rightStack.isEmpty()) {
            if (leftStack.getItem() instanceof AdvancedArmorItem armor && rightStack.getItem() instanceof UpgradeItem upgradeItem && upgradeItem.getAbility() != null) {
                MyneHeroes.LOGGER.info("heelpp");
                if (upgradeItem.getEquipmentSlot() == null || upgradeItem.getEquipmentSlot() == armor.getType()) {
                    MyneHeroes.LOGGER.info("cry about it");
                    // Create a copy to work with the result item
                    ItemStack resultStack = leftStack.copy();

                    Ability ability = upgradeItem.getAbility();


                    MyneHeroes.LOGGER.info(armor.getAbilities(leftStack).toString());
                    MyneHeroes.LOGGER.info(ability.getId().toString());

                    if (!armor.getAbilities(leftStack).stream().map(Ability::getId).toList().contains(ability.getId())) {
                        MyneHeroes.LOGGER.info("seek therapy");
                        armor.saveAbility(resultStack, ability);
                        // Set the output to your new custom item
                        CraftingResultInventory resultInventory = ((ForgingScreenHandlerAccessor) (Object) this).getOutput();
                        resultInventory.setStack(0, resultStack);
                        this.levelCost.set(upgradeItem.getLevelCost());

                        ((AnvilScreenHandler) (Object) this).sendContentUpdates();

                        ci.cancel(); // prevent further vanilla processing
                    }
                }
            }
        }
    }
}
