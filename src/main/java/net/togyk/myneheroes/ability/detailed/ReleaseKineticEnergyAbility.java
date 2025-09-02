package net.togyk.myneheroes.ability.detailed;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.PassiveAbility;
import net.togyk.myneheroes.damage.ModDamageTypes;
import net.togyk.myneheroes.util.PlayerAbilities;
import net.togyk.myneheroes.util.StockPile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReleaseKineticEnergyAbility extends Ability implements PassiveAbility, StockPile {
    private float charge = 0;
    protected final float maxCharge;
    protected final float rangeMultiplier;

    private final Identifier chargeIcon;

    public ReleaseKineticEnergyAbility(Identifier id, Ability.Settings settings, int cooldown, float maxCharge, float rangeMultiplier) {
        super(id, cooldown, settings, null);
        this.maxCharge = maxCharge;
        this.chargeIcon = Identifier.of(MyneHeroes.MOD_ID,"textures/ability/charge/"+id.getPath()+".png");
        this.rangeMultiplier = rangeMultiplier;
    }

    @Override
    public void tick(PlayerEntity player) {
        super.tick(player);
    }

    @Override
    public void use(PlayerEntity player) {
        float charge = 0;
        List<Ability> abilities = ((PlayerAbilities) player).myneheroes$getAbilities();
        List<Ability> matchingAbilities = new ArrayList<>();
        for (Ability ability : abilities) {
            if (ability.getId() == this.getId() && ability instanceof StockPile stockpileAbility) {
                charge += stockpileAbility.getCharge();
                matchingAbilities.add(ability);
            }
        }

        if (this.getCooldown() == 0 && charge != 0) {
            //*kinetic energy explosion*
            this.explode(player, (float) (this.rangeMultiplier * Math.sqrt((double) charge / 50)), (float) Math.sqrt((double) charge / 100), (float) Math.sqrt((double) charge / 75));

            for (Ability ability : matchingAbilities) {
                ((StockPile) ability).setCharge(0);
                ability.setCooldown(this.getMaxCooldown());
                ability.save();
            }
        }
    }

    private void explode(PlayerEntity player, float radius, float knockback, float damage) {
        World world = player.getWorld();
        // Get all entities within the radius, excluding the player.
        List<Entity> targets = world.getEntitiesByClass(Entity.class,
                player.getBoundingBox().expand(radius),
                entity -> entity != player);

        for (Entity target : targets) {
            // Compute the vector from player to the target.
            Vec3d direction = target.getPos().subtract(player.getPos());
            double distance = direction.length();
            if (distance > 0) {
                // Normalize and optionally scale by a falloff factor.
                Vec3d knockbackVector = direction.normalize().multiply(knockback * (1 - distance / radius));
                // Velocity on the y-axis should be a bit less due to gravity and all.
                knockbackVector.multiply(1, 0.75, 1);

                target.addVelocity(knockbackVector.x, knockbackVector.y, knockbackVector.z);
            }
            // Damage the entity using the player as the source.
            target.damage(ModDamageTypes.of(player.getWorld(), ModDamageTypes.POWERFUL_PUNCH_TYPE_KEY, player), (float) (damage * (1 - distance / radius)));
        }

        // Spawn particles around the player.
        Random random = new Random();
        for (int i = 0; i < 30 * radius; i++) {
            double speedX = (random.nextDouble() - 0.5) * 2;
            double speedY = (random.nextDouble() - 0.5) * 2;
            double speedZ = (random.nextDouble() - 0.5) * 2;
            world.addParticle(ParticleTypes.DRAGON_BREATH,
                    player.getX(),
                    player.getY() + player.getHeight() / 2,
                    player.getZ(),
                    speedX, speedY, speedZ);
        }
    }

    @Override
    public boolean onGotHit(PlayerEntity player, DamageSource source, float amount) {
        Random random = new Random();
        if (this.getMaxCharge() != this.getCharge()) {
            this.setCharge((getCharge() + amount * (random.nextFloat(0, 6))));
        }
        this.save();
        return true;
    }

    @Override
    public boolean Usable() {
        return true;
    }

    @Override
    public Identifier getStockPileId() {
        return this.getId();
    }

    public float getCharge() {
        return charge;
    }

    public void setCharge(float charge) {
        this.charge = Math.min(charge, this.getMaxCharge());
    }

    public float getMaxCharge() {
        return maxCharge;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putFloat("charge", this.getCharge());
        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if (nbt.contains("charge")) {
            this.setCharge(nbt.getFloat("charge"));
        }
    }

    public Identifier getChargeIcon() {
        return chargeIcon;
    }

    @Override
    public boolean appearsMultipleTimes() {
        return false;
    }

    @Override
    public ReleaseKineticEnergyAbility copy() {
        return new ReleaseKineticEnergyAbility(id, settings, maxCooldown, maxCharge, rangeMultiplier);
    }
}
