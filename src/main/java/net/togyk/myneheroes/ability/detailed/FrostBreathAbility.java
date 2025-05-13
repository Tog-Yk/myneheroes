package net.togyk.myneheroes.ability.detailed;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.togyk.myneheroes.ability.StockpileLinkedAbility;
import net.togyk.myneheroes.power.StockpilePower;

import java.util.List;
import java.util.Random;

public class FrostBreathAbility extends StockpileLinkedAbility {
    protected final int range;

    public FrostBreathAbility(Identifier id, String name, int cooldown, int unlocksAt, int cost, int range) {
        super(id, name, cooldown, unlocksAt, cost);
        this.range = range;
    }

    @Override
    public void Use(PlayerEntity player) {
        World world = player.getWorld();
        if (this.getCooldown() == 0 && this.getHolderItem() instanceof StockpilePower power && power.getCharge() >= this.getCost()) {
            Vec3d direction = player.getRotationVec(1.0F);
            Vec3d origin = player.getEyePos().add(0, -0.2, 0);
            if (!world.isClient()) {

                Box area = new Box(origin, origin.add(direction.multiply(range))).expand(1.5);

                List<Entity> targets = world.getOtherEntities(player, area, entity -> entity instanceof LivingEntity);
                for (Entity entity : targets) {
                    if (entity instanceof LivingEntity living && living.isAlive()) {
                        living.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 2)); // Slowness III
                        living.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 60, 1)); // Optional
                        entity.setFrozenTicks(Math.min( entity.getFrozenTicks() + 4, 200)); // Freezing effect (vanilla)
                    }
                }

                // Optional: frost particles or freezing blocks
                freezeGround(world, origin, direction, range);

                power.setCharge(power.getCharge() - this.getCost());
                this.setCooldown(this.getMaxCooldown());
            }
            Random random = new Random();
            for (int i = 0; i < range; i++) {
                double speedX = (random.nextDouble()) * direction.x / 2;
                double speedY = (random.nextDouble()) * direction.y / 2;
                double speedZ = (random.nextDouble()) * direction.z / 2;
                world.addParticle(ParticleTypes.SNOWFLAKE,
                        origin.getX(),
                        origin.getY(),
                        origin.getZ(),
                        speedX, speedY, speedZ);
            }
        }
        this.save();
    }

    private static void freezeGround(World world, Vec3d origin, Vec3d direction, double range) {
        Vec3d current = origin;
        for (int i = 0; i < range * 2; i++) {
            BlockPos pos = BlockPos.ofFloored(current);
            BlockPos below = pos.down();
            if (world.getBlockState(pos).isOf(Blocks.WATER)) {
                world.setBlockState(pos, Blocks.FROSTED_ICE.getDefaultState());
            } else if (world.getBlockState(pos).isIn(BlockTags.FIRE)) {
                BlockSoundGroup soundGroup = world.getBlockState(pos).getSoundGroup();
                world.playSound(null ,pos, soundGroup.getBreakSound(), SoundCategory.BLOCKS, soundGroup.getVolume(), soundGroup.getPitch());
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
            } else if (world.getBlockState(pos).isOf(Blocks.LAVA)) {
                world.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState());
            } else if (world.getBlockState(below).isFullCube(world, below) && world.getBlockState(pos).isAir()) {
                world.setBlockState(pos, Blocks.SNOW.getDefaultState());
            }
            current = current.add(direction.multiply(0.5));
        }
    }

    @Override
    public FrostBreathAbility copy() {
        return new FrostBreathAbility(this.id, this.abilityName, this.maxCooldown, this.unlocksAt, this.cost, this.range);
    }
}
