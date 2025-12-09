package net.togyk.myneheroes.client.render;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;

public class ToolbeltModel extends BipedEntityModel<LivingEntity> {
    public static final EntityModelLayer TOOLBELT =
            new EntityModelLayer(Identifier.of(MyneHeroes.MOD_ID, "toolbelt"), "main");

    public ToolbeltModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        // This should create the vanilla armor model layout
        return TexturedModelData.of(BipedEntityModel.getModelData(new Dilation(0.5F), 0.0F), 64, 32);
    }
}