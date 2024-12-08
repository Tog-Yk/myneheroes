package net.togyk.myneheroes.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.togyk.myneheroes.Item.custom.DyeableAdvancedArmorItem;
import net.togyk.myneheroes.MyneHeroes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> {
    /*
    this works partially
    @Inject(
            method = "renderArmor",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/feature/ArmorFeatureRenderer;renderArmorParts(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/model/BipedEntityModel;ILnet/minecraft/util/Identifier;)V",
                    ordinal = 0
            ),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private void injectModifyLayerColor(MatrixStack matrixStack, VertexConsumerProvider consumerProvider, LivingEntity entity, EquipmentSlot equipmentSlot, int integer, BipedEntityModel model, CallbackInfo ci, ItemStack stack, ArmorItem armorItem, boolean bl, ArmorMaterial armorMaterial, int i, Iterator iterator, ArmorMaterial.Layer layer, int j) {
        // Modify the local variable `j`
        if (stack.getItem() instanceof DyeableAdvancedArmorItem advancedArmorItem && layer.isDyeable()) {
            List<ArmorMaterial.Layer> layers = armorMaterial.layers();
            j = advancedArmorItem.getColor(stack, layers.indexOf(layer));
            MyneHeroes.LOGGER.info("{}?",j);
        }
    }
    Should work but doesn't
    @ModifyVariable(
            method = "renderArmor",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/feature/ArmorFeatureRenderer;renderArmorParts(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/model/BipedEntityModel;ILnet/minecraft/util/Identifier;)V",
                    ordinal = 0
            ),
            ordinal = 2
    )
    private int modifyColor(int j, @Local ItemStack itemStack, @Local ArmorMaterial.Layer layer) {
        if (layer.isDyeable() && itemStack.getItem() instanceof DyeableAdvancedArmorItem armorItem) {
            //getting the layers of the armorMaterial
            List<ArmorMaterial.Layer> layers = armorItem.getMaterial().value().layers();
            MyneHeroes.LOGGER.info("{}",armorItem.getColor(itemStack, layers.indexOf(layer)));
            return armorItem.getColor(itemStack, layers.indexOf(layer));
        } else {
            return j;
        }
    }
     */
    //Almost works
    @ModifyVariable(
            method = "renderArmor",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/feature/ArmorFeatureRenderer;renderArmorParts(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/model/BipedEntityModel;ILnet/minecraft/util/Identifier;)V",
                    ordinal = 0
            ),
            ordinal = 2
    )
    private int modifyColor(int j, @Local ItemStack itemStack, @Local ArmorMaterial.Layer layer) {
        MyneHeroes.LOGGER.info("{}",j);
        return -6265536;
    }
}
