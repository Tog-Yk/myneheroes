package net.togyk.myneheroes.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import net.togyk.myneheroes.entity.trail.LightningTrailEntity;
import net.togyk.myneheroes.entity.trail.TrailEntity;
import org.joml.Vector3f;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class LightningTrailRenderer {
    public static void render(LightningTrailEntity trail, float entityYaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int packedLight, PlayerEntityRenderer playerRenderer) {
        LaserRenderer laserRenderer = new LaserRenderer();
        laserRenderer.setSize(Math.max(0.15F * (1F - (trail.age / (float) trail.getLifetime())) - 0.03F, 0));

        List<Vector3f> thisConnectionPoints = trail.getConnectionPoints().stream()
                .map(vec -> new Vector3f(vec.x, vec.y, vec.z))
                .collect(Collectors.toList());
        thisConnectionPoints.sort(Comparator.comparing(Vector3f::y).reversed());

        List<Vector3f> thisBranches = trail.getBranches();

        for (Vector3f vec3f : thisBranches) {
            Vector3f connection = getClosestVector(vec3f, thisConnectionPoints);
            if (connection != null) {
                int colorIndex = ((int) (trail.getInnerColors().size() / (trail.getConnectionPoints().size() / (thisConnectionPoints.indexOf(connection) + 0.1))));
                laserRenderer.render(matrixStack, vertexConsumerProvider, new Vec3d(vec3f), new Vec3d(connection), 1F, trail.getInnerColors().get(colorIndex), trail.getColors().get(colorIndex));
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

        laserRenderer.setSize(0.15F * (1F - (trail.age / (float) trail.getLifetime())));

        Optional<TrailEntity> optionalConnectedSegment = trail.getConnectedSegment();
        if (optionalConnectedSegment.orElse(null) instanceof LightningTrailEntity connectedSegment) {
            List<Vector3f> connectedConnectionPoints = connectedSegment.getConnectionPoints().stream()
                    .map(vec -> new Vector3f(vec.x, vec.y, vec.z))
                    .collect(Collectors.toList());
            for (int i = 0; i < trail.getConnectionPoints().size(); i++) {
                int colorIndex = ((int) (trail.getInnerColors().size() / (trail.getConnectionPoints().size() / (i + 0.1))));
                Vector3f thisConnectionPoint = getHighestVector(thisConnectionPoints);
                Vector3f connectedConnectionPoint = getHighestVector(connectedConnectionPoints);

                if (thisConnectionPoint != null) {
                    if (connectedConnectionPoint != null) {
                        laserRenderer.render(matrixStack, vertexConsumerProvider, new Vec3d(thisConnectionPoint), new Vec3d(connectedConnectionPoint).add(connectedSegment.getPos()).subtract(trail.getPos()), 1F, trail.getInnerColors().get(colorIndex), trail.getColors().get(colorIndex));

                        thisConnectionPoints.remove(thisConnectionPoint);
                        connectedConnectionPoints.remove(connectedConnectionPoint);
                    } else {
                        Vector3f branch = getClosestVector(thisConnectionPoint, thisConnectionPoints);
                        if (branch != null) {
                            laserRenderer.render(matrixStack, vertexConsumerProvider, new Vec3d(thisConnectionPoint), new Vec3d(branch), 1F, trail.getInnerColors().get(colorIndex), trail.getColors().get(colorIndex));
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
}
