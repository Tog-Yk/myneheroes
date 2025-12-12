package net.togyk.myneheroes.client.render.entity;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.client.render.entity.states.MeteorEntityRendererState;

public class MeteorEntityModel extends EntityModel<MeteorEntityRendererState> {
    public static final EntityModelLayer METEOR = new EntityModelLayer(Identifier.of(MyneHeroes.MOD_ID, "meteor"), "main");
    private final ModelPart meteor;

    public MeteorEntityModel(ModelPart root) {
        super(root);
        this.meteor = root.getChild("meteor");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData comet = modelPartData.addChild("meteor", ModelPartBuilder.create(), ModelTransform.origin(0.0F, 24.0F, 0.0F));

        ModelPartData cube_r1 = comet.addChild("cube_r1", ModelPartBuilder.create().uv(0, 25).cuboid(-6.5F, -6.0F, -6.5F, 13.0F, 12.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.6109F, -0.5236F, 0.5585F));

        ModelPartData cube_r2 = comet.addChild("cube_r2", ModelPartBuilder.create().uv(0, 50).cuboid(-5.5F, -5.0F, -5.5F, 11.0F, 10.0F, 11.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.6981F, 0.6981F, 0.3491F));

        ModelPartData cube_r3 = comet.addChild("cube_r3", ModelPartBuilder.create().uv(0, 0).cuboid(-7.0F, -6.0F, -6.0F, 13.0F, 12.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.6109F, -0.6981F, 0.1745F));
        return TexturedModelData.of(modelData, 128, 128);
    }

    @Override
    public void setAngles(MeteorEntityRendererState state) {
    }
}
