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
            if (!player.getWorld().isClient()) {
                ScrollData.setScrolledAbilitiesOffset(player, scrolledAbilityOffset);
            }
            this.isDirty = false;
        }
        if (player != null) {
            List<Ability> bufferList = myneheroes$getAbilities(player);
            if (bufferList != null) {
                this.abilities = bufferList;
            }
        }
        for (Ability ability : this.abilities) {
            if (ability != null) {
                ability.tick(player);
            }
        }
        if (this.scrolledAbilityOffset > this.myneheroes$maxAbilityScroll()) {
            this.scrolledAbilityOffset = this.myneheroes$maxAbilityScroll();
        } else if (scrolledAbilityOffset < 0) {
            this.scrolledAbilityOffset = 0;
        }
    }

    private List<Ability> myneheroes$getAbilities(PlayerEntity player) {
        List<Ability> abilityList = new ArrayList<>();
        List<Power> powerList = PowerData.getPowers(player);
        if (!powerList.isEmpty()) {
            for (Power power : powerList) {
                if (power != null) {
                    // Have I told you I am really amazed about your progress and your skills?
                    for (Ability ability : power.getAbilities()) {
                        if (!abilityList.contains(ability) && ability != null) {
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
            if (advancedArmorItem.getAbilities(helmetStack, player.getWorld()) != null) {
                for (Ability ability : advancedArmorItem.getAbilities(helmetStack, player.getWorld())) {
                    if (!abilityList.contains(ability) && ability != null) {
                        abilityList.add(ability);
                    }
                }
            }
        }
        ItemStack chestplateStack = player.getEquippedStack(EquipmentSlot.CHEST);
        if (chestplateStack.getItem() instanceof AdvancedArmorItem advancedArmorItem) {
            if (advancedArmorItem.getAbilities(chestplateStack, player.getWorld()) != null) {
                for (Ability ability : advancedArmorItem.getAbilities(chestplateStack, player.getWorld())) {
                    if (!abilityList.contains(ability) && ability != null) {
                        abilityList.add(ability);
                    }
                }
            }
        }
        ItemStack leggingsStack = player.getEquippedStack(EquipmentSlot.LEGS);
        if (leggingsStack.getItem() instanceof AdvancedArmorItem advancedArmorItem) {
            if (advancedArmorItem.getAbilities(leggingsStack, player.getWorld()) != null) {
                for (Ability ability : advancedArmorItem.getAbilities(leggingsStack, player.getWorld())) {
                    if (!abilityList.contains(ability) && ability != null) {
                        abilityList.add(ability);
                    }
                }
            }
        }
        ItemStack bootsStack = player.getEquippedStack(EquipmentSlot.FEET);
        if (bootsStack.getItem() instanceof AdvancedArmorItem advancedArmorItem) {
            if (advancedArmorItem.getAbilities(bootsStack, player.getWorld()) != null) {
                for (Ability ability : advancedArmorItem.getAbilities(bootsStack, player.getWorld())) {
                    if (!abilityList.contains(ability) && ability != null) {
                        abilityList.add(ability);
                    }
                }
            }
        }
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.getItem() instanceof AbilityHoldingItem abilityHoldingItem) {
                if (abilityHoldingItem.getAbilities(stack, player.getWorld()) != null) {
                    for (Ability ability : abilityHoldingItem.getAbilities(stack, player.getWorld())) {
                        if (!abilityList.contains(ability) && ability != null) {
                            abilityList.add(ability);
                        }
                    }
                }
            }
        }
        return abilityList;
    }

    public List<Ability> myneheroes$getFilteredAbilities() {
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
    public Ability myneheroes$getFirstAbility() {
        List<Ability> filteredAbilities = myneheroes$getFilteredAbilities();
        if (filteredAbilities != null && filteredAbilities.size() >= 1 + this.myneheroes$getScrolledAbilityOffset()) {
            return filteredAbilities.get(this.myneheroes$getScrolledAbilityOffset());
        } else {
            return null;
        }
    }

    @Override
    public Ability myneheroes$getSecondAbility() {
        List<Ability> filteredAbilities = myneheroes$getFilteredAbilities();
        if (filteredAbilities != null && filteredAbilities.size() >= 2 + this.myneheroes$getScrolledAbilityOffset()) {
            return filteredAbilities.get(1 + this.myneheroes$getScrolledAbilityOffset());
        } else {
            return null;
        }
    }

    @Override
    public Ability myneheroes$getThirdAbility() {
        List<Ability> filteredAbilities = myneheroes$getFilteredAbilities();
        if (filteredAbilities != null && filteredAbilities.size() >= 3 + this.myneheroes$getScrolledAbilityOffset()) {
            return filteredAbilities.get(2 + this.myneheroes$getScrolledAbilityOffset());
        } else {
            return null;
        }
    }

    @Override
    public Ability myneheroes$getFourthAbility() {
        List<Ability> filteredAbilities = myneheroes$getFilteredAbilities();
        if (filteredAbilities != null && filteredAbilities.size() >= 4 + this.myneheroes$getScrolledAbilityOffset()) {
            return filteredAbilities.get(3 + this.myneheroes$getScrolledAbilityOffset());
        } else {
            return null;
        }
    }

    @Override
    public Ability myneheroes$getAbilityBeforeFirst() {
        List<Ability> filteredAbilities = myneheroes$getFilteredAbilities();
        if (filteredAbilities != null && filteredAbilities.size() > 4 && this.myneheroes$getScrolledAbilityOffset() > 0) {
            return filteredAbilities.get(this.myneheroes$getScrolledAbilityOffset() - 1);
        } else {
            return null;
        }
    }

    @Override
    public Ability myneheroes$getFifthAbility() {
        List<Ability> filteredAbilities = myneheroes$getFilteredAbilities();
        if (filteredAbilities != null && filteredAbilities.size() >= 5 + this.myneheroes$getScrolledAbilityOffset()) {
            return filteredAbilities.get(4 + this.myneheroes$getScrolledAbilityOffset());
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
    public int myneheroes$getScrolledAbilityOffset() {
        return Math.max(scrolledAbilityOffset, 0);
    }

    @Override
    public void myneheroes$setScrolledAbilityOffset(int scrolledAbilityOffset) {
        this.scrolledAbilityOffset = scrolledAbilityOffset;
    }

    @Override
    public boolean myneheroes$canScrollAbilityFurther() {
        return this.myneheroes$maxAbilityScroll() != this.scrolledAbilityOffset;
    }

    @Override
    public int myneheroes$maxAbilityScroll() {
        return this.myneheroes$getFilteredAbilities().size() - 4;
    }

    @Override
    public void myneheroes$scrollAbilityFurther() {
        if (this.scrolledAbilityOffset < this.myneheroes$maxAbilityScroll()) {
            this.scrolledAbilityOffset += 1;
        }
    }

    @Override
    public void myneheroes$scrollAbilityBack() {
        if (this.scrolledAbilityOffset > 0) {
            this.scrolledAbilityOffset -= 1;
        }
    }

    @Override
    public List<Ability> myneheroes$getAbilities() {
        return abilities;
    }
}