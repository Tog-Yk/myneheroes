package net.togyk.myneheroes.client.render.block_entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.togyk.myneheroes.block.entity.MeteorRadarBlockEntity;
import net.togyk.myneheroes.entity.MeteorEntity;
import net.togyk.myneheroes.entity.MeteorVariant;

import java.util.List;

public class MeteorRadarBlockEntityRenderer implements BlockEntityRenderer<MeteorRadarBlockEntity> {
    public MeteorRadarBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
    }

    @Override
    public void render(MeteorRadarBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        List<MeteorEntity> comets = entity.getMeteorsInRange();

        if (entity.getWorld() != null) {
            matrices.push();
            matrices.translate((float) 0.5, 0, (float) 0.5);
            for (MeteorEntity meteorN : comets) {
                MeteorEntity meteor = new MeteorEntity(entity.getWorld());
                meteor.copyFrom(meteorN);
                EntityRenderer<? super MeteorEntity> renderer = MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(meteor);
                matrices.push();
                BlockPos blockPos = entity.getPos();
                BlockPos cometPos = meteor.getBlockPos();

                Vec3d renderPos = Vec3d.of(new BlockPos(blockPos.subtract(cometPos))).multiply((float) 0.75 / entity.getRange()/2);
                matrices.translate(-renderPos.getX(), 1.1, -renderPos.getZ());

                meteor.setVariant(MeteorVariant.DEFAULT);
                meteor.setSize(0.1F);

                renderer.render(meteor, 0, 0, matrices, vertexConsumers, Math.min(getLightLevel(entity.getWorld(), blockPos.add(0,1,0)) + 1048592, 15728880));

                matrices.pop();
            }
            matrices.pop();
        }
    }

    private int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }
}
