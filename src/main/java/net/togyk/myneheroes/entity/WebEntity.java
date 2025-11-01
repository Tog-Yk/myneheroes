package net.togyk.myneheroes.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.togyk.myneheroes.Item.ModItems;
import net.togyk.myneheroes.entity.trail.LightningTrailEntity;
import net.togyk.myneheroes.entity.trail.TrailEntity;
import net.togyk.myneheroes.particle.ModParticles;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Random;

public class WebEntity extends PersistentProjectileEntity {
    private static final TrackedData<Boolean> isTaser = DataTracker.registerData(WebEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    protected WebEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public WebEntity(World world) {
        super(ModEntities.WEB, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (isTaser()) {
            Random random1 = new Random();

            if (random1.nextFloat() <= 0.1F) {

                Vec3d pos = getPos();

                this.getWorld().addParticle(ModParticles.ELECTRICITY_PARTICLE,
                        pos.getX(), pos.getY() + 0.125F, pos.getZ(),
                        0, 0, 0);
            }
        }

        //slow colliding entities down
        for (Entity entity : this.getWorld().getOtherEntities(this, this.getBoundingBox().expand(0.25), Entity::isAlive)) {
            if (entity instanceof WebEntity) {
                continue;
            }
            Vec3d vec3d = new Vec3d(0.5F, 0.3F, 0.5);
            if (entity instanceof LivingEntity livingEntity && livingEntity.hasStatusEffect(StatusEffects.WEAVING)) {
                vec3d = new Vec3d(0.75F, 0.5F, 0.75F);
            }

            entity.setVelocity(entity.getVelocity().multiply(vec3d));
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();

        if (isTaser()) {
            //spawn a LightningTrail as electricity
            TrailEntity trail = new LightningTrailEntity(entity, Optional.empty(), 20, 7, 7, 0x3300FFFF, 0xFFF0FFFF);
            trail.setPosition(entity.getX(), entity.getY(), entity.getZ());

            entity.getWorld().spawnEntity(trail);

            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 30, 1));
            }
        }

        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 30, 1));
            //slow entity down
            Vec3d vec3d = new Vec3d(0.25, 0.05F, 0.25);
            if (livingEntity.hasStatusEffect(StatusEffects.WEAVING)) {
                vec3d = new Vec3d(0.5, 0.25, 0.5);
            }

            entity.setVelocity(entity.getVelocity().multiply(vec3d));
        }
    }


    @Override
    public double getDamage() {
        return isTaser()? super.getDamage() * 1.5 : super.getDamage();
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return ModItems.WEB_FLUID.getDefaultStack();
    }

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        return false;
    }


    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(isTaser, false);
    }

    @NotNull
    public boolean isTaser() {
        return this.getDataTracker().get(isTaser);
    }

    public void setIsTaser(boolean b) {
        this.getDataTracker().set(isTaser, b);
    }
}
