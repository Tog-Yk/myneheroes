package net.togyk.myneheroes.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.custom.AbilityHoldingItem;
import net.togyk.myneheroes.Item.custom.AdvancedArmorItem;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.util.ScrollData;
import net.togyk.myneheroes.util.PlayerAbilities;
import net.togyk.myneheroes.util.PowerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerAbilityMixin implements PlayerAbilities {
    @Shadow public abstract void playSound(SoundEvent sound, float volume, float pitch);

    private List<Ability> abilities = new ArrayList<>();
    private boolean isDirty = false;

    private int scrolledAbilityOffset = 0;


    @Inject(at = @At("HEAD"), method = "tick")
    private void tick(CallbackInfo info) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (this.isDirty) {
            ScrollData.setScrolledAbilitiesOffset(player, ScrollData.getScrolledAbilitiesOffset(player));
            this.isDirty = false;
        }
        if (player != null) {
            List<Ability> bufferList = getAbilities(player);
            if (bufferList != null) {
                this.abilities = bufferList;
            }
        }
        for (Ability ability : this.abilities) {
            ability.tick();
        }
        if (this.scrolledAbilityOffset > this.maxAbilityScroll()) {
            this.scrolledAbilityOffset = this.maxAbilityScroll();
        } else if (scrolledAbilityOffset < 0) {
            this.scrolledAbilityOffset = 0;
        }
    }

    private List<Ability> getAbilities(PlayerEntity player) {
        List<Ability> abilityList = new ArrayList<>();
        List<Power> powerList = PowerData.getPowers(player);
        if (!powerList.isEmpty()) {
            for (Power power : powerList) {
                if (power != null && !power.isDampened()) {
                    // Have I told you I am really amazed about your progress and your skills?
                    for (Ability ability : power.getAbilities()) {
                        if (!abilityList.contains(ability)) {
                            ability.setHolder(power);
                            abilityList.add(ability);
                        }
                    }
                }
            }
        }

        PlayerInventory inventory = player.getInventory();
        ItemStack helmetStack = player.getEquippedStack(EquipmentSlot.HEAD);
        if (helmetStack.getItem() instanceof AdvancedArmorItem advancedArmorItem) {
            if (advancedArmorItem.getAbilities(helmetStack) != null) {
                for (Ability ability : advancedArmorItem.getAbilities(helmetStack)) {
                    if (!abilityList.contains(ability)) {
                        ability.setHolder(helmetStack);
                        abilityList.add(ability);
                    }
                }
            }
        }
        ItemStack chestplateStack = player.getEquippedStack(EquipmentSlot.CHEST);
        if (chestplateStack.getItem() instanceof AdvancedArmorItem advancedArmorItem) {
            if (advancedArmorItem.getAbilities(chestplateStack) != null) {
                for (Ability ability : advancedArmorItem.getAbilities(chestplateStack)) {
                    if (!abilityList.contains(ability)) {
                        ability.setHolder(chestplateStack);
                        abilityList.add(ability);
                    }
                }
            }
        }
        ItemStack leggingsStack = player.getEquippedStack(EquipmentSlot.LEGS);
        if (leggingsStack.getItem() instanceof AdvancedArmorItem advancedArmorItem) {
            if (advancedArmorItem.getAbilities(leggingsStack) != null) {
                for (Ability ability : advancedArmorItem.getAbilities(leggingsStack)) {
                    if (!abilityList.contains(ability)) {
                        ability.setHolder(leggingsStack);
                        abilityList.add(ability);
                    }
                }
            }
        }
        ItemStack bootsStack = player.getEquippedStack(EquipmentSlot.FEET);
        if (bootsStack.getItem() instanceof AdvancedArmorItem advancedArmorItem) {
            if (advancedArmorItem.getAbilities(bootsStack) != null) {
                for (Ability ability : advancedArmorItem.getAbilities(bootsStack)) {
                    if (!abilityList.contains(ability)) {
                        ability.setHolder(bootsStack);
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

    public List<Ability> getFilteredAbilities() {
        List<Ability> abilityList = new ArrayList<>();
        List<Identifier> ids = new ArrayList<>();
        for (Ability ability : this.abilities) {
            if (ability.Usable() && !(!ability.appearsMultipleTimes() && ids.contains(ability.getId()))) {
                abilityList.add(ability);
                ids.add(ability.getId());
            }
        }
        return abilityList;
    }

    @Override
    public Ability getFirstAbility() {
        List<Ability> filteredAbilities = getFilteredAbilities();
        if (filteredAbilities != null && filteredAbilities.size() >= 1 + this.getScrolledAbilityOffset()) {
            return filteredAbilities.get(this.getScrolledAbilityOffset());
        } else {
            return null;
        }
    }

    @Override
    public Ability getSecondAbility() {
        List<Ability> filteredAbilities = getFilteredAbilities();
        if (filteredAbilities != null && filteredAbilities.size() >= 2 + this.getScrolledAbilityOffset()) {
            return filteredAbilities.get(1 + this.getScrolledAbilityOffset());
        } else {
            return null;
        }
    }

    @Override
    public Ability getThirdAbility() {
        List<Ability> filteredAbilities = getFilteredAbilities();
        if (filteredAbilities != null && filteredAbilities.size() >= 3 + this.getScrolledAbilityOffset()) {
            return filteredAbilities.get(2 + this.getScrolledAbilityOffset());
        } else {
            return null;
        }
    }

    @Override
    public Ability getFourthAbility() {
        List<Ability> filteredAbilities = getFilteredAbilities();
        if (filteredAbilities != null && filteredAbilities.size() >= 4 + this.getScrolledAbilityOffset()) {
            return filteredAbilities.get(3 + this.getScrolledAbilityOffset());
        } else {
            return null;
        }
    }




    @Inject(at = @At("HEAD"), method = "readCustomDataFromNbt")
    private void readFromNbt(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains(MyneHeroes.MOD_ID)) {
            NbtCompound modNbt = nbt.getCompound(MyneHeroes.MOD_ID);
            if (modNbt.contains("scrolled_ability_offset")) {
                this.scrolledAbilityOffset = modNbt.getInt("scrolled_ability_offset");
            }
        }
        this.isDirty = true;
    }
    @Inject(at = @At("HEAD"), method = "writeCustomDataToNbt")
    private void writeToNbt(NbtCompound nbt, CallbackInfo info) {
        NbtCompound modNbt = new NbtCompound();
        if (nbt.contains(MyneHeroes.MOD_ID)) {
            modNbt = nbt.getCompound(MyneHeroes.MOD_ID);
        }

        modNbt.putInt("scrolled_ability_offset",this.scrolledAbilityOffset);

        nbt.put(MyneHeroes.MOD_ID,modNbt);
    }

    @Override
    public int getScrolledAbilityOffset() {
        return Math.max(Math.min(scrolledAbilityOffset, this.abilities.size() - 4), 0);
    }

    @Override
    public void setScrolledAbilityOffset(int scrolledAbilityOffset) {
        this.scrolledAbilityOffset = scrolledAbilityOffset;
    }

    @Override
    public boolean canScrollAbilityFurther() {
        return this.maxAbilityScroll() != this.scrolledAbilityOffset;
    }

    @Override
    public int maxAbilityScroll() {
        return this.getFilteredAbilities().size() - 4;
    }

    @Override
    public void scrollAbilityFurther() {
        if (this.scrolledAbilityOffset < this.maxAbilityScroll()) {
            this.scrolledAbilityOffset += 1;
        }
    }

    @Override
    public void scrollAbilityBack() {
        if (this.scrolledAbilityOffset > 0) {
            this.scrolledAbilityOffset -= 1;
        }
    }

    @Override
    public List<Ability> getAbilities() {
        return abilities;
    }
}