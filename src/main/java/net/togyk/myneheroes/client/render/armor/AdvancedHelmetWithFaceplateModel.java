package net.togyk.myneheroes.client.render.armor;// Made with Blockbench 5.0.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.togyk.myneheroes.MyneHeroes;
import org.joml.Vector3f;

public class AdvancedHelmetWithFaceplateModel extends AdvancedArmorModel {
    private static final float[] ROTATION_TIMES = {0.0f, 0.475f, 0.75f, 1f};
    private static final float[][] ROTATION_KEYFRAMES = {
            {0f, 0f, 0f},
            {-15f, 0f, 0f},
            {-15f, 0f, 0f},
            {-65f, 0f, 0f}
    };

    private static final float[] POSITION_TIMES = {0.0f, 0.475f, 0.25f, 0.75f, 1f};
    private static final float[][] POSITION_KEYFRAMES = {
            {0f, 0f, 0f},
            {0f, 0f, -1.25f},
            {0f, 0f, -1.25f},
            {0f, -3.2f, -0.25f},
            {0f, -3.6f, 2.5F}
    };

    private static final float[] SCALE_TIMES = {0.0f, 0.475f};
    private static final float[][] SCALE_KEYFRAMES = {
            {1f, 1f, 1f},
            {1.01f, 1.01f, 1.01f}
    };


    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final EntityModelLayer ADVANCED_HELMET_W_FACEPLATE = new EntityModelLayer(Identifier.of(MyneHeroes.MOD_ID, "advanced_helmet"), "main");
    private final ModelPart faceplate;

    public AdvancedHelmetWithFaceplateModel(ModelPart root) {
        super(root);
        this.faceplate = root.getChild(EntityModelPartNames.HEAD).getChild("faceplate");
    }

    public AdvancedHelmetWithFaceplateModel(EntityModelLayer layer) {
        this(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(layer));
    }


    public static ModelData getModelData(Dilation dilation, float pivotOffsetY) {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData head = modelPartData.addChild(
                EntityModelPartNames.HEAD,
                ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, dilation.add(0.1F)),
                ModelTransform.pivot(0.0F, 0.0F + pivotOffsetY, 0.0F)
        );

        modelPartData.addChild(
                EntityModelPartNames.HAT,
                ModelPartBuilder.create()
                        .uv(32, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, dilation.add(0.5F).add(0.1F)),
                ModelTransform.pivot(0.0F, 0.0F + pivotOffsetY, 0.0F)
        );

        head.addChild(
                "faceplate",
                ModelPartBuilder.create()
                        .uv(64, 0).cuboid(-4.0F, -2.0F, 0.0F, 8.0F, 8.0F, 8.0F, dilation.add(0.1F))
                        .uv(96, 0).cuboid(-4.0F, -2.0F, 0.0F, 8.0F, 8.0F, 8.0F, dilation.add(0.5F).add(0.1F)),
                ModelTransform.pivot(0.0F, -6F, -4.0F)
        );

        modelPartData.addChild(
                EntityModelPartNames.BODY,
                ModelPartBuilder.create()
                        .uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, dilation)
                        .uv(16, 32).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, dilation.add(0.5F)),
                ModelTransform.pivot(0.0F, 0.0F + pivotOffsetY, 0.0F)
        );

        modelPartData.addChild(
                EntityModelPartNames.RIGHT_ARM,
                ModelPartBuilder.create()
                        .uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation)
                        .uv(40, 32).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.5F)),
                ModelTransform.pivot(-4.0F, 2.0F, 0.0F)
        );

        modelPartData.addChild(
                EntityModelPartNames.LEFT_ARM,
                ModelPartBuilder.create()
                        .uv(72, 16).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation)
                        .uv(72, 32).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.5F)),
                ModelTransform.pivot(4.0F, 2.0F, 0.0F)
        );

        modelPartData.addChild(
                EntityModelPartNames.RIGHT_LEG,
                ModelPartBuilder.create()
                        .uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation)
                        .uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.5F)),
                ModelTransform.pivot(-2.0F, 12.0F, 0.0F)
        );

        modelPartData.addChild(
                EntityModelPartNames.LEFT_LEG,
                ModelPartBuilder.create()
                        .uv(56, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(-0.1F))
                        .uv(56, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.4F)),
                ModelTransform.pivot(2.0F, 12.0F, 0.0F)
        );

        modelPartData.addChild(
                "right_boot",
                ModelPartBuilder.create()
                        .uv(0, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation)
                        .uv(0, 64).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.5F)),
                ModelTransform.pivot(2.0F, 12.0F, 0.0F)
        );

        modelPartData.addChild(
                "left_boot",
                ModelPartBuilder.create()
                        .uv(16, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation)
                        .uv(16, 64).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.5F)),
                ModelTransform.pivot(2.0F, 12.0F, 0.0F)
        );


        return modelData;
    }

    private static float[] interpolate(float time, float[] times, float[][] values) {
        // Clamp time
        if (time <= times[0]) return values[0];
        if (time >= times[times.length - 1]) return values[times.length - 1];

        int i;
        for (i = 0; i < times.length - 1; i++) {
            if (time < times[i + 1]) break;
        }

        float t0 = times[i];
        float t1 = times[i + 1];
        float factor = (time - t0) / (t1 - t0);

        float[] start = values[i];
        float[] end = values[i + 1];
        return new float[]{
                start[0] + (end[0] - start[0]) * factor,
                start[1] + (end[1] - start[1]) * factor,
                start[2] + (end[2] - start[2]) * factor
        };
    }


    public void setFaceplateOpenProgress(float progress) {
        // Clamp progress between 0..1
        progress = MathHelper.clamp(progress, 0f, 1f);

        // Interpolate rotation (degrees → radians)
        float[] rot = interpolate(progress, ROTATION_TIMES, ROTATION_KEYFRAMES);
        this.faceplate.pitch = (float)Math.toRadians(rot[0]);
        this.faceplate.yaw = (float)Math.toRadians(rot[1]);
        this.faceplate.roll = (float)Math.toRadians(rot[2]);

        // Interpolate position
        float[] pos = interpolate(progress, POSITION_TIMES, POSITION_KEYFRAMES);
        this.faceplate.setPivot(this.faceplate.pivotX + pos[0], this.faceplate.pivotY + pos[1], this.faceplate.pivotZ + pos[2]);

        // Interpolate scale
        float[] scale = interpolate(progress, SCALE_TIMES, SCALE_KEYFRAMES);
        this.faceplate.scale(new Vector3f(scale[0] - 1f, scale[1] - 1f, scale[2] - 1f));
    }

    @Override
    public void setRotation(BipedEntityModel<LivingEntity> model) {
        super.setRotation(model);
    }


    @Override
    public void setEquipmentSlotVisible(EquipmentSlot slot) {
        super.setEquipmentSlotVisible(slot);
        this.faceplate.hidden = slot != EquipmentSlot.HEAD;
    }

    public static TexturedModelData getTexturedModelData() {
        // This should create the vanilla armor model layout
        return TexturedModelData.of(AdvancedHelmetWithFaceplateModel.getModelData(new Dilation(0.5F), 0.0F), 128, 128);
    }
}