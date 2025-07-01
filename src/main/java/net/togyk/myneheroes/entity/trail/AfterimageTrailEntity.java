package net.togyk.myneheroes.entity.trail;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.togyk.myneheroes.entity.ModEntities;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class AfterimageTrailEntity extends TrailEntity {
    private static final TrackedData<Float> alpha = DataTracker.registerData(AfterimageTrailEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<NbtCompound> imageNbt = DataTracker.registerData(AfterimageTrailEntity.class, TrackedDataHandlerRegistry.NBT_COMPOUND);

    public AfterimageTrailEntity(EntityType<AfterimageTrailEntity> type, World world) {
        super(type, world);
    }

    public AfterimageTrailEntity(Entity parent, Optional<UUID> connectedSegment, int lifetime, float alpha) {
        super(parent, connectedSegment, lifetime, ModEntities.AFTERIMAGE_TRAIL);
        this.setAlpha(alpha);
        this.setImage(parent);
    }


    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(alpha, 0.5F);
        builder.add(imageNbt, new NbtCompound());
    }

    @NotNull
    public float getAlpha() {
        return this.getDataTracker().get(alpha);
    }

    public void setAlpha(float alpha) {
        this.getDataTracker().set(AfterimageTrailEntity.alpha, alpha);
    }

    public NbtCompound getImage() {
        return this.getDataTracker().get(imageNbt);
    }

    public void setImage(Entity image) {
        NbtCompound nbt = new NbtCompound();
        image.saveSelfNbt(nbt);

        if (image instanceof PlayerEntity player) {
            EntityType<?> entityType = player.getType();
            Identifier identifier = EntityType.getId(entityType);
            if (identifier != null) {
                nbt.putString("id", identifier.toString());
                nbt.put("Myneheroes$Inventory", player.getInventory().writeNbt(new NbtList()));
                player.writeNbt(nbt);
            }
        }

        this.getDataTracker().set(AfterimageTrailEntity.imageNbt, nbt);
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        nbt.putFloat("alpha", this.getAlpha());

        nbt.put("image_nbt", this.getDataTracker().get(imageNbt));
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        if (nbt.contains("alpha")) {
            this.setAlpha(nbt.getFloat("alpha"));
        }

        if (nbt.contains("image_nbt")) {
            this.getDataTracker().set(imageNbt, nbt.getCompound("image_nbt"));
        }
    }
}
