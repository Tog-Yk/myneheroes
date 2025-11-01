package net.togyk.myneheroes.mixin;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.custom.AbilityHolding;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.networking.PlayerAbilitySyncDataPayload;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.util.PlayerAbilities;
import net.togyk.myneheroes.util.PowerData;
import net.togyk.myneheroes.util.ScrollData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerAbilityMixin implements PlayerAbilities {
    @Shadow public abstract void playSound(SoundEvent sound, float volume, float pitch);

    @Unique
    private List<Ability> abilities = new ArrayList<>();
    @Unique
    private boolean isDirty = false;

    @Unique
    private int scrolledAbilityOffset = 0;

    @Unique
    private boolean isHoldingJump = false;


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
        //sync the data to other players
        if (player instanceof ServerPlayerEntity serverPlayer) {
            for (ServerPlayerEntity player1 : serverPlayer.getServer().getPlayerManager().getPlayerList()) {
                if (player1 != serverPlayer) {
                    ServerPlayNetworking.send(player1, new PlayerAbilitySyncDataPayload(this.abilities, player.getUuid()));
                }
            }
        }
    }

    private List<Ability> myneheroes$getAbilities(PlayerEntity player) {
        List<Ability> abilityList = new ArrayList<>();
        List<Power> powerList = PowerData.getPowers(player);
        if (!powerList.isEmpty()) {
            for (Power power : powerList) {
                if (power != null) {
                    power.setHolder(player);
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
        if (helmetStack.getItem() instanceof AbilityHolding abilityHolding) {
            if (abilityHolding.getAbilities(helmetStack) != null) {
                //get the abilities for when the item is equipped
                for (Ability ability : abilityHolding.getArmorAbilities(helmetStack)) {
                    if (!abilityList.contains(ability) && ability != null) {
                        abilityList.add(ability);
                    }
                }
            }
        }
        ItemStack chestplateStack = player.getEquippedStack(EquipmentSlot.CHEST);
        if (chestplateStack.getItem() instanceof AbilityHolding abilityHolding) {
            if (abilityHolding.getAbilities(chestplateStack) != null) {
                //get the abilities for when the item is equipped
                for (Ability ability : abilityHolding.getArmorAbilities(chestplateStack)) {
                    if (!abilityList.contains(ability) && ability != null) {
                        abilityList.add(ability);
                    }
                }
            }
        }
        ItemStack leggingsStack = player.getEquippedStack(EquipmentSlot.LEGS);
        if (leggingsStack.getItem() instanceof AbilityHolding abilityHolding) {
            if (abilityHolding.getAbilities(leggingsStack) != null) {
                //get the abilities for when the item is equipped
                for (Ability ability : abilityHolding.getArmorAbilities(leggingsStack)) {
                    if (!abilityList.contains(ability) && ability != null) {
                        abilityList.add(ability);
                    }
                }
            }
        }
        ItemStack bootsStack = player.getEquippedStack(EquipmentSlot.FEET);
        if (bootsStack.getItem() instanceof AbilityHolding abilityHolding) {
            if (abilityHolding.getAbilities(bootsStack) != null) {
                //get the abilities for when the item is equipped
                for (Ability ability : abilityHolding.getArmorAbilities(bootsStack)) {
                    if (!abilityList.contains(ability) && ability != null) {
                        abilityList.add(ability);
                    }
                }
            }
        }
        //get the abilities from the main hand
        ItemStack mainHandStack = player.getStackInHand(Hand.MAIN_HAND);
        if (mainHandStack.getItem() instanceof AbilityHolding abilityHolding) {
            if (abilityHolding.getAbilities(mainHandStack) != null) {
                for (Ability ability : abilityHolding.getMainHandAbilities(mainHandStack)) {
                    if (!abilityList.contains(ability) && ability != null) {
                        abilityList.add(ability);
                    }
                }
            }
        }
        //get the abilities from the offhand
        ItemStack offHandStack = player.getStackInHand(Hand.OFF_HAND);
        if (offHandStack.getItem() instanceof AbilityHolding abilityHolding) {
            if (abilityHolding.getAbilities(offHandStack) != null) {
                for (Ability ability : abilityHolding.getOffHandAbilities(offHandStack)) {
                    if (!abilityList.contains(ability) && ability != null) {
                        abilityList.add(ability);
                    }
                }
            }
        }
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            //get the abilities in the hotbar
            if (i < 9 && stack.getItem() instanceof AbilityHolding abilityHolding) {
                if (abilityHolding.getAbilities(stack) != null) {
                    for (Ability ability : abilityHolding.getHotbarAbilities(stack)) {
                        if (!abilityList.contains(ability) && ability != null) {
                            abilityList.add(ability);
                        }
                    }
                }
            }
            //get the abilities in the inventory
            if (stack.getItem() instanceof AbilityHolding abilityHolding) {
                if (abilityHolding.getAbilities(stack) != null) {
                    for (Ability ability : abilityHolding.getAbilities(stack)) {
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
        return Math.max(this.myneheroes$getFilteredAbilities().size() - 4, 0);
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
    @Override
    public void myneheroes$setAbilities(List<Ability> abilities) {
        this.abilities = abilities;
    }

    @Override
    public boolean myneheroes$isHoldingJump() {
        return isHoldingJump;
    }

    @Override
    public void myneheroes$setIsHoldingJump(boolean jumping) {
        this.isHoldingJump = jumping;
    }
}