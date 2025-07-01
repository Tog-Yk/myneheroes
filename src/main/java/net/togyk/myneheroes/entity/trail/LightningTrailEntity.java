package net.togyk.myneheroes.entity.trail;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.world.World;
import net.togyk.myneheroes.entity.ModEntities;
import net.togyk.myneheroes.entity.data.ModTrackedData;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.*;

public class LightningTrailEntity extends TrailEntity {
    private static final TrackedData<List<Vector3f>> connectionPoints = DataTracker.registerData(LightningTrailEntity.class, ModTrackedData.VEC3F_LIST);
    private static final TrackedData<List<Vector3f>> branches = DataTracker.registerData(LightningTrailEntity.class, ModTrackedData.VEC3F_LIST);
    private static final TrackedData<List<Integer>> colors = DataTracker.registerData(LightningTrailEntity.class, ModTrackedData.INTEGER_LIST);
    private static final TrackedData<List<Integer>> innerColors = DataTracker.registerData(LightningTrailEntity.class, ModTrackedData.INTEGER_LIST);

    public LightningTrailEntity(EntityType<LightningTrailEntity> lightningTrailEntityType, World world) {
        super(lightningTrailEntityType, world);
    }


    public LightningTrailEntity(Entity parent, Optional<UUID> connectedSegment, int lifetime) {
        super(parent, connectedSegment, lifetime, ModEntities.LIGHTNING_TRAIL);
    }

    public LightningTrailEntity(Entity parent, Optional<UUID> connectedSegment, int lifetime, int connectionPoints, int branches) {
        this(parent, connectedSegment, lifetime);

        this.setConnectionPoints(genConnectionPoints(connectionPoints));
        this.setBranches(genBranches(branches));
    }

    public LightningTrailEntity(Entity parent, Optional<UUID> connectedSegment, int lifetime, int connectionPoints, int branches, int color, int innerColor) {
        this(parent, connectedSegment, lifetime, connectionPoints, branches);

        this.setColors(List.of(color));
        this.setInnerColors(List.of(innerColor));
    }

    public LightningTrailEntity(Entity parent, Optional<UUID> connectedSegment, int lifetime, int connectionPoints, int branches, List<Integer> colors, List<Integer> innerColors) {
        this(parent, connectedSegment, lifetime, connectionPoints, branches);

        this.setColors(colors);
        this.setInnerColors(innerColors);
    }


    protected List<Vector3f> genConnectionPoints(int amount) {
        List<Vector3f> connectionPoints = new ArrayList<>();

        Random random = new Random();
        EntityPose pose = this.getPose();
        for (int i = 0; i < amount; i++) {
            connectionPoints.add(new Vector3f((float) random.nextDouble(this.getDimensions(pose).width()) - this.getDimensions(pose).width() / 2, (float) random.nextDouble(this.getDimensions(pose).height()), (float) random.nextDouble(this.getDimensions(pose).width()) - this.getDimensions(pose).width() / 2));
        }

        return connectionPoints;
    }

    protected List<Vector3f> genBranches(int amount) {
        List<Vector3f> branches = new ArrayList<>();

        Random random = new Random();
        EntityPose pose = this.getPose();
        for (int i = 0; i < amount; i++) {
            branches.add(new Vector3f((float) random.nextDouble(this.getDimensions(pose).width()) - this.getDimensions(pose).width() / 2, (float) random.nextDouble(this.getDimensions(pose).height()), (float) random.nextDouble(this.getDimensions(pose).width()) - this.getDimensions(pose).width() / 2));
        }

        return branches;
    }

    public List<Vector3f> getConnectionPoints() {
        return this.getDataTracker().get(connectionPoints);
    }

    public void setConnectionPoints(List<Vector3f> points) {
        this.getDataTracker().set(connectionPoints, points);
    }

    public List<Vector3f> getBranches() {
        return this.getDataTracker().get(branches);
    }

    public void setBranches(List<Vector3f> points) {
        this.getDataTracker().set(branches, points);
    }

    public List<Integer> getColors() {
        return this.getDataTracker().get(colors);
    }

    public void setColors(List<Integer> argb) {
        this.getDataTracker().set(colors, argb);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(connectionPoints, List.of());
        builder.add(branches, List.of());
        builder.add(colors, List.of(0x55FF0000));
        builder.add(innerColors, List.of(0xDDFFF0F0));
    }

    @NotNull
    public List<Integer> getInnerColors() {
        return this.getDataTracker().get(innerColors);
    }

    public void setInnerColors(List<Integer> argb) {
        this.getDataTracker().set(innerColors, argb);
    }
}
