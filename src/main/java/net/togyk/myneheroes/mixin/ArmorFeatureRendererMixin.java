package net.togyk.myneheroes.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.togyk.myneheroes.Item.custom.DyeableAdvancedArmorItem;
import net.togyk.myneheroes.MyneHeroes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> {
    //change the lightLevel of a armorMaterial layer based on the items NBT and if it extends the DyeableAdvancedArmorItem from this mod
    //only works on dyeable layers so check your armorMaterial
    @ModifyVariable(
            method = "renderArmor",
            at = @At("STORE"),
            ordinal = 2
    )
    private int modifyColor(int i, @Local ItemStack itemStack, @Local ArmorMaterial.Layer layer) {
        if (layer.isDyeable() && itemStack.getItem() instanceof DyeableAdvancedArmorItem armorItem) {
            List<ArmorMaterial.Layer> layers = armorItem.getMaterial().value().layers();
            return armorItem.getColor(itemStack,layers.indexOf(layer));
        } else {
            return i;
        }
    }
    @ModifyArg(
            method = "renderArmor",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/feature/ArmorFeatureRenderer;renderArmorParts(" +
                    "Lnet/minecraft/client/util/math/MatrixStack;" +
                    "Lnet/minecraft/client/render/VertexConsumerProvider;" +
                    "I" +
                    "Lnet/minecraft/client/render/entity/model/BipedEntityModel;" +
                    "I" +
                    "Lnet/minecraft/util/Identifier;" +
                    ")V"),
            index = 2)
    private int modifyLight(int light, @Local ItemStack itemStack, @Local ArmorMaterial.Layer layer) {
        if (itemStack.getItem() instanceof DyeableAdvancedArmorItem armorItem) {
            List<ArmorMaterial.Layer> layers = armorItem.getMaterial().value().layers();
            int layerIndex = layers.indexOf(layer);
            if (armorItem.layerIsLightable(itemStack, layerIndex)) {
                // times 1048576 to make a lightLever of 15 the maximum light
                MyneHeroes.LOGGER.info(String.valueOf((armorItem.getLightLevel(itemStack, layerIndex) / 15) * 15728880));
                MyneHeroes.LOGGER.info(String.valueOf(armorItem.getLightLevel(itemStack, layerIndex) / 15));
                return (armorItem.getLightLevel(itemStack, layerIndex) / 15) * 15728880;
            }
        }
        return light;
    }
}