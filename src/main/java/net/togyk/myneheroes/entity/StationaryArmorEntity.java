package net.togyk.myneheroes.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.togyk.myneheroes.Item.custom.AdvancedArmorItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
                this.handleArmorTransfer(player, ((ArmorItem) stack.getItem()).getSlotType(), stack);
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

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.isRemoved()) {
            return false;
        } else {
            World var4 = this.getWorld();
            if (var4 instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld)var4;
                if (source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
                    this.kill();
                    return false;
                } else if (!this.isInvulnerableTo(source) && !this.isInvulnerable()) {
                    if (source.isIn(DamageTypeTags.IS_EXPLOSION)) {
                        this.playBreakSound();
                        this.dropArmor();
                        this.kill();
                        return false;
                    } else if (source.isIn(DamageTypeTags.IGNITES_ARMOR_STANDS)) {
                        if (this.isOnFire()) {
                            this.updateHealth(serverWorld, source, 0.15F);
                        } else {
                            this.setOnFireFor(5.0F);
                        }

                        return false;
                    } else if (source.isIn(DamageTypeTags.BURNS_ARMOR_STANDS) && this.getHealth() > 0.5F) {
                        this.updateHealth(serverWorld, source, 4.0F);
                        return false;
                    } else {
                        boolean bl = source.isIn(DamageTypeTags.CAN_BREAK_ARMOR_STAND);
                        boolean bl2 = source.isIn(DamageTypeTags.ALWAYS_KILLS_ARMOR_STANDS);
                        if (!bl && !bl2) {
                            return false;
                        } else {
                            Entity attacker = source.getAttacker();
                            if (attacker instanceof PlayerEntity playerEntity) {
                                if (!playerEntity.getAbilities().allowModifyWorld) {
                                    return false;
                                }
                            }

                            if (source.isSourceCreativePlayer()) {
                                this.playBreakSound();
                                this.dropArmor();
                                this.kill();
                                return true;
                            } else {
                                long l = serverWorld.getTime();
                                if (l - this.lastHitTime > 5L && !bl2) {
                                    serverWorld.sendEntityStatus(this, (byte)32);
                                    this.emitGameEvent(GameEvent.ENTITY_DAMAGE, source.getAttacker());
                                    this.lastHitTime = l;
                                } else {
                                    this.playBreakSound();
                                    this.dropArmor();
                                    this.kill();
                                }

                                return true;
                            }
                        }
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    private void updateHealth(ServerWorld world, DamageSource damageSource, float amount) {
        float f = this.getHealth();
        f -= amount;
        if (f <= 0.5F) {
            this.dropArmor();
            this.kill();
        } else {
            this.setHealth(f);
            this.emitGameEvent(GameEvent.ENTITY_DAMAGE, damageSource.getAttacker());
        }

    }

    private void playBreakSound() {
        this.getWorld().playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), this.getDeathSound(), this.getSoundCategory(), 1.0F, 1.0F);
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        ItemStack helmet = this.getEquippedStack(EquipmentSlot.HEAD);
        if (helmet.getItem() instanceof AdvancedArmorItem item) {
            return item.getMaterial().value().equipSound().value();
        }
        return super.getDeathSound();
    }

    @Override
    public boolean shouldHideBasePlate() {
        return true;
    }

    @Override
    public ItemStack getPickBlockStack() {
        return ItemStack.EMPTY;
    }
}
