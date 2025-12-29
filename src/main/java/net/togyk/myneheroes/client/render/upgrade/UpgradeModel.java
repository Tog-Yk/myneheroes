package net.togyk.myneheroes.client.render.upgrade;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.upgrade.Upgrade;

public class UpgradeModel extends BipedEntityModel<LivingEntity> {
    public static final EntityModelLayer UPGRADE =
            new EntityModelLayer(Identifier.of(MyneHeroes.MOD_ID, "upgrade"), "main");

    public UpgradeModel(ModelPart root) {
        super(root);
    }

    public UpgradeModel(EntityModelLoader loader) {
        super(loader.getModelPart(UPGRADE));
    }

    public Identifier getTexture(ItemStack stack) {
        Identifier itemId = Registries.ITEM.getId(stack.getItem());
        return Identifier.of(itemId.getNamespace(), "textures/models/upgrade/" + itemId.getPath() + ".png");
    }

    public Identifier getTexture(Upgrade upgrade) {
        Identifier id = upgrade.getId();
        return Identifier.of(id.getNamespace(), "textures/models/upgrade/" + id.getPath() + ".png");
    }

    public void setEquipmentSlotVisible(EquipmentSlot slot) {
        this.head.hidden = true;
        this.hat.hidden = true;

        this.body.hidden = true;
        this.leftArm.hidden = true;
        this.rightArm.hidden = true;

        this.leftLeg.hidden = true;
        this.rightLeg.hidden = true;

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
                this.body.hidden = false;
                this.leftLeg.hidden = false;
                this.rightLeg.hidden = false;
            }
            case FEET -> {
                this.leftLeg.hidden = false;
                this.rightLeg.hidden = false;
            }
        }
    }

    public static TexturedModelData getTexturedModelData() {
        // This should create the vanilla armor model layout
        return TexturedModelData.of(BipedEntityModel.getModelData(new Dilation(0.5F), 0.0F), 64, 32);
    }
}
