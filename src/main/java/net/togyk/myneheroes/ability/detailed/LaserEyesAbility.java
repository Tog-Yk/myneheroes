package net.togyk.myneheroes.ability.detailed;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.togyk.myneheroes.ability.StockpileLinkedAbility;
import net.togyk.myneheroes.client.render.LaserRenderer;
import net.togyk.myneheroes.damage.ModDamageTypes;
import net.togyk.myneheroes.power.Power;

public class LaserEyesAbility extends StockpileLinkedAbility {
    private final LaserRenderer laserRenderer = new LaserRenderer();

    private final int color;
    private final int innerColor;
    private Vec3d end;
    private float length = 0;

    public LaserEyesAbility(Identifier id, int cooldown, int unlocksAt, int cost, Settings settings, int color, int innerColor, int maxHoldTime) {
        super(id, cooldown, unlocksAt, cost, settings, null, null, maxHoldTime);
        this.hold = this::useLaserEyes;
        this.color = color;
        this.innerColor = innerColor;
    }

    public LaserEyesAbility(Identifier id, int unlocksAt, int cost, Settings settings, int color, int innerColor, int maxHoldTime) {
        this(id, maxHoldTime, unlocksAt, cost, settings, color, innerColor, maxHoldTime);
    }

    public LaserEyesAbility(Identifier id, int unlocksAt, int cost, Settings settings, int color, int innerColor) {
        this(id, 2, unlocksAt, cost, settings, color, innerColor, 0);
    }

    @Override
    public void tick(PlayerEntity player) {
        super.tick(player);
    }

    @Override
    public void useReleased(PlayerEntity player) {
        this.length = 0;
        this.end = null;
        this.setCooldown(Math.max(this.getHoldTime() * (this.getMaxCooldown() / this.getMaxHoldTime()), this.getCooldown()));
        super.useReleased(player);
    }

    public boolean useLaserEyes(PlayerEntity player) {
        this.length = Math.min(32, this.length + 2.5F);

        World world = player.getWorld();

        Vec3d start = player.getEyePos().add(0, 0, 0);
        Vec3d look = player.getRotationVec(1.0F);
        Vec3d end = start.add(look.multiply(this.length));


        BlockHitResult blockHit = world.raycast(new RaycastContext(
                start,
                end,
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.NONE,
                player
        ));

        // Damage first hit entity
        Box box = player.getBoundingBox()
                .stretch(look.multiply(this.length))
                .expand(1.0); // optional buffer

        EntityHitResult entityHit = ProjectileUtil.raycast(
                player,
                start,
                end,
                box,
                entity -> !entity.isSpectator() && entity.isAttackable(),
                this.length * this.length
        );


        if (entityHit != null && (blockHit.getType() == HitResult.Type.MISS ||
                start.squaredDistanceTo(entityHit.getPos()) < start.squaredDistanceTo(blockHit.getPos()))) {
            // Damage entity
            Entity hitEntity = entityHit.getEntity();
            hitEntity.damage(ModDamageTypes.of(player.getWorld(), ModDamageTypes.LASER_TYPE_KEY, player), 5.0F);

            end = entityHit.getPos();
        } else if (blockHit.getType() == HitResult.Type.BLOCK) {

            end = blockHit.getPos();
        }

        this.end = end;

        this.save();
        return true;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putFloat("length", this.length);

        if (end != null) {
            NbtCompound endNbt = new NbtCompound();
            endNbt.putDouble("x", end.x);
            endNbt.putDouble("y", end.y);
            endNbt.putDouble("z", end.z);
            nbt.put("end", endNbt);
        }

        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if (nbt.contains("length")) {
            this.length = nbt.getFloat("length");
        }

        if (nbt.contains("end")) {
            NbtCompound endNbt = nbt.getCompound("end");
            this.end = new Vec3d(endNbt.getDouble("x"), endNbt.getDouble("y"), endNbt.getDouble("z"));
        }
    }

    public LaserRenderer getLaserRenderer() {
        return laserRenderer;
    }

    public Vec3d getStart(float tickDelta) {
        if (this.getIndirectHolder() instanceof Power power && power.getHolder() != null) {
            PlayerEntity player = power.getHolder();
            // Interpolated position
            double x = MathHelper.lerp(tickDelta, player.prevX, player.getX());
            double y = MathHelper.lerp(tickDelta, player.prevY, player.getY());
            double z = MathHelper.lerp(tickDelta, player.prevZ, player.getZ());

            // Add eye height
            return new Vec3d(x, y + player.getEyeHeight(player.getPose()), z);
        }
        return null;
    }

    public Vec3d getEnd(float tickDelta) {
        return this.end;
    }

    public int getInnerColor() {
        return innerColor;
    }

    public int getColor() {
        return color;
    }

    @Override
    public LaserEyesAbility copy() {
        return new LaserEyesAbility(id, maxCooldown, unlocksAt, cost, settings, innerColor, color, maxHoldTime);
    }
}
