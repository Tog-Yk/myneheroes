package net.togyk.myneheroes.Item.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.togyk.myneheroes.entity.LaserEntity;

public class ThrowableShieldItem extends ShieldItem implements StationaryItem{
    private final float bonusAttackDamage;
    private final EntityType<LaserEntity> projectileEntityType;

    public ThrowableShieldItem(EntityType<LaserEntity> projectileEntity, float bonusAttackDamage, Settings settings) {
        super(settings);
        this.bonusAttackDamage = bonusAttackDamage;
        this.projectileEntityType = projectileEntity;
    }

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        return this.bonusAttackDamage;
    }

    public EntityType<LaserEntity> getProjectileEntityType() {
        return projectileEntityType;
    }

    @Override
    public ActionResult interactEntity(PlayerEntity player, Hand hand) {
        return ActionResult.PASS;
    }
}
