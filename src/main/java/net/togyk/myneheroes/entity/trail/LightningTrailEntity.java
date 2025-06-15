package net.togyk.myneheroes.entity.trail;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.togyk.myneheroes.client.render.LaserRenderer;
import net.togyk.myneheroes.entity.ModEntities;
import net.togyk.myneheroes.entity.data.ModTrackedData;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public void render(float entityYaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int packedLight, PlayerEntityRenderer playerRenderer) {
        LaserRenderer laserRenderer = new LaserRenderer();
        laserRenderer.setSize(Math.max(0.15F * (1F - (this.age / (float) this.getLifetime())) - 0.03F, 0));

        List<Vector3f> thisConnectionPoints = this.getConnectionPoints().stream()
                .map(vec -> new Vector3f(vec.x, vec.y, vec.z))
                .collect(Collectors.toList());
        thisConnectionPoints.sort(Comparator.comparing(Vector3f::y).reversed());

        List<Vector3f> thisBranches = this.getBranches();

        for (Vector3f vec3f : thisBranches) {
            Vector3f connection = getClosestVector(vec3f, thisConnectionPoints);
            if (connection != null) {
                int colorIndex = ((int) (this.getInnerColors().size() / (this.getConnectionPoints().size() / (thisConnectionPoints.indexOf(connection) + 0.1))));
                laserRenderer.render(matrixStack, vertexConsumerProvider, new Vec3d(vec3f), new Vec3d(connection), 1F, this.getInnerColors().get(colorIndex), this.getColors().get(colorIndex));
            }
            /*
            debug points

            matrixStack.push();

            matrixStack.translate(vec3f.x, vec3f.y, vec3f.z);
            laserRenderer.render(matrixStack, vertexConsumerProvider, new Vec3d(0, 0, 0), laserRenderer.getSize(), 1F, 0x554455FF, 0x550011BB);

            matrixStack.pop();

             */
        }

        /*

        debug points
        for (Vector3f vector3f: thisConnectionPoints) {
            if (vector3f != null) {
                matrixStack.push();

                matrixStack.translate(vector3f.x, vector3f.y, vector3f.z);
                laserRenderer.render(matrixStack, vertexConsumerProvider, new Vec3d(0, 0, 0), laserRenderer.getSize(), 1F, 0x5544FF55, 0x5500BB11);

                matrixStack.pop();
            }
        }

         */

        laserRenderer.setSize(0.15F * (1F - (this.age / (float) this.getLifetime())));

        Optional<TrailEntity> optionalConnectedSegment = this.getConnectedSegment();
        if (optionalConnectedSegment.orElse(null) instanceof LightningTrailEntity connectedSegment) {
            List<Vector3f> connectedConnectionPoints = connectedSegment.getConnectionPoints().stream()
                    .map(vec -> new Vector3f(vec.x, vec.y, vec.z))
                    .collect(Collectors.toList());
            for (int i = 0; i < this.getConnectionPoints().size(); i++) {
                int colorIndex = ((int) (this.getInnerColors().size() / (this.getConnectionPoints().size() / (i + 0.1))));
                Vector3f thisConnectionPoint = getHighestVector(thisConnectionPoints);
                Vector3f connectedConnectionPoint = getHighestVector(connectedConnectionPoints);

                if (thisConnectionPoint != null) {
                    if (connectedConnectionPoint != null) {
                        laserRenderer.render(matrixStack, vertexConsumerProvider, new Vec3d(thisConnectionPoint), new Vec3d(connectedConnectionPoint).add(connectedSegment.getPos()).subtract(this.getPos()), 1F, this.getInnerColors().get(colorIndex), this.getColors().get(colorIndex));

                        thisConnectionPoints.remove(thisConnectionPoint);
                        connectedConnectionPoints.remove(connectedConnectionPoint);
                    } else {
                        Vector3f branch = getClosestVector(thisConnectionPoint, thisConnectionPoints);
                        if (branch != null) {
                            laserRenderer.render(matrixStack, vertexConsumerProvider, new Vec3d(thisConnectionPoint), new Vec3d(branch), 1F, this.getInnerColors().get(colorIndex), this.getColors().get(colorIndex));
                        }
                    }
                }
            }
        }
    }

    private static Vector3f getClosestVector(Vector3f target, List<Vector3f> vectors) {
        Vector3f closest = null;
        float DistanceSquared = 0;

        for (Vector3f vec : vectors) {
            float distanceSquared = vec.distanceSquared(target);
            if (DistanceSquared == 0 || distanceSquared < DistanceSquared) {
                DistanceSquared = distanceSquared;
                closest = vec;
            }
        }

        return closest;
    }

    private static Vector3f getHighestVector(List<Vector3f> vectors) {
        Vector3f highest = null;

        for (Vector3f vec : vectors) {
            if (highest == null || vec.y > highest.y) {
                highest = vec;
            }
        }

        return highest;
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
