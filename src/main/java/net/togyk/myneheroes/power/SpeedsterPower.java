package net.togyk.myneheroes.power;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.entity.trail.AfterimageTrailEntity;
import net.togyk.myneheroes.entity.trail.LightningTrailEntity;
import net.togyk.myneheroes.entity.trail.TrailEntity;
import net.togyk.myneheroes.upgrade.ColorUpgrade;
import net.togyk.myneheroes.upgrade.Upgrade;
import net.togyk.myneheroes.util.AbilityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpeedsterPower extends Power implements VariableLinkedPower, UpgradablePower {
    private int speedLevel = 0;
    private final int maxSpeedLevel;

    private Optional<UUID> lastSegment = Optional.empty();
    private Vec3d lastSegmentPos;
    private int trailCooldown = 0;

    private boolean speedActive = true;
    private boolean phasing = false;

    private List<Upgrade> upgrades = new ArrayList<>();

    public SpeedsterPower(Identifier id, int color, List<Ability> abilities, int maxSpeedLevel, Settings settings, attributeModifiers attributeModifiers) {
        super(id, color, abilities, settings, attributeModifiers);
        this.maxSpeedLevel = maxSpeedLevel;
    }

    @Override
    public void tick(PlayerEntity player) {
        super.tick(player);

        if ((trailCooldown <= 0 ||(lastSegmentPos != null && player.getPos().distanceTo(lastSegmentPos) > 1)) && speedActive) {
            if (getSpeedLevel() > 9) {
                List<Integer> outerColors = new ArrayList<>();

                if (!upgrades.isEmpty()) {
                    for (Upgrade upgrade : this.upgrades) {
                        if (upgrade instanceof ColorUpgrade colorUpgrade) {
                            outerColors.add(ColorHelper.Argb.withAlpha(0x77, colorUpgrade.getColor(player.getWorld())));
                        }
                    }
                } else {
                    outerColors = List.of(ColorHelper.Argb.withAlpha(0x77, this.getColor()));
                }

                List<Integer> innerColors = outerColors.stream().map(color -> {
                    if (color != 0x99000000) {
                        return ColorHelper.Argb.withAlpha(0xFF, ColorHelper.Argb.lerp(1F, color, 0xFFFFFFFF));
                    } else {
                        return ColorHelper.Argb.withAlpha(0xFF, color);
                    }
                }).toList();

                TrailEntity trail = new LightningTrailEntity(player, lastSegment, 20, 6, 3, outerColors, innerColors);
                trail.setPosition(player.getX(), player.getY(), player.getZ());
                lastSegment = Optional.of(trail.getUuid());
                lastSegmentPos = player.getPos();
                trailCooldown = 10;

                player.getWorld().spawnEntity(trail);
            }
            if (getSpeedLevel() > 5 && getSpeedLevel() <= 10) {
                TrailEntity trail = new AfterimageTrailEntity(player, lastSegment, 20, 0.75F);
                trail.setPosition(player.getX(), player.getY(), player.getZ());
                lastSegment = Optional.of(trail.getUuid());
                lastSegmentPos = player.getPos();
                trailCooldown = 10;

                player.getWorld().spawnEntity(trail);
            }
        } else {
            if (trailCooldown > 0) {
                trailCooldown -= 1;
            }
        }
    }

    private Double getStepHeight() {
        if (isSpeedActive() && getSpeedLevel() >= 5) {
            return 1.0D;
        }
        return 0.0D;
    }

    private Double getSpeed() {
        if (isSpeedActive()) {
            return 0.5D * getSpeedLevel();
        }
        return 0.0D;
    }

    private Double getSafeFallDistance() {
        if (isSpeedActive()) {
            return 0.1D * getSpeedLevel();
        }
        return 0.0D;
    }

    private Double getFallDamageMultiplier() {
        if (isSpeedActive()) {
            return 1 - 0.08D * getSpeedLevel();
        }
        return 1.0D;
    }

    @Override
    public boolean canStandOnWater() {
        return this.speedActive && !isDampened() && this.getSpeedLevel() > 9 && this.getHolder() != null && (this.getHolder().getVelocity().x != 0 || this.getHolder().getVelocity().z != 0);
    }

    @Override
    public boolean isPhasing() {
        return phasing && !isDampened();
    }

    public void setPhasing(boolean phasing) {
        if (this.phasing == phasing) {
            return;
        }
        this.phasing = phasing;
    }

    @Override
    public boolean canWallClimb() {
        return super.canWallClimb() || (isSpeedActive() && this.speedLevel > 5 && this.getHolder().getPitch() <= -50);
    }

    public int getMaxSpeedLevel() {
        return maxSpeedLevel;
    }

    public int getSpeedLevel() {
        return speedLevel;
    }

    public void setSpeedLevel(int level) {
        if (level == this.speedLevel || level < 0 || level > this.maxSpeedLevel) {
            return;
        }
        this.speedLevel = level;
    }

    public boolean isSpeedActive() {
        return speedActive;
    }

    public void setSpeedActive(boolean active) {
        if (active == speedActive) {
            return;
        }
        speedActive = active;
    }

    @Override
    public Object get(String name) {
        if (name.equals("speedLevel")) {
            return this.getSpeedLevel();
        } else if (name.equals("speedActive")) {
            return this.isSpeedActive();
        } else if (name.equals("phasing")) {
            return this.phasing;
        }
        return null;
    }


    @Override
    public boolean canSet(String name, Object variable) {
        return (name.equals("speedActive") && variable instanceof Boolean) || (name.equals("phasing") && variable instanceof Boolean) || this.isSpeedActive() && (name.equals("speedLevel") && variable instanceof Integer);
    }

    @Override
    public void set(String name, Object variable) {
        if (name.equals("speedLevel") && variable instanceof Integer level) {
            this.setSpeedLevel(level);
        } else if (name.equals("speedActive") && variable instanceof Boolean active) {
            this.setSpeedActive(active);
        } else if (name.equals("phasing") && variable instanceof Boolean phasing) {
            this.setPhasing(phasing);
        }
    }

    @Override
    public attributeModifiers getAttributeModifiers() {
        return super.getAttributeModifiers()
                .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, Identifier.of(MyneHeroes.MOD_ID, "speedster_power.speed"), EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, this::getSpeed)
                .addAttributeModifier(EntityAttributes.PLAYER_BLOCK_BREAK_SPEED, Identifier.of(MyneHeroes.MOD_ID, "speedster_power.block_break_speed"), EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, this::getSpeed)
                .addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, Identifier.of(MyneHeroes.MOD_ID, "speedster_power.attack_speed"), EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, this::getSpeed)
                .addAttributeModifier(EntityAttributes.GENERIC_STEP_HEIGHT, Identifier.of(MyneHeroes.MOD_ID, "speedster_power.step_height"), EntityAttributeModifier.Operation.ADD_VALUE, this::getStepHeight)
                .addAttributeModifier(EntityAttributes.GENERIC_FALL_DAMAGE_MULTIPLIER, Identifier.of(MyneHeroes.MOD_ID, "speedster_power.fall_damage"), EntityAttributeModifier.Operation.ADD_VALUE, this::getFallDamageMultiplier)
                .addAttributeModifier(EntityAttributes.GENERIC_SAFE_FALL_DISTANCE, Identifier.of(MyneHeroes.MOD_ID, "speedster_power.safe_fall_distance"), EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, this::getSafeFallDistance);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putBoolean("speedActive", speedActive);
        nbt.putBoolean("phasing", phasing);
        nbt.putInt("speedLevel", speedLevel);

        lastSegment.ifPresent(uuid -> nbt.putUuid("lastSegment", uuid));
        nbt.putInt("trailCooldown", trailCooldown);

        if (lastSegmentPos != null) {
            NbtCompound vecTag = new NbtCompound();
            vecTag.putDouble("x", lastSegmentPos.x);
            vecTag.putDouble("y", lastSegmentPos.y);
            vecTag.putDouble("z", lastSegmentPos.z);
            nbt.put("lastSegmentPos", vecTag);
        }

        NbtList upgradesNbt = new NbtList();

        for (Upgrade upgrade : upgrades) {
            upgradesNbt.add(upgrade.writeNbt(new NbtCompound()));
        }

        nbt.put("upgrades", upgradesNbt);

        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if (nbt.contains("speedActive")) {
            speedActive = nbt.getBoolean("speedActive");
        }

        if (nbt.contains("phasing")) {
            phasing = nbt.getBoolean("phasing");
        }

        if (nbt.contains("speedLevel")) {
            speedLevel = nbt.getInt("speedLevel");
        }

        if (nbt.contains("lastSegment")) {
            lastSegment = Optional.of(nbt.getUuid("lastSegment"));
        }

        if (nbt.contains("trailCooldown")) {
            trailCooldown = nbt.getInt("trailCooldown");
        }

        if (nbt.contains("lastSegmentPos", NbtElement.COMPOUND_TYPE)) {
            NbtCompound vecTag = nbt.getCompound("lastSegmentPos");
            double x = vecTag.getDouble("x");
            double y = vecTag.getDouble("y");
            double z = vecTag.getDouble("z");
            lastSegmentPos = new Vec3d(x, y, z);
        }


        NbtList upgradesNbt = new NbtList();
        if (nbt.contains("upgrades")) {
            upgradesNbt = nbt.getList("upgrades", NbtElement.COMPOUND_TYPE);
        }

        List<Upgrade> upgradeList = new ArrayList<>();
        for (NbtElement nbtElement : upgradesNbt) {
            if (nbtElement instanceof NbtCompound nbtCompound) {
                Upgrade upgrade = AbilityUtil.nbtToUpgrade(nbtCompound);
                if (upgrade != null) {
                    upgradeList.add(upgrade);
                }
            }
        }

        this.upgrades = upgradeList;
    }

    @Override
    public boolean canUpgrade(Upgrade upgrade) {
        return this.upgrades.size() < 6 && upgrade instanceof ColorUpgrade;
    }

    @Override
    public List<Upgrade> getUpgrades() {
        return upgrades;
    }

    @Override
    public void setUpgrades(List<Upgrade> upgrades) {
        this.upgrades = upgrades;
    }

    @Override
    public SpeedsterPower copy() {
        return new SpeedsterPower(id, color, abilities, maxSpeedLevel, settings, attributeModifiers);
    }
}
