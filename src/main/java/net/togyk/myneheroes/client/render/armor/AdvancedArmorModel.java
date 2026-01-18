package net.togyk.myneheroes.client.render.armor;// Made with Blockbench 5.0.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.Item.custom.DyeableAdvancedArmorItem;
import net.togyk.myneheroes.MyneHeroes;

@Environment(EnvType.CLIENT)
public class AdvancedArmorModel extends BipedEntityModel<LivingEntity> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final EntityModelLayer ADVANCED_ARMOR = new EntityModelLayer(Identifier.of(MyneHeroes.MOD_ID, "advanced_armor"), "main");
    public final ModelPart leftBoot;
    public final ModelPart rightBoot;

    public AdvancedArmorModel(ModelPart root) {
        super(root);
        this.leftBoot = root.getChild("left_boot");
        this.rightBoot = root.getChild("right_boot");
    }

    public AdvancedArmorModel(EntityModelLayer layer) {
        this(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(layer));
    }

    @Override
    protected Iterable<ModelPart> getBodyParts() {
        return ImmutableList.of(this.body, this.rightArm, this.leftArm, this.rightLeg, this.leftLeg, this.rightBoot, this.leftBoot, this.hat);
    }

    public Identifier getTexture(ItemStack stack, int layer_index) {
        if (stack.getItem() instanceof DyeableAdvancedArmorItem dyeableArmor) {
            ArmorMaterial.Layer layer = dyeableArmor.getMaterial().value().layers().get(layer_index);
            String layerPath = layer.getTexture(true).getPath();
            String layerName = layerPath.substring("textures/models/armor/".length(), layerPath.length() - "_layer_2.png".length());
            if (dyeableArmor.layerIsDyed(stack, layer_index)){
                layerName = "dyed_" + layerName;
            }
            return Identifier.of(layer.getTexture(true).getNamespace(), "textures/models/armor/" + layerName + ".png");
        }
        Identifier itemId = Registries.ITEM.getId(stack.getItem());
        return Identifier.of(itemId.getNamespace(), "textures/models/armor/" + itemId.getPath() + ".png");
    }

    public static ModelData getModelData(Dilation dilation, float pivotOffsetY) {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(
                EntityModelPartNames.HEAD,
                ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, dilation),
                ModelTransform.pivot(0.0F, 0.0F + pivotOffsetY, 0.0F)
        );

        modelPartData.addChild(
                EntityModelPartNames.HAT,
                ModelPartBuilder.create()
                        .uv(32, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, dilation.add(0.5F)),
                ModelTransform.pivot(0.0F, 0.0F + pivotOffsetY, 0.0F)
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
                        .uv(56, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation)
                        .uv(56, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.5F)),
                ModelTransform.pivot(2.0F, 12.0F, 0.0F)
        );

        modelPartData.addChild(
                "right_boot",
                ModelPartBuilder.create()
                        .uv(0, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.1F))
                        .uv(0, 64).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.6F)),
                ModelTransform.pivot(2.0F, 12.0F, 0.0F)
        );

        modelPartData.addChild(
                "left_boot",
                ModelPartBuilder.create()
                        .uv(16, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.1F))
                        .uv(16, 64).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.6F)),
                ModelTransform.pivot(2.0F, 12.0F, 0.0F)
        );


        return modelData;
    }



    public void setRotation(BipedEntityModel<LivingEntity> model) {
        model.copyBipedStateTo(this);
        this.rightBoot.copyTransform(model.rightLeg);
        this.leftBoot.copyTransform(model.leftLeg);
    }

    public void setEquipmentSlotVisible(EquipmentSlot slot) {
        this.head.hidden = true;
        this.hat.hidden = true;

        this.body.hidden = true;
        this.leftArm.hidden = true;
        this.rightArm.hidden = true;

        this.leftLeg.hidden = true;
        this.rightLeg.hidden = true;

        this.leftBoot.hidden = true;
        this.rightBoot.hidden = true;

        switch (slot) {
            case HEAD -> {
                this.head.hidden = false;
                this.hat.hidden = false;
            }
            case CHEST -> {
                this.body.hidden = false;
                this.leftArm.hidden = false;
                this.rightArm.hidden = false;
            }
            case LEGS -> {
                this.leftLeg.hidden = false;
                this.rightLeg.hidden = false;
            }
            case FEET -> {
                this.leftBoot.hidden = false;
                this.rightBoot.hidden = false;
            }
        }
    }

    public static TexturedModelData getTexturedModelData() {
        // This should create the vanilla armor model layout
        return TexturedModelData.of(AdvancedArmorModel.getModelData(new Dilation(0.5F), 0.0F), 128, 128);
    }
}