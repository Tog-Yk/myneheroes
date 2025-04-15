package net.togyk.myneheroes.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.togyk.myneheroes.Item.custom.AdvancedArmorItem;
import org.jetbrains.annotations.NotNull;

public class StationaryArmorEntity extends ArmorStandEntity {
    private static final TrackedData<Boolean> isArmoring = DataTracker.registerData(StationaryArmorEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public StationaryArmorEntity(EntityType<? extends ArmorStandEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void tick() {
        Iterable<ItemStack> armorItems = this.getArmorItems();
        for (ItemStack stack : armorItems) {
            if (stack.isEmpty()) {
                this.dropArmor();
                this.discard();
            }
        }

        //if there is no player nearby
        if (!isArmoring() && this.getWorld().getClosestPlayer(this, 0.75) == null) {
            setArmoring(true);
        }

        super.tick();
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(isArmoring, false);
    }

    @NotNull
    public boolean isArmoring() {
        return this.getDataTracker().get(isArmoring);
    }

    public void setArmoring(boolean bl) {
        this.getDataTracker().set(isArmoring, bl);
    }

    @Override
    public boolean canEquip(ItemStack stack) {
        return stack.getItem() instanceof AdvancedArmorItem;
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return super.getArmorItems(); // this returns the armor items
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        return super.getEquippedStack(slot);
    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {
        super.equipStack(slot, stack);
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25);
    }

    public void dropArmor() {
        Iterable<ItemStack> armorItems = this.getArmorItems();
        for (ItemStack stack : armorItems) {
            if (!stack.isEmpty()) {
                Block.dropStack(this.getWorld(), this.getBlockPos().up(), stack);
                this.equipStack(((AdvancedArmorItem) stack.getItem()).getSlotType(), ItemStack.EMPTY);
            }
        }
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        if (distanceTo(player) < 0.5) {
            if (!this.getWorld().isClient && this.isArmoring() && !hasArmorOn(player)) {
                this.setArmoring(false);
                equipPlayerWithArmor(player);
                this.discard();
            }
        }
    }

    private boolean hasArmorOn(PlayerEntity player) {
        Iterable<ItemStack> armor = player.getArmorItems();
        for (ItemStack stack : armor) {
            if (!(stack.getItem() instanceof AdvancedArmorItem)) {
                return false;
            }
        }
        return true;
    }

    // Method to equip the player with armor and handle inventory
    private void equipPlayerWithArmor(PlayerEntity player) {
        Iterable<ItemStack> armorItems = this.getArmorItems();
        for (ItemStack stack : armorItems) {
            if (!stack.isEmpty()) {
                this.handleArmorTransfer(player, ((AdvancedArmorItem) stack.getItem()).getSlotType(), stack);
            }
        }
    }

    // Helper method to handle transferring current armor to inventory or dropping it
    private void handleArmorTransfer(PlayerEntity player, EquipmentSlot armorSlot, ItemStack newArmor) {
        ItemStack currentArmor = player.getEquippedStack(armorSlot);

        // Check if the player is already wearing armor in the specified slot
        if (!currentArmor.isEmpty()) {
            // Attempt to place the current armor in the player's inventory
            boolean addedToInventory = player.getInventory().insertStack(currentArmor);

            // If the armor couldn't be added to inventory, drop it on the ground
            if (!addedToInventory) {
                player.dropItem(currentArmor, true); // Drop the current armor item
            }
        }

        // Equip the new armor
        player.equipStack(armorSlot, newArmor);
    }
}
