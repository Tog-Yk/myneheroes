package net.togyk.myneheroes.ability.detailed;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.AttributeModifierAbility;

public class RageAbility extends Ability implements AttributeModifierAbility {
    protected final float trigger;
    private boolean isActive = false;

    public RageAbility(Identifier id, Settings settings, float trigger) {
        super(id, 0, settings, null);
        this.trigger = trigger;
    }

    @Override
    public void tick(PlayerEntity player) {
        this.isActive = player.getHealth() <= trigger * player.getMaxHealth();
        super.save();
    }

    @Override
    public boolean isHidden() {
        return false;
    }


    public boolean isActive() {
        return isActive;
    }

    public Double getAttackDamage() {
        return isActive() ? 0.1D : 0;
    }

    public Double getArmor() {
        return isActive() ? 4.0D : 0;
    }


    public Double getSpeed() {
        return isActive() ? 0.5D : 0;
    }

    @Override
    public attributeModifiers getAttributeModifiers() {
        Identifier id = this.getId();
        return new attributeModifiers()
                .addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, Identifier.of(id.getNamespace(), id.getPath() + "_attack_damage"), EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, this::getAttackDamage)
                .addAttributeModifier(EntityAttributes.GENERIC_ARMOR, Identifier.of(id.getNamespace(), id.getPath() + "_armor"), EntityAttributeModifier.Operation.ADD_VALUE, this::getArmor)
                .addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, Identifier.of(id.getNamespace(), id.getPath() + "_attack_speed"), EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, this::getSpeed)
                .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, Identifier.of(id.getNamespace(), id.getPath() + "_speed"), EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, this::getSpeed);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putBoolean("is_active", this.isActive());

        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        if (nbt.getBoolean("is_active")) {
            this.isActive = nbt.getBoolean("is_active");
        }

        super.readNbt(nbt);
    }

    @Override
    public Ability copy() {
        return new RageAbility(this.id, this.settings, this.trigger);
    }
}
