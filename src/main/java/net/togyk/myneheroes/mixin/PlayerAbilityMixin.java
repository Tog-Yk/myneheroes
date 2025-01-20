package net.togyk.myneheroes.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.togyk.myneheroes.Item.custom.AbilityHoldingItem;
import net.togyk.myneheroes.Item.custom.AdvancedArmorItem;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.util.PlayerAbilities;
import net.togyk.myneheroes.util.PlayerPowers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerAbilityMixin implements PlayerAbilities {
    private List<Ability> abilities = new ArrayList<>();


    @Inject(at = @At("HEAD"), method = "tick")
    private void tick(CallbackInfo info) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            List<Ability> bufferList = getAbilities(player);
            if (bufferList != null) {
                abilities = bufferList;
            }
        }
        for (Ability ability : abilities) {
            ability.tick();
        }
    }

    private List<Ability> getAbilities(PlayerEntity player) {
        List<Ability> abilityList = new ArrayList<>();
        List<Power> powerList = ((PlayerPowers) player).getPowers();
        if (!powerList.isEmpty()) {
            for (Power power : powerList) {
                if (!power.isDampened()) {
                    for (Ability ability : power.abilities) {
                        if (!abilityList.contains(ability)) {
                            abilityList.add(ability);
                        }
                    }
                }
            }
        }

        PlayerInventory inventory = player.getInventory();
        ItemStack helmetStack = player.getEquippedStack(EquipmentSlot.HEAD);
        if (helmetStack.getItem() instanceof AdvancedArmorItem advancedArmorItem) {
            if (advancedArmorItem.abilities != null) {
                for (Ability ability : advancedArmorItem.abilities) {
                    if (!abilityList.contains(ability)) {
                        abilityList.add(ability);
                    }
                }
            }
        }
        ItemStack chestplateStack = player.getEquippedStack(EquipmentSlot.CHEST);
        if (chestplateStack.getItem() instanceof AdvancedArmorItem advancedArmorItem) {
            if (advancedArmorItem.abilities != null) {
                for (Ability ability : advancedArmorItem.abilities) {
                    if (!abilityList.contains(ability)) {
                        abilityList.add(ability);
                    }
                }
            }
        }
        ItemStack leggingsStack = player.getEquippedStack(EquipmentSlot.LEGS);
        if (leggingsStack.getItem() instanceof AdvancedArmorItem advancedArmorItem) {
            if (advancedArmorItem.abilities != null) {
                for (Ability ability : advancedArmorItem.abilities) {
                    if (!abilityList.contains(ability)) {
                        abilityList.add(ability);
                    }
                }
            }
        }
        ItemStack bootsStack = player.getEquippedStack(EquipmentSlot.FEET);
        if (bootsStack.getItem() instanceof AdvancedArmorItem advancedArmorItem) {
            if (advancedArmorItem.abilities != null) {
                for (Ability ability : advancedArmorItem.abilities) {
                    if (!abilityList.contains(ability)) {
                        abilityList.add(ability);
                    }
                }
            }
        }
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (abilities != null) {
                abilities = new ArrayList<>();
            }
            if (stack.getItem() instanceof AbilityHoldingItem abilityHoldingItem) {
                if (!abilityList.contains(abilityHoldingItem.ability)) {
                    abilityList.add(abilityHoldingItem.ability);
                }
            }
        }
        return abilityList;
    }

    @Override
    public Ability getFirstAbility() {
        if (abilities != null && !abilities.isEmpty()) {
            return abilities.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Ability getSecondAbility() {
        if (abilities != null && abilities.size() >= 2) {
            return abilities.get(1);
        } else {
            return null;
        }
    }

    @Override
    public Ability getThirdAbility() {
        if (abilities != null && abilities.size() >= 3) {
            return abilities.get(2);
        } else {
            return null;
        }
    }

    @Override
    public Ability getFourthAbility() {
        if (abilities != null && abilities.size() >= 4) {
            return abilities.get(3);
        } else {
            return null;
        }
    }
}